package com.ider.launchermovie;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ider.launchermovie.util.FullscreenActivity;
import com.ider.launchermovie.view.SettingLargeView;

import static com.ider.launchermovie.R.id.setting;

public class SettingActivity extends FullscreenActivity implements View.OnClickListener {

    private SettingLargeView screen,sound,language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        screen= (SettingLargeView)findViewById(R.id.setting_large_image21);
        sound= (SettingLargeView)findViewById(R.id.setting_large_image22);
        language= (SettingLargeView)findViewById(R.id.setting_large_image23);
        screen.setOnClickListener(this);
        sound.setOnClickListener(this);
        language.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.setting_large_image21:
                intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.ScreensSettings"));
                startActivity(intent);
                break;
            case R.id.setting_large_image22:
                intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.sound.SoundSetting"));
                startActivity(intent);
                break;
            case R.id.setting_large_image23:
                intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.language.LanguageInputmethod"));
                startActivity(intent);
                break;
        }
    }
}
