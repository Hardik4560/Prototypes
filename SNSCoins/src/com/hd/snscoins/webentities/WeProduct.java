
package com.hd.snscoins.webentities;

import java.util.List;

public class WeProduct {
    Long product_id;
    Long sub_category_id;
    String product_title;
    List<WeYear> year;

    public List<WeYear> getYear() {
        return year;
    }

    public void setYear(List<WeYear> year) {
        this.year = year;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(Long sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public class WeYear {
        Long pid;
        String title;

        List<WeMint> mint;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<WeMint> getMint() {
            return mint;
        }

        public void setMint(List<WeMint> mint) {
            this.mint = mint;
        }

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }

    }

    public class WeMint {
        Long pid;
        String title;
        Integer rare;

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer isRare() {
            return rare;
        }

        public void setRare(Integer rare) {
            this.rare = rare;
        }

    }

}
