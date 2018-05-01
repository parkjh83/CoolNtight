package com.coolnight.coolnight;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolnight.coolnight.network.model.OpenWeatherData;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainContract.MainView {
    @BindView(R.id.temperature_info)
    TextView mTemperInfo;
    @BindView(R.id.refresh_button)
    Button mRefreshButton;


    private final int REQUEST_LOCATION = 1318;
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        checkLocation();

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });
    }

    private void checkLocation() {
        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION );
        } else {
            requestWeather();
        }
    }

    public void requestWeather() {
        if (mPresenter != null) {
            mPresenter.refreshWeather(getApplicationContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestWeather();
            } else {
                // TODO : 위치정보 미동의
            }

        }
    }

    @Override
    public void showWeatherInfo(OpenWeatherData weather) {
        String temperature;
        if (weather == null) {
            temperature = getString(R.string.str_loading_fail);
        } else {
            temperature = String.format(getString(R.string.str_now_temperature_info), String.valueOf(weather.main.temp));
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FIREBASE_ANAL_EVENT_NAME_CITY, weather.name);
            bundle.putDouble(Constants.FIREBASE_ANAL_EVENT_NAME_TEMP, weather.main.temp);
            FirebaseAnalytics.getInstance(this).logEvent(Constants.FIREBASE_ANAL_EVENT_NAME_COLLECT, bundle);
        }
        mTemperInfo.setText(temperature);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mPresenter = null;
    }
}
