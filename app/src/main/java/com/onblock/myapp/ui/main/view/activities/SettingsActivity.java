package com.onblock.myapp.ui.main.view.activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsActivity extends AppCompatActivity {
    public static String TAG = "com.onblock.myapp.ui.main.view.activities.SettingsActivity";
    private static final int PICK_IMAGE = 100;

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sharedPreferences = getApplicationContext().getSharedPreferences("PREFS_SCREEN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        Uri imageUri;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference screenImg = (Preference) findPreference("browsef");
            screenImg.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    // open browser or intent here
                    openGallery();
                    return true;
                }
            });

            Preference sizeScreen = (Preference) findPreference("sizeScreen");
            sizeScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    // open browser or intent here
                    //  sizeScreen.setLayoutResource(R.layout.dialog_picker_number);
                    openDialog();
                    return true;
                }
            });

            SeekBarPreference seekBar = (SeekBarPreference) findPreference("textSize");
            seekBar.setMax(34);
            seekBar.setMin(10);
            seekBar.setDefaultValue(14);
            if (seekBar != null) {
                seekBar.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (newValue instanceof Integer) {
                            Integer newValueInt;
                            try {
                                newValueInt = (Integer) newValue;
                            } catch (NumberFormatException nfe) {
                                Log.e(TAG,
                                        "SeekBarPreference is a Integer, but it caused a NumberFormatException");
                                Toast.makeText(getActivity(),
                                        "SeekBarPreference is a Integer, but it caused a NumberFormatException",
                                        Toast.LENGTH_SHORT).show();

                                return false;
                            }
                            // Do something with the value
                            Toast.makeText(getActivity(), "Size " + newValue, Toast.LENGTH_SHORT).show();

                            return true;
                        } else {
                            String objType = newValue.getClass().getName();
                            Log.e(TAG, "SeekBarPreference is not a Integer, it is " + objType);
                            Toast.makeText(getActivity(),
                                    " SeekBarPreference is not a Integer, it is " + objType,
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                });
            }
            Preference resetConf = (Preference) findPreference("resetConf");
            resetConf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    // open browser or intent here
                    editor.clear();
                    editor.commit(); // commit changes
                    Toast.makeText(getActivity(),
                            " Clear conf ",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        private void openGallery() {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                imageUri = data.getData();
                Toast.makeText(getActivity(), "image selected " + imageUri, Toast.LENGTH_SHORT).show();
                // uriToDrawable(imageUri);
                Uri imgUri = saveLocal(imageUri);
                editor.putString("imgBackground", String.valueOf(imgUri));
                editor.commit();
                Toast.makeText(getActivity(), "image selected " + imgUri, Toast.LENGTH_SHORT).show();

                //insertPathInDb(imageUri);
            }
        }

        public Drawable uriToDrawable(Uri yourUri) {
            Drawable imDrawable;
            try {
                ContentResolver contentResolver = getActivity().getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(yourUri);
                imDrawable = Drawable.createFromStream(inputStream, yourUri.toString());
            } catch (FileNotFoundException e) {
                imDrawable = getResources().getDrawable(R.drawable.default_image1);
            }

            return imDrawable;
        }

        public Uri saveLocal(Uri uri) {
            // Get the image from drawable resource as drawable object
            Drawable drawable = uriToDrawable(uri);

            // Get the bitmap from drawable object
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                /*
                    ContextWrapper
                        Proxying implementation of Context that simply delegates all of its calls
                        to another Context. Can be subclassed to modify behavior without
                        changing the original Context.
                */
            ContextWrapper wrapper = new ContextWrapper(getActivity().getApplicationContext());


            // Initializing a new file
            // The bellow line return a directory in internal storage
            File file = wrapper.getDir("Images", MODE_PRIVATE);


            // Create a file to save the image
            file = new File(file, "wallpaperCust" + ".jpg");

            try {

                OutputStream stream = null;


                stream = new FileOutputStream(file);


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);


                stream.flush();

                stream.close();

            } catch (IOException e) // Catch the exception
            {
                e.printStackTrace();
            }

            // Parse the gallery image url to uri
            Uri savedImageURI = Uri.parse(file.getAbsolutePath());

            return savedImageURI;

        }


        private String saveToInternalStorage(Uri uri) {
            Bitmap bitmapImage = AppInfoController.drawable2Bitmap(uriToDrawable(uri));
            ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, "profile.jpg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            editor.putString("imagePath", directory.getAbsolutePath());
            editor.commit();
            return directory.getAbsolutePath();
        }

       /* public void insertPathInDb(Uri uri){
            String path = saveToInternalStorage(uri);
            ScreenConfig screenConfig = new ScreenConfig(0,path);
            screenViewModel.insert(screenConfig);

        }*/


        private void openDialog() {
            Dialog dialog;
            dialog = new Dialog(getActivity());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // dialog.setContentView(R.layout.dialog_picker_number);
            View vi = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_picker_number, null, false);
            NumberPicker npCols = vi.findViewById(R.id.colms);
            Button saveChanges = vi.findViewById(R.id.saveChanges);


            npCols.setMaxValue(5);
            npCols.setMinValue(1);

            dialog.setContentView(vi);
            dialog.show();

            saveChanges.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int valueCols = npCols.getValue();

                    editor.putInt("numbCols", valueCols);

                    editor.commit();
                    Toast.makeText(getActivity(), " " + valueCols + " ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}