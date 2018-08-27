package com.xinshen.dynamtheme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * DynamTheme-
 * @Author thinkpad
 * @create 2018-08-25 13:04
 */
public class SkinManager {

    private static final SkinManager mInstance = new SkinManager();
    private Resources mSkinResources;
    private Context context;
    private String skinPackageName;
    private boolean isExternalSkin;
    private List<SkinUpdateListener> mListeners = new ArrayList<>();
    private final String KEY = "skin_path";

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        return mInstance;
    }

    private void judge() {
        if (context == null) {
            throw new IllegalStateException("context is null");
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LoadTask extends AsyncTask<String, Void, Resources> {

        @Override
        protected Resources doInBackground(String... paths) {
            try {
                if (paths.length == 1) {
                    String skinPkgPath = paths[0];
                    File file = new File(skinPkgPath);
                    if (!file.exists()) {
                        return null;
                    }
                    PackageManager mPm = context.getPackageManager();
                    PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager
                            .GET_ACTIVITIES);
                    skinPackageName = mInfo.packageName;
                    AssetManager assetManager = AssetManager.class.newInstance();
                    Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",
                            String.class);
                    addAssetPath.invoke(assetManager, skinPkgPath);
                    Resources superRes = context.getResources();
                    Resources skinResource = new Resources(assetManager, superRes
                            .getDisplayMetrics(), superRes.getConfiguration());
                    saveSkinPath(skinPkgPath);

                    return skinResource;
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Resources resources) {
            super.onPostExecute(resources);
            mSkinResources = resources;
            if (mSkinResources != null) {
                isExternalSkin = true;
                notifySkinUpdate();
            }
        }
    }

    public interface SkinUpdateListener {
        void onSkinUpdate();
    }

    private void notifySkinUpdate() {
        for (SkinUpdateListener listener : mListeners) {
            listener.onSkinUpdate();
        }
    }

    /**
     * API
     */
    public void addSkinUpdateListener(SkinUpdateListener listener) {
        if (listener == null)
            return;
        judge();
        mListeners.add(listener);
    }

    public String getSkinPath() {
        judge();
        String skinPath = (String) SPUtil.get(context, KEY, "");
        return TextUtils.isEmpty(skinPath) ? null : skinPath;
    }

    public void saveSkinPath(String path) {
        judge();
        SPUtil.put(context, KEY, path);
    }

    public boolean isExternalSkin() {
        judge();
        return isExternalSkin;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        String skinPath = (String) SPUtil.get(context, KEY, "");
        isExternalSkin = !TextUtils.isEmpty(skinPath);
        loadSkin(getSkinPath());
    }

    public void loadSkin(String path) {
        judge();
        if (path == null)
            return;
        new LoadTask().execute(path);
    }

    public int getColor(String resName,int resId) {
        judge();
        int originColor = context.getResources().getColor(resId);
        if(mSkinResources == null || !isExternalSkin){
            return originColor;
        }
        int newResId = mSkinResources.getIdentifier(resName, "color", skinPackageName);
        int newColor;
        try{
            newColor = mSkinResources.getColor(newResId);
        }catch(Resources.NotFoundException e){
            e.printStackTrace();
            return originColor;
        }
        return newColor;
    }

    public Drawable getDrawable(String resName,int resId){
        judge();
        Drawable originDrawable = context.getResources().getDrawable(resId);
        if(mSkinResources == null || !isExternalSkin){
            return originDrawable;
        }
        int newResId = mSkinResources.getIdentifier(resName, "drawable", skinPackageName);
        Drawable newDrawable;
        try{
            if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                newDrawable = mSkinResources.getDrawable(newResId);
            }else{
                newDrawable = mSkinResources.getDrawable(newResId, null);
            }
        }catch(Resources.NotFoundException e){
            e.printStackTrace();
            return originDrawable;
        }
        return newDrawable;
    }

    public void restoreDefaultTheme(){
        judge();
        SPUtil.put(context, KEY, "");
        isExternalSkin= false;
        mSkinResources = null;
        notifySkinUpdate();
    }

}
