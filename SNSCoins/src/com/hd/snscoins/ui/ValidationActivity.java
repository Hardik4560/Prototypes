
package com.hd.snscoins.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@EActivity(R.layout.activity_validation)
@OptionsMenu(R.menu.menu_validation)
public class ValidationActivity extends Activity implements ImageChooserListener {

    //==========================================
    // CONSTANTS
    //==========================================
    public static final String TAG = ValidationActivity.class.getSimpleName();

    protected static final int CROP_FROM_CAMERA = 0x007;

    //==========================================
    // VIEWS
    //==========================================

    @ViewById(R.id.edtName)
    EditText edtName;

    @ViewById(R.id.edtEmail)
    EditText edtEmail;

    @ViewById(R.id.edtMobile)
    EditText edtMobile;

    @ViewById(R.id.grdPhotos)
    GridView grdPhotos;

    //==========================================
    // VARIABLES
    //==========================================
    private SnSCoreSystem mAppContext;
    private Context mActivityContext;

    private PhotoAdapter adapterPhotos;

    PhotoList mPhotoList;
    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String filePath;

    public int mId = 0;

    //==========================================
    // OVERIDE ACTIVITY METHODS
    //==========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mAppContext = ((SnSCoreSystem) getApplicationContext());
        mActivityContext = this;

        mPhotoList = new PhotoList();
    }

    @AfterViews
    protected void init() {

        adapterPhotos = new PhotoAdapter(this);
        setListAdapter(adapterPhotos);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK
            && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }

            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @OptionsItem(R.id.action_save)
    public void onDone() {

    }

    //==========================================
    // CLASS METHODS
    //==========================================
    private void setListAdapter(PhotoAdapter adapter) {
        grdPhotos.setAdapter(adapter);
    }

    protected void showImageChooserDialog() {
        String[] imageCaptureOptions = new String[] { "Gallery", "Camera" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ValidationActivity.this);
        builder.setTitle("Select Image");
        builder.setSingleChoiceItems(imageCaptureOptions, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                switch (whichButton) {
                    case 0:
                        chooseImage();
                        break;
                    case 1:
                        takePicture();
                        break;
                    default:
                        break;
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        builder.create().show();
    }

    protected void showRemoveImageDialog(final Photo photo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ValidationActivity.this);
        builder.setTitle("Remove Image ?");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mPhotoList.remove(photo);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        builder.create().show();
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        pickImage();
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        pickImage();
    }

    private void pickImage() {
        imageChooserManager = new ImageChooserManager(this, chooserType, mActivityContext.getFilesDir().getAbsolutePath(), true);
        Bundle extras = new Bundle();
        extras.putString("crop", "true");

        extras.putInt("aspectX", 0);
        extras.putInt("aspectY", 0);
        extras.putInt("outputX", 400);
        extras.putInt("outputY", 400);

        imageChooserManager.setExtras(extras);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    private class PhotoList extends ArrayList<Photo> {

        private static final int MAX_PHOTOS_ALLOWED = 5;
        Photo photoPlaceHolder;
        boolean isPlaceHolderPresent;

        public PhotoList() {
            photoPlaceHolder = new Photo(true);
            addPlaceHolder(0);
        }

        public void addPlaceHolder(int index) {
            isPlaceHolderPresent = true;
            add(index, photoPlaceHolder);
        }

        public void removePlaceHolder() {
            isPlaceHolderPresent = false;
            remove(photoPlaceHolder);
        }

        @Override
        public void add(int index, Photo object) {
            super.add(index, object);
            onListDataChanged();
        }

        @Override
        public boolean add(Photo object) {
            Log.d(TAG, "Adding photo : " + object.id);
            boolean x = super.add(object);
            onListDataChanged();
            return x;
        }

        private void onListDataChanged() {
            //Check if this the max number of photo added.
            if (size() > MAX_PHOTOS_ALLOWED) {
                removePlaceHolder();
            }
            else if (size() < MAX_PHOTOS_ALLOWED && !isPlaceHolderPresent) {//re-add the place holder again.
                addPlaceHolder(size());
            }

            if (adapterPhotos != null) {
                adapterPhotos.notifyDataSetChanged();
            }
        }

        @Override
        public Photo remove(int index) {
            Photo x = super.remove(index);
            onListDataChanged();
            return x;
        }

        @Override
        public boolean remove(Object object) {
            boolean x = super.remove(object);
            onListDataChanged();
            return x;
        }
    }

    private class Photo {

        int id;
        Uri fullSize;
        Uri thumbnailSmall;
        boolean isPlaceHolder;

        public Photo(boolean placeHolder) {
            id = mId++;
            isPlaceHolder = placeHolder;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Photo other = (Photo) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (id != other.id)
                return false;
            return true;
        }

        private ValidationActivity getOuterType() {
            return ValidationActivity.this;
        }
    }

    //==========================================
    // CLASS ADAPTERS
    //==========================================
    private class PhotoAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageLoader imageLoader;
        DisplayImageOptions options;

        public PhotoAdapter(Context context) {
            super();
            mInflater = LayoutInflater.from(context);

            //Initialize lazy loading api.
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.img_not_available)
                    .showImageForEmptyUri(R.drawable.img_not_available)
                    .showImageOnFail(R.drawable.img_not_available)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public Photo getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public long getItemId(int arg0) {
            return mPhotoList.get(arg0).id;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.element_photo_frame, null);

                viewHolder = new ViewHolder();
                viewHolder.photo = (ImageView) convertView.findViewById(R.id.imgPhoto);
                viewHolder.imgRemove = (ImageView) convertView.findViewById(R.id.imgRemove);
                viewHolder.txtAddPhoto = (TextView) convertView.findViewById(R.id.txtAddPhoto);

                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //reset the views;
            convertView.setOnClickListener(null);
            viewHolder.imgRemove.setVisibility(View.GONE);
            viewHolder.txtAddPhoto.setVisibility(View.GONE);

            final Photo photo = getItem(position);

            //It's a place holder !!!
            if (photo.isPlaceHolder) {
                viewHolder.txtAddPhoto.setVisibility(View.VISIBLE);

                //Set the image on the imageView
                viewHolder.photo.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_gallery));

                //Set the onclick listener
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showImageChooserDialog();
                    }
                });
            }
            else {
                viewHolder.imgRemove.setVisibility(View.VISIBLE);
                viewHolder.imgRemove.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showRemoveImageDialog(photo);
                    }
                });

                //Set the image on the imageView
                viewHolder.photo.setImageURI(photo.thumbnailSmall);

                //Set the onclick listener
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Show the photo in a bigger view.
                    }
                });
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView photo, imgRemove;
            TextView txtAddPhoto;
        }
    }

    //==========================================
    // METHODS OVERRIDED FOR IMAGE CHOOSER
    //==========================================
    @UiThread
    @Override
    public void onError(String reason) {
        ToastMaker.getInstance().createToast(reason);
    }

    @UiThread
    @Override
    public void onImageChosen(ChosenImage image) {
        if (image != null) {
            Photo photo = new Photo(false);
            photo.fullSize = Uri.parse(new File(image.getFilePathOriginal()).toString());
            photo.thumbnailSmall = Uri.parse(new File(image.getFileThumbnailSmall()).toString());

            mPhotoList.add(mPhotoList.size() - 1, photo);
        }
    }
}
