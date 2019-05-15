package com.example.muhammadashfaq.recieveapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.muhammadashfaq.recieveapp.Adapter.IMEIAdapter;
import com.example.muhammadashfaq.recieveapp.Adapter.PhoneNoAdapter;
import com.example.muhammadashfaq.recieveapp.Constants.BaseUrl;
import com.example.muhammadashfaq.recieveapp.Model.IMEINModel;
import com.example.muhammadashfaq.recieveapp.Model.PhoneNoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class PhoneNoActivity extends AppCompatActivity {
    LinearLayout linearLayoutNetworkStatus;
    TextView txtVuHeadingNetwork, txtVuCountry;
    PhoneNoAdapter imeiAdapter;
    ProgressDialog progressDialog;

    ArrayList<PhoneNoModel> listJson;

    RecyclerView recyclerViewIMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numbers);

        recyclerViewIMEI=findViewById(R.id.recyler_view);
        linearLayoutNetworkStatus=findViewById(R.id.linearNetworkStatus);
        txtVuHeadingNetwork=findViewById(R.id.tv_heading);
        txtVuCountry=findViewById(R.id.tv_country);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait for a litte while");
        progressDialog.setCancelable(false);



        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewIMEI.setLayoutManager(linearLayoutManager) ;

        registerForContextMenu(recyclerViewIMEI);
        startThread();
    }

    private void startThread() {
        progressDialog.show();
        Thread mThread= new Thread(){
            public void run(){
                super.run();
                try {
                    Thread.sleep(1000);
                    trimCache(PhoneNoActivity.this);
                    String url = BaseUrl.baseUrl + getResources().getString(R.string.get_phone_numbers);
                    StringRequest mStringRequest = new StringRequest(1, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.i("Response",response);
                            try {
                                listJson=new ArrayList<PhoneNoModel>();
                                JSONObject jsonObject=new JSONObject(response);
                                JSONArray array=jsonObject.getJSONArray("result");
                                if(array.length()==0){
                                    Toast.makeText(PhoneNoActivity.this, "Please First Register any Device", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        PhoneNoModel imeinModel = new PhoneNoModel();
                                        imeinModel.setPhone_no(object.getString("phone"));
                                        listJson.add(imeinModel);
                                    }

                                    imeiAdapter= new PhoneNoAdapter(PhoneNoActivity.this,listJson,R.layout.recyler_item_design_phone_no);
                                    recyclerViewIMEI.setLayoutManager(new LinearLayoutManager(PhoneNoActivity.this));

                                    recyclerViewIMEI.setAdapter(imeiAdapter);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.dismiss();
                            Log.i("Error",error.toString());
                            Toast.makeText(PhoneNoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {


                            return null;

                        }
                    };

                    RequestQueue mRequestQueue = Volley.newRequestQueue(PhoneNoActivity.this);
                    mRequestQueue.add( mStringRequest);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

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
