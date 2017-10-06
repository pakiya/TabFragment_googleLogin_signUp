package com.maanondev.e2softdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    SignInButton googleSignUp;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN=2;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatButton singUp;
    AppCompatButton logIn;
    EditText username;
    EditText password;
    FirebaseDatabase Database;
    DatabaseReference myRef;
    public  int q=0,uName=0,passW=0;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        singUp = (AppCompatButton) findViewById(R.id.signUp_btn);

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LogIn.this,SingUp.class);
                startActivity(i);
            }
        });

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        logIn = (AppCompatButton) findViewById(R.id.logIn_btn);


        Database= FirebaseDatabase.getInstance();
        myRef=Database.getInstance().getReference().child("DatabaseReg");


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserNameCheck((Map<String,Object>) dataSnapshot.getValue());
                        UserPasswordCheck((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });




        googleSignUp = (SignInButton) findViewById(R.id.googleSignUp);
        mAuth = FirebaseAuth.getInstance();


        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() !=null) {
                    startActivity(new Intent(LogIn.this,MainActivity.class));

                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LogIn.this,"Somthing went worng",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

      public void UserNameCheck(Map<String,Object> DatabaseRef) {
          String passcode = username.getText().toString().trim();
          ArrayList<String> UserName =new ArrayList<>();
          for (Map.Entry<String,Object> entry : DatabaseRef.entrySet()) {
              Map uName= (Map) entry.getValue();
              UserName.add((String) uName.get("UserName"));
          }
          for (int s=0; s <UserName.size();s++) {
              if (passcode.equals(UserName.get(s))) {

                  uName=1;
                  q=s;
              }
          }
      }

      private void UserPasswordCheck(Map<String,Object> DatabaseRef) {
          String passcode1=password.getText().toString().trim();
          ArrayList<String> PassWord=new ArrayList<>();
          for (Map.Entry<String,Object> entry : DatabaseRef.entrySet()) {
              Map passW= (Map) entry.getValue();
              PassWord.add((String) passW.get("Password"));
          }
          if (passcode1.equals(PassWord.get(q))) {
              passW=1;
          } if (uName==1 && passW==1) {

              Intent i=new Intent(this,MainActivity.class);
              startActivity(i);
              Toast.makeText(this,"Wlcome to login",Toast.LENGTH_LONG).show();
          } else {
              Toast.makeText(this,"Worng Login",Toast.LENGTH_LONG).show();
          }
      }




    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

                Toast.makeText(LogIn.this,"Auth went worng",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
