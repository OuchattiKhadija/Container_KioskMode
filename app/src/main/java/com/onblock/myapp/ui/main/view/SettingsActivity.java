package com.onblock.myapp.ui.main.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.onblock.myapp.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    public static View mHomeScreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        mHomeScreenImage = findViewById(R.id.main_act);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public class SettingsFragment extends PreferenceFragmentCompat {

        int REQUEST_CODE_IMAGE = 1;
        Uri imageUri;

        String PREFS_NAME = "PrefsHo";



        int SELECT_PICTURE = 200;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            Preference button = getPreferenceManager().findPreference("browsef");
            if (button != null) {
                button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        imageChooser();
                        return true;
                    }
                });
            }


        }

        // this function is triggered when
        // the Select Image Button is clicked
        void imageChooser() {

            // create an instance of the
            // intent of the type image
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            // pass the constant to compare it
            // with the returned requestCode
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        }

        // this function is triggered when user
        // selects the image from the imageChooser
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {

                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (requestCode == SELECT_PICTURE) {
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {

                        // update the preview image in the layout
                        mHomeScreenImage.setBackground(uriToDrawable(selectedImageUri));
                    }
                }
            }
        }

        public Drawable uriToDrawable(Uri uri) {
            Drawable bckg;

            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bckg = Drawable.createFromStream(inputStream, uri.toString());
            return bckg;
        }
    }
}