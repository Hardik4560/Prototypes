
package com.hd.snscoins.webentities;

import java.util.List;

public class WeProduct {
    Long product_id;
    Long sub_category_id;
    String product_title;
    List<WeYear> product_mint;
    String product_image;

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public List<WeYear> getProduct_mint() {
        return product_mint;
    }

    public void setProduct_mint(List<WeYear> product_mint) {
        this.product_mint = product_mint;
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
        String year;

        List<WeMint> mint_title;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public List<WeMint> getMint_title() {
            return mint_title;
        }

        public void setMint_title(List<WeMint> mint_title) {
            this.mint_title = mint_title;
        }

    }

    public class WeMint {
        String title;
        Integer is_rare;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getIs_rare() {
            return is_rare;
        }

        public void setIs_rare(Integer is_rare) {
            this.is_rare = is_rare;
        }
    }

}
