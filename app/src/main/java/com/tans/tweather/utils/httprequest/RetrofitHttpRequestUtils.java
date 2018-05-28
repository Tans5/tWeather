package com.tans.tweather.utils.httprequest;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.bean.request.HttpGetParams;
import com.tans.tweather.bean.weather.AtmosphereBean;
import com.tans.tweather.bean.weather.ConditionBean;
import com.tans.tweather.bean.weather.ForecastBean;
import com.tans.tweather.bean.weather.WindBean;
import com.tans.tweather.utils.ResponseConvertUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 鹏程 on 2018/5/23.
 */

public class RetrofitHttpRequestUtils extends BaseHttpRequestUtils {

    private static RetrofitHttpRequestUtils instance;

    private Retrofit.Builder mBuilder = null;

    public static class WindConverter implements Converter<ResponseBody,WindBean> {

        @Override
        public WindBean convert(ResponseBody value) throws IOException {
            String jsonString = ResponseConvertUtils.getWindJsonString(value.string());
            Gson gson = new Gson();
            WindBean result = gson.fromJson(jsonString,WindBean.class);
            return result;
        }
    }

    public static class AtmosphereConverter implements Converter<ResponseBody,AtmosphereBean> {

        @Override
        public AtmosphereBean convert(ResponseBody value) throws IOException {
            String jsonString = ResponseConvertUtils.getAtmosphereJsonString(value.string());
            Gson gson = new Gson();
            AtmosphereBean result = gson.fromJson(jsonString,AtmosphereBean.class);
            return result;
        }
    }

    public static class ForecastConverter implements Converter<ResponseBody,List> {

        @Override
        public List convert(ResponseBody value) throws IOException {
            String jsonString = ResponseConvertUtils.getFutureConditionJsonString(value.string());
            Gson gson = new Gson();
            List<ForecastBean> result = gson.fromJson(jsonString, new TypeToken<ArrayList<ForecastBean>>() {
            }.getType());
            return result;
        }
    }

    public static class ConditionConverter implements Converter<ResponseBody,ConditionBean> {

        @Override
        public ConditionBean convert(ResponseBody value) throws IOException {
            String jsonString = ResponseConvertUtils.getCurrentConditionJsonString(value.string());
            Gson gson = new Gson();
            ConditionBean result = gson.fromJson(jsonString,ConditionBean.class);
            return result;
        }
    }

    public static class StringConverter implements Converter<ResponseBody,String> {

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    public static class ResponseBody2BeanConverter<T> implements Converter<ResponseBody,T> {
        private Gson gson;
        private TypeAdapter<T> adapter;
        public ResponseBody2BeanConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }
        @Override
        public T convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        }
    }

    public static class Bean2RequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final static MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final static Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        Bean2RequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    public static class BeanConverterFactory extends Converter.Factory {
        private WindConverter windConverter = new WindConverter();
        private ForecastConverter forecastConverter = new ForecastConverter();
        private AtmosphereConverter atmosphereConverter = new AtmosphereConverter();
        private ConditionConverter conditionConverter = new ConditionConverter();
        private StringConverter stringConverter = new StringConverter();
        private static BeanConverterFactory instance;
        private Type responseType;

        public static BeanConverterFactory create(Type responseType) {
            if(instance == null) {
                instance = new BeanConverterFactory();
            }
            instance.responseType = responseType;
            return instance;
        }

        @Nullable
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            Converter result = null;
            if(type == WindBean.class) {
                result = windConverter;
            } else if(type == List.class) {
                result = forecastConverter;
            } else if(type == AtmosphereBean.class) {
                result = atmosphereConverter;
            } else if(type == ConditionBean.class) {
                result = conditionConverter;
            } else if(type == String.class) {
                result = stringConverter;
            } else {
                Gson gson = new Gson();
                TypeAdapter adapter = gson.getAdapter(TypeToken.get(responseType));
                result = new ResponseBody2BeanConverter<>(gson,adapter);
            }
            return result;
        }

        @Nullable
        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            Gson gson = new Gson();
            TypeAdapter adapter = gson.getAdapter(TypeToken.get(type));
            return new Bean2RequestBodyConverter<>(gson,adapter);
        }

        private BeanConverterFactory() {

        }
    }

    public interface WeatherService {
        @GET
        Observable<WindBean> getWind(@Url String url, @QueryMap Map<String,String> params);

        @GET
        Observable<List> listForecast(@Url String url, @QueryMap Map<String,String> params);

        @GET
        Observable<AtmosphereBean> getAtmosphere(@Url String url, @QueryMap Map<String,String> params);

        @GET
        Observable<ConditionBean> getCondition(@Url String url, @QueryMap Map<String,String> params);
    }

    public interface GetService {
        @GET
        Observable<String> getStringResult(@Url String url, @QueryMap Map<String,String> params);

        @GET
        Observable<String> getStringResult(@Url String url);

        @GET
        Observable<Object> getObjectResult(@Url String url, @QueryMap Map<String,String> params);
    }

    public interface PostService {
        @Headers({"Content-type:application/json;charset=UTF-8"})
        @POST
        Observable<Object> getObjectResult(@Url String url,@Body Object body);

        @Headers({"Content-type:application/json;charset=UTF-8"})
        @POST
        Observable<String> getStringResult(@Url String url,@Body Object body);
    }

    public static RetrofitHttpRequestUtils newInstance() {
        if(instance == null) {
            instance = new RetrofitHttpRequestUtils();
            instance.init(BaseApplication.getInstance());
        }
        return  instance;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    @Override
    public void request(String baseUrl, String path, HttpRequestMethod method, Object requestParams, final HttpRequestListener listener) {
        Class c = listener.getResultType();
        Retrofit retrofit = mBuilder.baseUrl(baseUrl)
                .addConverterFactory(BeanConverterFactory.create(c))
                .build();
        if(c == WindBean.class) {
            retrofit.create(WeatherService.class)
                    .getWind(path, ((HttpGetParams)requestParams).getParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<WindBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onFail(e.getMessage());
                        }

                        @Override
                        public void onNext(WindBean windBean) {
                            listener.onSuccess(windBean);
                        }
                    });
        } else if(c == List.class) {
            retrofit.create(WeatherService.class)
                    .listForecast(path, ((HttpGetParams)requestParams).getParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onFail(e.getMessage());
                        }

                        @Override
                        public void onNext(List forecast) {
                            listener.onSuccess(forecast);
                        }
                    });

        } else if(c == AtmosphereBean.class) {
            retrofit.create(WeatherService.class)
                    .getAtmosphere(path, ((HttpGetParams)requestParams).getParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AtmosphereBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onFail(e.getMessage());
                        }

                        @Override
                        public void onNext(AtmosphereBean atmosphere) {
                            listener.onSuccess(atmosphere);
                        }
                    });

        } else if(c == ConditionBean.class) {
            retrofit.create(WeatherService.class)
                    .getCondition(path, ((HttpGetParams)requestParams).getParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ConditionBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onFail(e.getMessage());
                        }

                        @Override
                        public void onNext(ConditionBean condition) {
                            listener.onSuccess(condition);
                        }
                    });
        } else if(c == String.class) {
            if(requestParams != null) {
                retrofit.create(GetService.class)
                        .getStringResult(path, ((HttpGetParams) requestParams).getParams())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFail(e.getMessage());
                            }

                            @Override
                            public void onNext(String s) {
                                listener.onSuccess(s);
                            }
                        });
            } else {
                retrofit.create(GetService.class)
                        .getStringResult(path)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFail(e.getMessage());
                            }

                            @Override
                            public void onNext(String s) {
                                listener.onSuccess(s);
                            }
                        });
            }
        } else {
            if(method == HttpRequestMethod.GET) {
                retrofit.create(GetService.class)
                        .getObjectResult(path, ((HttpGetParams)requestParams).getParams())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Object>() {

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFail(e.getMessage());
                            }

                            @Override
                            public void onNext(Object o) {
                                listener.onSuccess(o);
                            }
                        });
            } else if(method == HttpRequestMethod.POST) {
                retrofit.create(PostService.class)
                        .getObjectResult(path,requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Object>() {

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFail(e.getMessage());
                            }

                            @Override
                            public void onNext(Object o) {
                                listener.onSuccess(o);
                            }
                        });
            }
        }
    }

    private RetrofitHttpRequestUtils() {
    }

}
