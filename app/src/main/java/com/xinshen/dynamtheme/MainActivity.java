package com.xinshen.dynamtheme;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import java.io.File;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btnDefault;

    private Button btnBlue;

    private String skinPath;    //皮肤包路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDefault = findViewById(R.id.btn_default);
        btnBlue = findViewById(R.id.btn_blue);
        btnDefault.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "blue-skin.skin";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_default:
                SkinManager.getInstance().restoreDefaultTheme();
                break;
            case R.id.btn_blue:
                SkinManager.getInstance().loadSkin(skinPath);
                break;
        }
    }

}
