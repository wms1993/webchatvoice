package com.wms.weixinchat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity implements VoiceButton.OnRecordOkListener {

    private ListView listView;
    private ArrayList<VoiceBean> voices = new ArrayList<VoiceBean>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((VoiceButton) findViewById(R.id.voice_button)).setListener(this);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new MyAdapter(this);
        adapter.setVoices(voices);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void recordOk(int recordTime, String savePath) {
        //录音成功的回调
        VoiceBean bean = new VoiceBean();
        bean.setmRecordTime(recordTime);
        bean.setmRecordPath(savePath);
        voices.add(bean);
        bean = null;
        adapter.notifyDataSetChanged();
    }
}
