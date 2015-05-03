package com.wms.weixinchat;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioManager {
    private static AudioManager mInstance;
    public boolean isPrepared;
    //录音文件保存的目录
    private String mDir = "/sdcard/weixin";
    private MediaRecorder recorder;
    private String mCurrentSavePath;
    private OnPreparedOkListener listener;

    private AudioManager() {
    }

    public static AudioManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 录音之前的准备
     */
    public void prepare() {
        isPrepared = false;
        File saveDir = new File(mDir);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        //根据UUID创建文件名称
        String fileName = UUID.randomUUID().toString() + ".amr";
        File savePath = new File(saveDir, fileName);
        mCurrentSavePath = savePath.getAbsolutePath();
        if (!savePath.exists()) {
            try {
                savePath.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //初始化录音机对象
        if (recorder == null)
            recorder = new MediaRecorder();
        recorder.setOutputFile(savePath.getAbsolutePath());
        //设置输出文件
        recorder.setOutputFile(savePath.getAbsolutePath());
        //设置MediaRecoder的音频远为麦克风
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //音频格式
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        //音频编码
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //准备录音
        try {
            recorder.prepare();
            //开始
            recorder.start();

            isPrepared = true;

            if (listener != null) {
                listener.preparedOk();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListener(OnPreparedOkListener listener) {
        this.listener = listener;
    }

    /**
     * 用户放弃录音
     */
    public void cancle() {
        //取消录音
        saveRecoder();
        //删除保存的录音文件
        File file = new File(mCurrentSavePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 用户录音结束，保存录音
     */
    public void saveRecoder() {
        if (recorder == null)
            return;
        recorder.release();
        recorder = null;
        isPrepared = false;
    }

    /**
     * 获取当前的音量大小
     *
     * @param maxLevel
     * @return
     */
    public int getVolumLevel(int maxLevel) {
        if (isPrepared && recorder != null) {
            try {
                //mMediaRecorder.getMaxAmplitude() -32768~32767
                return maxLevel * recorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
            }
        }
        return 1;
    }

    public String getCurrentSavePath() {
        return mCurrentSavePath;
    }


    public interface OnPreparedOkListener {
        void preparedOk();
    }
}
