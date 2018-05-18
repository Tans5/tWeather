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

import com.tans.tweather.R;
import com.tans.tweather.utils.DensityUtils;

/**
 * Created by 鹏程 on 2018/5/18.
 */

public class SignUpDialogBuilder {
    SignUpDialog mDialog = null;

    SignUpListener listener;

    public interface SignUpListener {
        void signUp(String name, String password,SignUpDialog dialog);

        void reset(SignUpDialog dialog);

        void cancel(SignUpDialog dialog);
    }

    public SignUpDialogBuilder() {
    }

    public SignUpDialogBuilder setListener(SignUpListener listener) {
        this.listener = listener;
        return this;
    }

    public SignUpDialog build() {
        mDialog = new SignUpDialog();
        mDialog.setListener(listener);
        return mDialog;
    }

    public static class SignUpDialog extends DialogFragment implements View.OnClickListener {

        SignUpListener listener;
        View mContentView;

        Button mSignUpBt;
        Button mCancelBt;
        Button mResetBt;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mContentView = inflater.inflate(R.layout.layout_sign_up_dialog, container);
            initViews();
            return mContentView;
        }

        private void initViews() {
            mSignUpBt = mContentView.findViewById(R.id.sign_up_dialog_bt_sign_up);
            mCancelBt = mContentView.findViewById(R.id.sign_up_bt_cancel);
            mResetBt = mContentView.findViewById(R.id.sign_up_bt_reset);

            mCancelBt.setOnClickListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            getDialog().getWindow().setLayout((int) (DensityUtils.getScreenWith(getActivity()) * 0.8),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        public void setListener(SignUpListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int id = v.getId();
                switch (id) {
                    case R.id.sign_up_bt_cancel:
                        listener.cancel(this);
                        break;
                }
            }
        }
    }
}
