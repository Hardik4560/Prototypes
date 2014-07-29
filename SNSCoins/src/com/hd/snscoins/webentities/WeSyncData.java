
package com.hd.snscoins.webentities;

import java.util.List;

public class WeSyncData {
    List<WeCategory> category;
    List<WeSubCategory> sub_category;
    List<WeProduct> product;
    List<WeProduct> products;
    List<WeEvent> event;
    List<WeNews> news;
    List<WeNewsCategory> news_category;

    public List<WeCategory> getCategory() {
        return category;
    }

    public void setCategory(List<WeCategory> category) {
        this.category = category;
    }

    public List<WeSubCategory> getSub_category() {
        return sub_category;
    }

    public void setSub_category(List<WeSubCategory> sub_category) {
        this.sub_category = sub_category;
    }

    public List<WeProduct> getProduct() {
        return product;
    }

    public void setProduct(List<WeProduct> product) {
        this.product = product;
    }

    public List<WeEvent> getEvent() {
        return event;
    }

    public void setEvent(List<WeEvent> event) {
        this.event = event;
    }

    public List<WeNews> getNews() {
        return news;
    }

    public void setNews(List<WeNews> news) {
        this.news = news;
    }

    public List<WeNewsCategory> getNews_category() {
        return news_category;
    }

    public void setNews_category(List<WeNewsCategory> news_category) {
        this.news_category = news_category;
    }

    public List<WeProduct> getProducts() {
        return products;
    }

    public void setProducts(List<WeProduct> products) {
        this.products = products;
    }
}
