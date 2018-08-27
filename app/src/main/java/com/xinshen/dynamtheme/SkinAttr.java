package com.xinshen.dynamtheme;

/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:05
 */
public class SkinAttr {

    private String attrName;    //属性名（例如：background、textColor）

    private String attrType;    //属性类型（例如：drawable、color）

    private int resId;       //资源id（例如：123）

    private String resName;     //资源名称（例如：ic_bg）

    public SkinAttr(String attrName, String attrType, String resName,int resId) {
        this.attrName = attrName;
        this.attrType = attrType;
        this.resId = resId;
        this.resName = resName;
    }

    /**
     * API
     * @return
     */
    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }
}
