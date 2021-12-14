package com.c323FinalProject.colejmetzger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignInActivity extends AppCompatActivity {

    EditText et_name;
    EditText et_email;
    CircleImageView circleImageView;

    //Database Helper for setup
    DatabaseHelper databaseHelper;

    //On activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Get views
        et_name = findViewById(R.id.editTextTextPersonName);
        et_email = findViewById(R.id.editTextTextEmailAddress);
        circleImageView = findViewById(R.id.profile_image_signin);

        //Setup Database
        databasePopulationAndSetup();

        //Setup circleImageView onclick listener
        //Setting up image selector taken from https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelector();
            }
        });

        //Check if user is signed in
        checkSignIn();
    }

    private void databasePopulationAndSetup() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        boolean isDataPopulated = sharedPref.getBoolean("ISDATAPOPULATED", false);
        if (!(isDataPopulated)) {
            databaseHelper = new DatabaseHelper(this);
            databaseHelper.populateDB();
            editor.putBoolean("ISDATAPOPULATED", true);
        }

        editor.apply();
    }

    private void imageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    /** Most of code gotten from https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                    Picasso.get().load(selectedImageUri).resize(circleImageView.getWidth(), circleImageView.getHeight()).centerInside().into(circleImageView);
                    //circleImageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void checkSignIn() {

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean isSignedUp = sharedPref.getBoolean("ISSIGNEDUP", false);
        boolean isLoggedIn = sharedPref.getBoolean("ISLOGGEDIN", false);

        //If user is signed up and also logged in
        if (isSignedUp && isLoggedIn) {
            //Move to next activity if user is already signed up and logged in
            Toast.makeText(this, "Already Logged In. Proceeded to Home", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    public void signInButton(View view) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        boolean isSignedUp = sharedPref.getBoolean("ISSIGNEDUP", false);
        boolean isLoggedIn = sharedPref.getBoolean("ISLOGGEDIN", false);

        //Is user has signed up but not currently logged in
        if (isSignedUp && !(isLoggedIn)) {
            String name = sharedPref.getString("NAME", "");
            String email = sharedPref.getString("EMAIL", "");
            String inputtedName = et_name.getText().toString();
            String inputtedEmail = et_email.getText().toString();

            //Move to next activity if user inputs correct information
            if (name.equals(inputtedName) && email.equals(inputtedEmail)) {
                editor.putBoolean("ISLOGGEDIN", true);
                Toast.makeText(this, "Successful Log In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
            //If user doesn't input correct information
            else {
                Toast.makeText(this, "Inputted info is not correct", Toast.LENGTH_SHORT).show();
            }
        }
        //If user has not even signed up at all
        else if (!(isSignedUp) && !(isLoggedIn)) {
            String newUserName = et_name.getText().toString();
            String newUserEmail = et_email.getText().toString();

            //If user forgets to put in data for one of the fields
            if (newUserName.equals("") || newUserEmail.equals("")) {
                Toast.makeText(this, "Please input data for all fields.", Toast.LENGTH_SHORT).show();
            }
            else {
                editor.putString("NAME", newUserName);
                editor.putString("EMAIL", newUserEmail);
                editor.putBoolean("ISSIGNEDUP", true);
                editor.putBoolean("ISLOGGEDIN", true);
                Toast.makeText(this, "Successful Sign up", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        editor.apply();
    }
}