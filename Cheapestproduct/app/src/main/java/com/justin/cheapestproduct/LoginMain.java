package com.justin.cheapestproduct;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginMain extends AppCompatActivity {
    private SignInButton btnGoogleSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String TAG="GoogleSignLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        btnGoogleSignIn = (SignInButton)findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setSize(SignInButton.SIZE_WIDE);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "------------------ onStart ------------------");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.d(TAG, currentUser.getDisplayName());
        }

//        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "------------------ onActivityResult ------------------");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {

            Log.d(TAG, "------------------ googleSignInSuccess ------------------");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.d(TAG, "----------GoogleSignInAccount try------------");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG+"DisplayName", account.getDisplayName());
//                Log.d(TAG, "----------firebaseAuthWithGoogle try------------");

                //取得使用者並試登入Firebase
                firebaseAuthWithGoogle(account);
                Log.d(TAG, "Google sign in successful");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed", e);
                e.printStackTrace();
                // ...
            }
        }else{
            Log.d(TAG, "------------------ requestCode != 101 ------------------");
        }

    }




    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
//                            Log.d(TAG, user.toString());
//                            Intent intent = new Intent(LoginMain.this,MainActivity.class);
//                            Bundle accountBundle = new Bundle();
//                            accountBundle.putString("loginName",user.getDisplayName());
//                            accountBundle.putString("loginEmail",user.getEmail());
//                            accountBundle.putString("loginPhotoUrl",user.getPhotoUrl().toString());
//                            Log.d(TAG+"Firebase PhotoUrl",user.getPhotoUrl().toString());
//                            Log.d(TAG+"Firebase Name",user.getDisplayName());
//                            Log.d(TAG+"Firebase Email",user.getEmail());
//                            intent.putExtras(accountBundle);
//
//                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginMain.this, "User logged in SuccessFully", Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginMain.this, "Could not  log in User", Toast.LENGTH_SHORT).show();

                        }


                    }
                });


    }


}
