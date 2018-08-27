package com.xinshen.dynamtheme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * DynamTheme-
 * @Author thinkpad
 * @create 2018-08-25 13:02
 */
public class MySkinFactory implements LayoutInflater.Factory {

    private List<SkinItem> skinItems = new ArrayList<>();

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = createView(name,context,attrs);
        if (view!=null){
            collectViewAttr(view,context,attrs);
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            if (-1 == name.indexOf('.')){	//不带".",说明是系统的View
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            }else {	//带".",说明是自定义的View
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }
        } catch (Exception e) {
            view = null;
        }
        return view;
    }

    private void collectViewAttr(View view,Context context, AttributeSet attrs) {
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int attCount = attrs.getAttributeCount();
        for (int i = 0;i<attCount;++i){
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            if (isSupportedAttr(attributeName)){
                if (attributeValue.startsWith("@")){
                    int resId = Integer.parseInt(attributeValue.substring(1));
                    String resName = context.getResources().getResourceEntryName(resId);
                    String attrType = context.getResources().getResourceTypeName(resId);
                    skinAttrs.add(new SkinAttr(attributeName,attrType,resName,resId));
                    SkinItem skinItem = new SkinItem(view, skinAttrs);
                    if (SkinManager.getInstance().isExternalSkin()){
                        skinItem.apply();
                    }
                    skinItems.add(skinItem);
                }
            }
        }
    }

    private boolean isSupportedAttr(String attributeName){
        return "background".equals(attributeName) || "textColor".equals(attributeName);
    }

    public void apply(){
        for (SkinItem item : skinItems) {
            item.apply();
        }
    }

}
