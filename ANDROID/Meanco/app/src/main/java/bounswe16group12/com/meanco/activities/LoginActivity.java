package bounswe16group12.com.meanco.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.utils.Connect;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private Tracker mTracker;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AuthenticationTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        mTracker = ((MeancoApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("LOGIN_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        int userId = preferences.getInt("UserId", -1);

        if(userId != -1){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignUpButton = (Button) findViewById(R.id.signup_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               attemptRegister();

            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        final TextView continueWithout = (TextView) findViewById(R.id.continue_without);
        continueWithout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                continueWithout.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            new AuthenticationTask(MeancoApplication.LOGIN_URL,username,password).execute();
        }
    }

    private void attemptRegister(){
        EditText temp = new EditText(LoginActivity.this);
        temp.setHint("Enter an email");
        temp.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        final EditText emailInput = temp;

        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Register")
                .setView(emailInput)
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String userEmail = emailInput.getText().toString();
                        if(userEmail.contains("@")) {
                            showProgress(true);
                            new AuthenticationTask(MeancoApplication.REGISTER_URL, userEmail, username, password).execute();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please enter a valid email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create()
                .show();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        //Blocks  return action of back button to prevent user go back to login page.
    }

    public class AuthenticationTask extends AsyncTask<Void, Void, Connect.APIResult> {
        private String email;
        private String username;
        private String password;
        private String url;

        //LOGIN CONSTRUCTOR
        public AuthenticationTask(String url, String username, String password){
            this.url = url;
            this.username = username;
            this.password = password;
        }

        //REGISTER CONSTRUCTOR
        public AuthenticationTask(String url,String email, String username, String password){
            this.url = url;
            this.email = email;
            this.username = username;
            this.password = password;
        }


        @Override
        protected void onPostExecute(Connect.APIResult response) {
            super.onPostExecute(response);
            showProgress(false);

            try {
                JSONObject jsonObject=new JSONObject(response.getData());
                if (jsonObject != null) {
                    if (response.getResponseCode() == 200) {
                        int userId = jsonObject.getInt("UserId");
                        Functions.clearUserPreferences(getApplicationContext());
                        Functions.setUserId(userId,getApplicationContext());
                        Functions.setUsername(username,getApplicationContext());

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"WRONG CREDENTIALS",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Connect.APIResult doInBackground(Void... voids) {

            String data = null;
            try {
                if(url.equals(MeancoApplication.REGISTER_URL)) {
                    data = URLEncoder.encode("email", "UTF-8")
                            + "=" + URLEncoder.encode(email + "", "UTF-8");
                    data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                            + URLEncoder.encode(username, "UTF-8");

                    data += "&" + URLEncoder.encode("password1", "UTF-8")
                            + "=" + URLEncoder.encode(password, "UTF-8");

                    data += "&" + URLEncoder.encode("password2", "UTF-8")
                            + "=" + URLEncoder.encode(password, "UTF-8");
                }
                else if(url.equals(MeancoApplication.LOGIN_URL)){
                    data = URLEncoder.encode("username", "UTF-8")
                            + "=" + URLEncoder.encode(username + "", "UTF-8");

                    data += "&" + URLEncoder.encode("password", "UTF-8")
                            + "=" + URLEncoder.encode(password, "UTF-8");
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String text = "";
            BufferedReader reader=null;
            URL url            = null;
            try {
                url = new URL(this.url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();


                int responseCode = conn.getResponseCode();

                if(responseCode == 200) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                else
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }
                text = sb.toString();

                return new Connect.APIResult(responseCode,text);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

