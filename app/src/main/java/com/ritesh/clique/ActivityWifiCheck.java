package com.ritesh.clique;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by ritesh on 31/8/16.
 */
public class ActivityWifiCheck extends AppCompatActivity {

    @BindView(R.id.btn_wifi_ok)
    Button wifiok;
    @BindView(R.id.pulsator)
    PulsatorLayout mPulsator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_check);
        ButterKnife.bind(this);
        mPulsator.start();

        wifiok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityWifiCheck.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);

                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
    }
}
