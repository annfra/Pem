package pg.autyzm.przyjazneemocje;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Ann on 15.10.2016.
 */
public class CameraOptions {

    public static Camera.PictureCallback getCameraPhoto()
    {
        Camera.PictureCallback cameraPhoto = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = getPhoto();
                if (pictureFile == null) {
                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {

                } catch (IOException e) {
                }
            }
        };
        return cameraPhoto;
    }
    //Spinner spinner = (Spinner) Activity.findViewById(R.id.spinner)
    public static Camera getCameraInstance()
    {
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c;
    }
     //tutaj jest definiowany katalog!!
    private static File getPhoto() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" +  ".jpg");//+ spinner.getSelectedItem().toString()

        return mediaFile;
    }

}
