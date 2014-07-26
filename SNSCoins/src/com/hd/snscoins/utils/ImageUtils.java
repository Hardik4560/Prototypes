
package com.hd.snscoins.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

public class ImageUtils {
    
    public static String saveToInternalSorage(Context context, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "" + System.currentTimeMillis());

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }
}
