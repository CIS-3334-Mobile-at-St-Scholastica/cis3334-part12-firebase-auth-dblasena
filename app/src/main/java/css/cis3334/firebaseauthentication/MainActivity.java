package css.cis3334.firebaseauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Main class for this app. It is ran on start of the app
 */
public class MainActivity extends AppCompatActivity {

    private TextView textViewStatus;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoogleLogin;
    private Button buttonCreateLogin;
    private Button buttonSignOut;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    /**
     * Oncreate method. This method is used when the app is first started creating all of the objects inside of this method.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoogleLogin = (Button) findViewById(R.id.buttonGoogleLogin);
        buttonCreateLogin = (Button) findViewById(R.id.buttonCreateLogin);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "normal login ");
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        buttonCreateLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Create Account ");
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Google login ");
                googleSignIn();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Logging out - signOut ");
                signOut();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("CIS3334", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("CIS3334", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    /**
     * This method will create the authentication listener on the start of the app. This listener will be sued for the different sign in attempts
     */
    @Override
    public void onStart() {
        super.onStart(); // Creates the listner object
        mAuth.addAuthStateListener(mAuthListener); // Adds a state listener to the object
    }

    /**
     * This method will remove any authentication listeners when the app is stopped or the connection to these log in services is interupted.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) { // checks to see if there is a state listnerer and if there is then it will be removed
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     *  Uses teh previously created authentication listeners to create the account with the entered email and password from the given text fields.
     * @param email this string will have the email
     * @param password this string will have the password.
     */
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password) // Creates a user with the given email and password and adds them to the database
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

       @Override
       public void onComplete(@NonNull Task<AuthResult> task) {
            // Adds to the log file of any created acounts
           //Log.d("CIS3334", "createUserWithEmail:onComplete:" + task.isSuccessful());

           // If sign in fails, display a message to the user. If sign in succeeds
           // the auth state listener will be notified and logic to handle the
           // signed in user can be handled in the listener.
           if (!task.isSuccessful()) {
              textViewStatus.setText("Authentication failed");

               //Toast.makeText(MainActivity.this, "Authentication failed.",
                                   // Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /**
     * This method will check the email and password string combination to any pre existing entries in the database and if there is a match then the user will be given access.
     * @param email this string will have the email
     * @param password this string will have the password
     */
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password) //Checks to see if the given email and password are in the databse in order to give that person access.
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Log.d("CIS3334", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                          textViewStatus.setText("Authentication failed");

                           // Log.w("CIS3334", "signInWithEmail", task.getException());
                            //Toast.makeText(MainActivity.this, "Authentication failed.",
                                //    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /**
     * This method uses the authentication listener to sign the current user out from the system.
     */
    private void signOut () {

        mAuth.signOut();// Signs the user out of the system
    }

    private void googleSignIn() {


    }




}
