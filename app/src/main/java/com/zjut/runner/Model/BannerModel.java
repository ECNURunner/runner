package com.zjut.runner.Model;

import java.io.Serializable;

/**
 * Created by Phuylai on 2016/10/23.
 */

public class BannerModel implements Serializable {

    private String imageUrl;
    private String goToUrl;
    private String title;

    public BannerModel(String imageUrl, String goToUrl,String title) {
        this.imageUrl = imageUrl;
        this.goToUrl = goToUrl;
        this.title = title;
    }

    public String getGoToUrl() {
        return goToUrl;
    }

    public void setGoToUrl(String goToUrl) {
        this.goToUrl = goToUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

