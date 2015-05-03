package com.wms.weixinchat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class VoiceButton extends Button implements AudioManager.OnPreparedOkListener {
    private static final float YDISTANCE = 150;
    //录音结束标记
    private static final int PREPARED_OK = 3;
    private static final int UPDATE_LEVEL = 4;
    //正常状态
    private static final int STATE_NORMAL = 0;
    //正在录音状态
    private static final int STATE_RECORDING = 1;
    //准备取消状态
    private static final int STATE_WANT_CANCLE = 2;
    private static final int DISMISS_DIALOG = 5;
    private final DialogManager dialogManager;
    private final AudioManager audioManager;
    private Context mContext;
    //当前状态初始化为正常状态
    private int mCurrentStatus = STATE_NORMAL;
    //是否正在录音
    private boolean isRecording;
    //录音时间
    private double mRecordTime;
    //开始录音的时间
    private long mStartTime;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PREPARED_OK:
                    //准备好就开始录音
                    isRecording = true;
                    //开启一个线程去实时更新音量
                    new Thread(updateVolumLevelRunnable).start();
                    break;
                case UPDATE_LEVEL:
                    //如果一直处于录音状态，则更新音量，取消录音，不用更新了;
                    dialogManager.setVolumLevel(audioManager.getVolumLevel(7));
                    break;
                case DISMISS_DIALOG:
                    isRecording = false;
                    dialogManager.dismissDialog();
                    break;
            }
        }
    };
    private Runnable updateVolumLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mRecordTime = mRecordTime + 0.1;
                    mHandler.sendEmptyMessage(UPDATE_LEVEL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private OnRecordOkListener listener;

    public VoiceButton(Context context) {
        this(context, null);
    }

    public VoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        dialogManager = new DialogManager(context);
        audioManager = AudioManager.getInstance();
        audioManager.setListener(this);
//        setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setButtonStatus(STATE_RECORDING);
                //录音准备工作
                if (!audioManager.isPrepared) {
                    audioManager.prepare();
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                if (isRecording) {
                if (wantToCancle(event.getX(), event.getY())) {
                    setButtonStatus(STATE_WANT_CANCLE);
                } else {
                    setButtonStatus(STATE_RECORDING);
                }
//                }
                break;
            case MotionEvent.ACTION_UP:
                //手指按下的时间小于0.6秒，则认为是时间过短
                if (mRecordTime < 0.6f) {
                    dialogManager.showShortDialog();
                    audioManager.cancle();
                } else if (mCurrentStatus == STATE_RECORDING) {
//                    保存录音
                    audioManager.saveRecoder();
                    if (listener != null) {
                        if (mRecordTime < 1) {
                            mRecordTime = 1;
                        }
                        listener.recordOk((int) mRecordTime, audioManager.getCurrentSavePath());
                    }
                } else {
                    //删除录音
                    audioManager.cancle();
                }

                reset();
                break;
        }
        return true;
    }

    //恢复状态 标志位
    private void reset() {
        mRecordTime = 0;
        mHandler.sendEmptyMessage(DISMISS_DIALOG);
        setButtonStatus(STATE_NORMAL);
    }

    /**
     * 判断是否是准备取消录音
     *
     * @param x
     * @param y
     * @return
     */
    private boolean wantToCancle(float x, float y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -YDISTANCE || y > YDISTANCE) {
            return true;
        }
        return false;
    }

    private void setButtonStatus(int status) {
        if (status != mCurrentStatus) {
            mCurrentStatus = status;
            switch (mCurrentStatus) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recoder_normal);
                    setText(getResources().getString(R.string.str_pressed_speak));
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recoding);
                    setText(getResources().getString(R.string.str_release_end));
                    dialogManager.showRecordingDialog();
                    break;
                case STATE_WANT_CANCLE:
                    setBackgroundResource(R.drawable.btn_recoding);
                    setText(getResources().getString(R.string.str_cancle));
                    dialogManager.wantToCancle();
                    break;
            }
        }
    }

    @Override
    public void preparedOk() {
        //录音准备好之后的回调
        mHandler.sendEmptyMessage(PREPARED_OK);
        mStartTime = System.currentTimeMillis();
    }

    public void setListener(OnRecordOkListener listener) {
        this.listener = listener;
    }

    public interface OnRecordOkListener {
        void recordOk(int recordTime, String savePath);
    }


}
