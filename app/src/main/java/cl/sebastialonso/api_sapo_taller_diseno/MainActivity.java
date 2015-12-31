package cl.sebastialonso.api_sapo_taller_diseno;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    private TextView mInfo;
    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        final Intent moveToMainContent = new Intent(MainActivity.this, MainContentActivity.class);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        final Context context = getApplicationContext();

        //Revisar que estemos logeados primero
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("isLoggedIn", null) != null) {
            startActivity(moveToMainContent);
        } else {

            setContentView(R.layout.activity_main);
            mInfo = (TextView) findViewById(R.id.info);
            mLoginButton = (LoginButton) findViewById(R.id.login_button);
            //Entrar con Facebook
            mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.w(TAG, "\n\n"+loginResult.toString());
                    Log.w(TAG, "\n\n"+loginResult.getAccessToken().toString());

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    //Parsing parameters
                    JSONObject params = new JSONObject();
                    JSONObject user = new JSONObject();

                    Log.e(TAG, "\n" + Constants.URL_LOGIN + "\n");
                    try {
                        user.put("token",loginResult.getAccessToken().getToken());
                        user.put("email", "sebagonz91@gmail.com");
                        user.put("face_id", loginResult.getAccessToken().getUserId());
                        params.put("user", user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Comunicar con la API
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                            Constants.URL_LOGIN,
                            params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //Guardar data importante
                                    SharedPreferences.Editor edit = prefs.edit();
                                    edit.putString("isLoggedIn", "true");
                                    try {
                                        edit.putString("userId", response.getString("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    edit.commit();

                                    //A MainContent Activity
                                    startActivity(moveToMainContent);
                            }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.w("TAG", "\n" + error.getMessage() + "\n");
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        public String getBodyContentType(){
                            return "application/json";
                        }
                    };

                    Log.w("TAG", "\nse agregara a la queue\n");
                    queue.add(jsObjRequest);
                    Log.w("TAG", "\nllamada se hace\n");
                    //mInfo.setText("User ID: " + loginResult.getAccessToken().getUserId() +
                    //       "\n Token: " + loginResult.getAccessToken().getToken());

                }

                @Override
                public void onCancel() {
                    mInfo.setText("Login attempt canceled.");
                }

                @Override
                public void onError(FacebookException error) {
                    mInfo.setText("Login attempt failed.");
                }

            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
