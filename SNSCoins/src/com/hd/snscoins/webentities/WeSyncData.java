
package com.hd.snscoins.webentities;

import java.util.List;

public class WeSyncData {
    List<WeCategory> category;
    List<WeSubCategory> sub_category;
    List<WeProduct> products;

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

    public List<WeProduct> getProducts() {
        return products;
    }

    public void setProducts(List<WeProduct> products) {
        this.products = products;
    }

}
