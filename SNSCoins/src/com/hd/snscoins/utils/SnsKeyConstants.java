
package com.hd.snscoins.utils;

public class SnsKeyConstants {

    public static final String EXT_FOLDER_NAME = null;

    public static enum ImageTypes {
        news_image,
        product_image,
        event_image;

        public String getImageUrl(Long id) {
            return "http://demo.iccgnews.com/mobile/get_image.php?id=" + id + "&tname=" + name();
        }
    }
}
