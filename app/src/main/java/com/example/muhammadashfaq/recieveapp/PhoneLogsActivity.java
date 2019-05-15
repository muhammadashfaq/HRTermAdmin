package com.example.muhammadashfaq.recieveapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.muhammadashfaq.recieveapp.Adapter.PhoneNoAdapter;
import com.example.muhammadashfaq.recieveapp.Constants.BaseUrl;
import com.example.muhammadashfaq.recieveapp.Model.PhoneNoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhoneLogsActivity extends AppCompatActivity {

    LinearLayout linearLayoutButton,linearLayoutAddUrl;

    Button btnSaveUrlToDB;
    EditText editText;
    ProgressDialog progressDialog;
    String txtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_logs);

        linearLayoutAddUrl = findViewById(R.id.linear_Layout_add_url);
        linearLayoutButton = findViewById(R.id.linear_Layout_Buttons);
        btnSaveUrlToDB=findViewById(R.id.bt_save_url);
        editText=findViewById(R.id.edt_txt_add_url);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait for a litte while");
        progressDialog.setCancelable(false);

        btnSaveUrlToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUrlToDB();
            }
        });

    }

    private void saveUrlToDB() {
        txtUrl = editText.getText().toString().trim();

        if(!txtUrl.isEmpty()){
            trimCache(this);
            progressDialog.show();
            Thread mThread= new Thread(){
                public void run(){
                    super.run();
                    try {
                        Thread.sleep(1000);
                        trimCache(PhoneLogsActivity.this);
                        final String url = BaseUrl.baseUrl + getResources().getString(R.string.save_website_url);
                        StringRequest mStringRequest = new StringRequest(1, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("true")){
                                    progressDialog.dismiss();
                                    Toast.makeText(PhoneLogsActivity.this, "Website Url Saved.", Toast.LENGTH_SHORT).show();
                                    linearLayoutAddUrl.setVisibility(View.GONE);
                                    linearLayoutButton.setVisibility(View.VISIBLE);
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(PhoneLogsActivity.this, "Oops Something went wrong. Try again please.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.i("Error",error.toString());
                                Toast.makeText(PhoneLogsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                HashMap<String,String> params = new HashMap<>();

                                params.put("website_url",txtUrl);

                                return params;

                            }
                        };

                        RequestQueue mRequestQueue = Volley.newRequestQueue(PhoneLogsActivity.this);
                        mRequestQueue.add( mStringRequest);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();
        }else{
            editText.setError("Please enter any website url to save");
            editText.requestFocus();
        }
    }

    public void btnSeeLogs(View view) {
        startActivity(new Intent(this,IMEIActivity.class));
    }

    public void btnSeePhones(View view) {
        startActivity(new Intent(this,PhoneNoActivity.class));
    }

    public void btnAddWebsiteUrl(View view) {
        linearLayoutButton.setVisibility(View.GONE);
        linearLayoutAddUrl.setVisibility(View.VISIBLE);
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {

        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

// The directory is now empty so delete it
        return dir.delete();
    }
}
