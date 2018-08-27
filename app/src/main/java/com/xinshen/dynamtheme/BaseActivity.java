package com.xinshen.dynamtheme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * 描述:
 * DynamTheme-
 * @Author thinkpad
 * @create 2018-08-25 13:02
 */
public abstract class BaseActivity extends AppCompatActivity implements SkinManager
        .SkinUpdateListener {

    private MySkinFactory mSkinFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinFactory = new MySkinFactory();
        getLayoutInflater().setFactory(mSkinFactory);
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().addSkinUpdateListener(this);
    }

    @Override
    public void onSkinUpdate() {
        mSkinFactory.apply();
    }

}
