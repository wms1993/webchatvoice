package com.wms.weixinchat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by love√Ù on 2015/5/3 0003.
 */
public class MyAdapter extends BaseAdapter{

    private List<VoiceBean> voices = new ArrayList<VoiceBean>();

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
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setVoices(List<VoiceBean> voices) {
        this.voices = voices;
    }
}
