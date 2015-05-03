package com.wms.weixinchat;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogManager {
    private Dialog mDialog;
    private Context mContext;
    private ImageView mIcon;
    private ImageView mLevel;
    private TextView mTips;
    private ImageView mCancle;
    private ImageView too_short;

    public DialogManager(Context context) {
        mContext = context;
    }

    /**
     * 显示正在录音对话框
     */
    public void showRecordingDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.recoder_dialog, null);
        mDialog.setContentView(view);
        mIcon = (ImageView) view.findViewById(R.id.recoder);
        mLevel = (ImageView) view.findViewById(R.id.level);
        mCancle = (ImageView) view.findViewById(R.id.cancle);
        mTips = (TextView) view.findViewById(R.id.recoder_tips);
        too_short = (ImageView) view.findViewById(R.id.too_short);

        mDialog.show();
    }

    /**
     * 准备取消录音对话框
     */
    public void wantToCancle() {
        if (mDialog != null && mDialog.isShowing()) {
            mTips.setText(mContext.getResources().getString(R.string.str_release_cancle));
            mTips.setBackgroundResource(R.drawable.warm);
            mIcon.setVisibility(View.GONE);
            mLevel.setVisibility(View.GONE);
            mCancle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏对话框
     */
    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 设置音量级别
     *
     * @param level
     */
    public void setVolumLevel(int level) {
        if (mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources().getIdentifier("v" + level, "drawable", mContext.getPackageName());
            mLevel.setImageResource(resId);
        }
    }

    public void showShortDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            Toast.makeText(mContext, "时间太短了", Toast.LENGTH_SHORT).show();
            mIcon.setVisibility(View.GONE);
            mLevel.setVisibility(View.GONE);
            mCancle.setVisibility(View.GONE);
            too_short.setVisibility(View.VISIBLE);
            mTips.setText(mContext.getResources().getString(R.string.str_too_short));
        }
    }
}
