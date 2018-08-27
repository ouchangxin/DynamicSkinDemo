package com.xinshen.dynamtheme;

import android.app.Application;

/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:01
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
