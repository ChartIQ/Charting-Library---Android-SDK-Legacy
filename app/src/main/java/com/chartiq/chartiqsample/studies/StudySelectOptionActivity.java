package com.chartiq.chartiqsample.studies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chartiq.chartiqsample.R;

import java.util.HashMap;

public class StudySelectOptionActivity extends AppCompatActivity {

    ListView listView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_chart_style_choose);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.listview);

        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        if (getIntent().hasExtra("parameter")) {
            StudyParameter parameter = (StudyParameter) getIntent().getSerializableExtra("parameter");
            if (parameter.options != null) {
                for (HashMap.Entry<String, Object> entry : parameter.options.entrySet()) {
                    adapter.add(String.valueOf(entry.getValue()));
                }
            }
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent result = new Intent();
                String positionValue = String.valueOf(adapter.getItem(position));
                StudyParameter parameter = null;
                if (getIntent().hasExtra("parameter")) {
                    parameter = (StudyParameter) getIntent().getSerializableExtra("parameter");
                    parameter.value = positionValue;
                    // if options exist get the mapped key value as that is the value needed by the study
                    if (parameter.options != null) {
                        for (HashMap.Entry<String, Object> entry : parameter.options.entrySet()) {
                            if(entry.getValue().equals(positionValue)){
                                String key = (String) entry.getKey();
                                parameter.value = key;
                            }
                        }
                    }
                }

                result.putExtra("chosenValue", positionValue);
                result.putExtra("parameter", parameter);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
