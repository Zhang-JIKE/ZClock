package com.winter4s.zclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.winter4s.zclocklib.CountTimeView;

public class MainActivity extends AppCompatActivity {

    private CountTimeView countTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        countTimeView=findViewById(R.id.countTimeView);
        countTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimeView.start();
            }
        });

        //倒计时控件计时完毕
        countTimeView.setOnFinishedListener(new CountTimeView.OnFinishedListener() {
            @Override
            public void finished() {
                Toast.makeText(getApplicationContext(),"Finished",Toast.LENGTH_SHORT).show();
            }
        });


        //倒计时控件计时时间
        countTimeView.setOnCountListener(new CountTimeView.OnCountListener() {
            @Override
            public void count(int time) {
                //
            }
        });
    }
}
