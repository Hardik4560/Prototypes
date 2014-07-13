
package com.hardy.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.hardy.logging.Logger;

public class FileAccess {

    private static String BASE_FOLDER;
    public static final String BRAND_IMAGES_FOLDER = "brands";
    public static final String SKU_IMAGES_FOLDER = "skus";
    public static final String ARTIFACTS_IMAGES_FOLDER = "artifacts";
    private static final String COMPANY_IMAGES_FOLDER = "company_images";
    public static final String BANNER_NAME = "banner.png";
    public static final String BANNER_THANKYOU = "thankyou.png";
    private static final String TAG = FileAccess.class.getSimpleName();

    private Context mContext;

    private static FileAccess fileAccess;

    private FileAccess(Context mContext, String folderName) {
        this.mContext = mContext;
        BASE_FOLDER = folderName;

        createFolderStructure();
    }

    public static void init(Context context, String folderName) {
        fileAccess = new FileAccess(context, folderName);
    }

    public static FileAccess getInstance(Context context) {
        if (fileAccess == null) {
            throw new RuntimeException("Try to access a null object, have you called init() of the class prior calling getInstance() ?");
        }
        return fileAccess;
    }

    public static File getBaseFileStorage() {
        String baseFolderPath = Environment.getExternalStorageDirectory() + File.separator + BASE_FOLDER;
        File baseFolder = new File(baseFolderPath);
        return baseFolder;
    }

    private void createFolderStructure() {
        String baseFolderPath = Environment.getExternalStorageDirectory() + File.separator + BASE_FOLDER;
        File baseFolder = new File(baseFolderPath);

        if (!baseFolder.exists()) {
            baseFolder.mkdir();
        }

        Logger.d(TAG, "Base Folder name = " + baseFolder.getName());
        File imagesFolder = new File(baseFolder.getPath() + File.separator + BRAND_IMAGES_FOLDER);
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        File skuImagesFolder = new File(baseFolder.getPath() + File.separator + SKU_IMAGES_FOLDER);
        if (!skuImagesFolder.exists()) {
            skuImagesFolder.mkdirs();
        }

        File artifactsImagesFolder = new File(baseFolder.getPath() + File.separator + ARTIFACTS_IMAGES_FOLDER);
        if (!artifactsImagesFolder.exists()) {
            artifactsImagesFolder.mkdirs();
        }

        File companyImagesFolder = new File(baseFolder.getPath() + File.separator + COMPANY_IMAGES_FOLDER);
        if (!companyImagesFolder.exists()) {
            companyImagesFolder.mkdirs();
        }
    }

    /**
     * 
     * @return return with a separator e.g., abc/xyz/
     */
    public static String getBrandImageStorageLocation() {
        String path = Environment.getExternalStorageDirectory() + File.separator + BASE_FOLDER + File.separator + BRAND_IMAGES_FOLDER + File.separator;
        return path;
    }

    public static String getSkuImageStorageLocation() {
        String path = Environment.getExternalStorageDirectory() + File.separator + BASE_FOLDER + File.separator + SKU_IMAGES_FOLDER + File.separator;
        return path;
    }

    public static String getArtifactsStorageLocation() {
        String path = Environment.getExternalStorageDirectory() + File.separator + BASE_FOLDER + File.separator + ARTIFACTS_IMAGES_FOLDER + File.separator;
        return path;
    }

    public static String getImageStorageLocation() {
        String path = Environment.getExternalStorageDirectory() + File.separator + BASE_FOLDER + File.separator + BRAND_IMAGES_FOLDER + File.separator;
        return path;
    }

    public static String getCompanyImageStorageLocation() {
        String path = Environment.getExternalStorageDirectory() + File.separator + BASE_FOLDER + File.separator + COMPANY_IMAGES_FOLDER + File.separator;
        return path;
    }

    /**
     * Returns the file extension or an empty string if there is no extension. 
     * This method is a convenience method for obtaining the extension of a url and has undefined results for other Strings.
     * NOTE: it prefixes the "." for.eg., ".png", ".jpg"
     * @param mimeType
     * @return
     * @author Hardik Shah
     */
    public static String getExtensionFromMimeType(String mimeType) {
        String[] exts = mimeType.split("/");
        String ext = "";
        try {
            ext = "." + exts[1];
        }
        catch (Exception e) {
            Logger.d(TAG, "String no in proper format for Mimetype or No mimeType Specified, " + mimeType);
        }
        return ext;
    }
}
