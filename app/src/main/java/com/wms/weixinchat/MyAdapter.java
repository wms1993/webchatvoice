package com.wms.weixinchat;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<VoiceBean> voices = new ArrayList<VoiceBean>();
    private Context mContext;
    private ImageView laba;

    public MyAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return voices.size();
    }

    @Override
    public Object getItem(int position) {
        return voices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.voice_item, null);
            holder.recorderTime = (TextView) convertView.findViewById(R.id.recoder_time);
            holder.bg_chat = (FrameLayout) convertView.findViewById(R.id.bg_chat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.recorderTime.setText(voices.get(position).getmRecordTime() + "'");
        holder.bg_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置动画
                View animView = (View) view.findViewById(R.id.laba);
                animView.setBackgroundResource(R.drawable.voiceplay_anim);
                AnimationDrawable d = (AnimationDrawable) animView.getBackground();
                d.start();
                MediaManager.playSound(voices.get(position).getmRecordPath());
            }
        });
        return convertView;
    }

    public void setVoices(List<VoiceBean> voices) {
        this.voices = voices;
    }

    private class ViewHolder {
        public TextView recorderTime;
        public FrameLayout bg_chat;
    }
}
