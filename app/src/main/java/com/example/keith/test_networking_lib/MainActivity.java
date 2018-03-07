package com.example.keith.test_networking_lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.ConnectivityCheck;

public class MainActivity extends AppCompatActivity {

    AppCompatImageView iv;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lets check on connectivity
        ConnectivityCheck mc = new ConnectivityCheck(this);
        boolean b = mc.isNetworkReachable();
        if (!b)
            Toast.makeText(this,"Your network is unreachable", Toast.LENGTH_SHORT).show();

        b= mc.isWifiReachable();
        if (!b)
            Toast.makeText(this,"Your wifi is unreachable", Toast.LENGTH_SHORT).show();


        //lets get an image
        WebImageView_KP mv = (WebImageView_KP)findViewById(R.id.imageView);
        mv.setImageUrl("http://www.pcs.cnu.edu/~kperkins/pets/p0.png");

        //lets get json
        tv= (TextView)findViewById(R.id.textView);
        DownloadTask_KP dt = new DownloadTask_KP(this);
        dt.execute("http://www.pcs.cnu.edu/~kperkins/pets/pets.json");
    }

    protected void processJSON(String result){
        tv.setText(result);
    }
}
