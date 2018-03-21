package com.tans.tweather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tans.tweather.R;
import com.tans.tweather.database.bean.LocationBean;

import java.util.List;

/**
 * Created by mine on 2018/3/21.
 */

@SuppressLint("ViewConstructor")
public class CitiesRecyclerAdapter extends RecyclerView.Adapter<CitiesRecyclerAdapter.ViewHolder> {

    List<LocationBean> data;

    ItemClickListener mItemClickListenter;

    Context mContext;

    public interface ItemClickListener {
        void onClick(int position,int level);
    }

    public CitiesRecyclerAdapter (Context context, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mItemClickListenter = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_city,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItemClickListenter(position);
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<LocationBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<LocationBean> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
        }
        public void setItemClickListenter (final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListenter.onClick(position,data.get(position).getLevel());
                }
            });
        }

        public void setData(LocationBean data) {
            TextView city = itemView.findViewById(R.id.tv_city);
            city.setText(data.getCityName());
        }
    }
}
