package com.zjut.runner.Model;

/**
 * Created by Phuylai on 2016/10/23.
 */

public class GridViewIconModel {

    private String url;
    private MenuType type;
    private String tag;
    private String enName;
    private String chName;
    private String imageUrl;
    private int imageId;

    public GridViewIconModel(MenuType type, String enName, String chName,int imageId) {
        this.type = type;
        this.enName = enName;
        this.chName = chName;
        this.imageId = imageId;
    }

    public GridViewIconModel(String url, MenuType type,String tag,String enName, String chName,
                             String imageUrl,int imageId) {
        this.url = url;
        this.type = type;
        this.tag = tag;
        this.enName = enName;
        this.chName = chName;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

