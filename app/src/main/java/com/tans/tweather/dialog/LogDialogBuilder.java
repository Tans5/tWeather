package com.tans.tweather.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tans.tweather.R;
import com.tans.tweather.utils.DensityUtils;

/**
 * Created by 鹏程 on 2018/5/18.
 */

public class LogDialogBuilder {
    LogDialog mDialog = null;

    LogDialogListener listener;

    public interface LogDialogListener {
        void log(String name, String password,LogDialog dialog);
        void cancel(LogDialog dialog);
        void signUp(LogDialog dialog);
    }

    public LogDialogBuilder() {
    }
    public LogDialogBuilder setListener(LogDialogListener listener) {
        this.listener = listener;
        return this;
    }
    public LogDialog build() {
        mDialog = new LogDialog();
        mDialog.setListener(listener);
        return mDialog;
    }
    public static class LogDialog extends DialogFragment implements View.OnClickListener {

        LogDialogListener listener;
        View mContentView;

        Button mLogBt;
        Button mCancelBt;
        Button mSignUpBt;
        EditText mNameEt;
        EditText mPasswordEt;
        TextView mWaitingTv;
        LinearLayout mContentLl;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mContentView = inflater.inflate(R.layout.layout_log_dialog,container);
            initViews();
            return mContentView;
        }

        private void initViews() {
            mLogBt = mContentView.findViewById(R.id.log_dialog_bt_log);
            mCancelBt = mContentView.findViewById(R.id.log_dialog_bt_cancel);
            mSignUpBt = mContentView.findViewById(R.id.log_dialog_bt_sign_up);
            mPasswordEt = mContentView.findViewById(R.id.log_dialog_et_password);
            mNameEt = mContentView.findViewById(R.id.log_dialog_et_name);
            mWaitingTv = mContentView.findViewById(R.id.log_dialog_tv_waiting);
            mContentLl = mContentView.findViewById(R.id.log_dialog_ll_content);
            mLogBt.setOnClickListener(this);
            mCancelBt.setOnClickListener(this);
            mSignUpBt.setOnClickListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            getDialog().getWindow().setLayout((int)(DensityUtils.getScreenWith(getActivity())*0.8),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        public void setListener(LogDialogListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(listener != null) {
                int id = v.getId();
                switch (id) {
                    case R.id.log_dialog_bt_cancel:
                        listener.cancel(this);
                        break;
                    case R.id.log_dialog_bt_log:
                        listener.log(mNameEt.getText().toString(),mPasswordEt.getText().toString(),this);
                        break;
                    case R.id.log_dialog_bt_sign_up:
                        listener.signUp(this);
                        break;

                }
            }
        }

        public void showWaiting() {
            mWaitingTv.setVisibility(View.VISIBLE);
            mContentLl.setVisibility(View.INVISIBLE);
        }

        public void showContent() {
            mWaitingTv.setVisibility(View.GONE);
            mContentLl.setVisibility(View.VISIBLE);
        }
    }
}
