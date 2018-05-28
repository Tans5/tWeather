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

import org.w3c.dom.Text;

/**
 * Created by 鹏程 on 2018/5/18.
 */

public class SignUpDialogBuilder {
    SignUpDialog mDialog = null;

    SignUpListener listener;

    public interface SignUpListener {
        void signUp(String name, String password, String passwordRepeat, SignUpDialog dialog);

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
        EditText mNameEt;
        EditText mPasswordEt;
        EditText mPasswordRepeatEt;
        TextView mWaitingTx;
        LinearLayout mContentLl;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mContentView = inflater.inflate(R.layout.layout_sign_up_dialog, container);
            initViews();
            return mContentView;
        }

        private void initViews() {
            mSignUpBt = mContentView.findViewById(R.id.sign_up_dialog_bt_sign_up);
            mCancelBt = mContentView.findViewById(R.id.sign_up_dialog_bt_cancel);
            mResetBt = mContentView.findViewById(R.id.sign_up_dialog_bt_reset);
            mNameEt = mContentView.findViewById(R.id.sign_up_dialog_et_name);
            mPasswordEt = mContentView.findViewById(R.id.sign_up_dialog_et_password);
            mPasswordRepeatEt = mContentView.findViewById(R.id.sign_up_dialog_et_password1);
            mWaitingTx = mContentView.findViewById(R.id.sign_up_dialog_tv_waiting);
            mContentLl = mContentView.findViewById(R.id.sign_up_dialog_ll_content);
            mSignUpBt.setOnClickListener(this);
            mResetBt.setOnClickListener(this);
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
                    case R.id.sign_up_dialog_bt_cancel:
                        listener.cancel(this);
                        break;
                    case R.id.sign_up_dialog_bt_reset:
                        mNameEt.setText("");
                        mPasswordEt.setText("");
                        mPasswordRepeatEt.setText("");
                        listener.reset(this);
                        break;
                    case R.id.sign_up_dialog_bt_sign_up:
                        listener.signUp(mNameEt.getText().toString(),
                                mPasswordEt.getText().toString(),
                                mPasswordRepeatEt.getText().toString(),
                                this);
                        break;
                }
            }
        }

        public void showWaiting() {
            mWaitingTx.setVisibility(View.VISIBLE);
            mContentLl.setVisibility(View.INVISIBLE);
        }

        public void showContent() {
            mWaitingTx.setVisibility(View.GONE);
            mContentLl.setVisibility(View.VISIBLE);
        }

    }
}
