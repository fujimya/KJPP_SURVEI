package com.example.fujimiya.kjpp_survei;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SurveActivity extends AppCompatActivity {
    private ArrayList<String> NoOrder =  new ArrayList<String>();
    private ArrayList<String> NamaPerusahaan = new ArrayList<String>();
    private ArrayList<String> Status = new ArrayList<String>();
    private ArrayList<String> Status_text = new ArrayList<String>();
    private ArrayList<String> ID_Survey  = new ArrayList<String>();
    private ArrayList<String> Status_Tanggal = new ArrayList<String>();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ActionBar actionBar;

    private String JSON_STRING;

    private ArrayList<String> NoOrderCari =  new ArrayList<String>();
    private ArrayList<String> NamaPerusahaanCari = new ArrayList<String>();
    private ArrayList<String> StatusCari = new ArrayList<String>();
    private ArrayList<String> Status_textCari = new ArrayList<String>();
    private ArrayList<String> ID_SurveyCari  = new ArrayList<String>();
    private ArrayList<String> Status_TanggalCari = new ArrayList<String>();

    String urll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surve);

        SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        String baca = sp.getString("id_user", "");
        urll = "http://45.77.40.141/kjpp/api/survey/get_survey.php?id_surveyor="+baca;
//        Toast.makeText(SurveActivity.this,""+urll,Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        actionBar = getSupportActionBar();
        actionBar.setTitle("DATA ORDER");

        rvView = findViewById(R.id.rv_main_barang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(SurveActivity.this, 1));

        final SearchView searchView = findViewById(R.id.svCari);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(!newText.isEmpty()) {
                    if (!ID_SurveyCari.isEmpty()) {
                        ID_SurveyCari.clear();
                    }

                    if (!NoOrderCari.isEmpty()) {
                        NoOrderCari.clear();
                    }

                    if (!NamaPerusahaanCari.isEmpty()) {
                        NamaPerusahaanCari.clear();
                    }

                    if (!StatusCari.isEmpty()) {
                        StatusCari.clear();
                    }
                    if (!Status_textCari.isEmpty()) {
                        Status_textCari.clear();
                    }
                    if (!Status_TanggalCari.isEmpty()) {
                        Status_TanggalCari.clear();
                    }

                    for(int a =0; a < ID_Survey.size(); a++){
                        if(NamaPerusahaan.get(a).toLowerCase().contains(newText.toLowerCase())) {
                            ID_SurveyCari.add(ID_Survey.get(a));
                            NoOrderCari.add(NoOrder.get(a));
                            NamaPerusahaanCari.add(NamaPerusahaan.get(a));
                            StatusCari.add(Status.get(a));
                            Status_textCari.add(Status_text.get(a));
                            Status_TanggalCari.add(Status_Tanggal.get(a));

                        }
                    }
                    adapter = new AdapterSurvei(SurveActivity.this,NoOrderCari,NamaPerusahaanCari,ID_SurveyCari,StatusCari,Status_textCari,Status_TanggalCari);
                    rvView.setAdapter(adapter);
                }else {
                    adapter = new AdapterSurvei(SurveActivity.this,NoOrder,NamaPerusahaan,ID_Survey,Status,Status_text,Status_Tanggal);
                    rvView.setAdapter(adapter);
                }
                return false;
            }
        });


//        for (int a=0;a < 10; a++) {
//
//            ID_Survey.add(""+a);
//            NoOrder.add("123456");
//            NamaPerusahaan.add("Aaaa");
//            Status.add("0");
//            Status_text.add("qwert");
//        }

        getJSON();
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJSON();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getJSON(){

        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SurveActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //Toast.makeText(LoginActivity.this,""+s,Toast.LENGTH_SHORT).show();
                showEmployee();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();


                String s = rh.sendGetRequest(""+urll);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showEmployee(){

        if(!ID_Survey.isEmpty()){
            ID_Survey.clear();
        }

        if(!NoOrder.isEmpty()){
            NoOrder.clear();
        }

        if(!NamaPerusahaan.isEmpty()){
            NamaPerusahaan.clear();
        }

        if(!Status.isEmpty()){
            Status.clear();
        }
        if(!Status_text.isEmpty()){
            Status_text.clear();
        }

        if(!Status_Tanggal.isEmpty()){
            Status_Tanggal.clear();
        }


        JSONObject jsonObject = null;
//        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("survey");
            //Toast.makeText(Login.this,"Get Json : "+JSON_STRING,Toast.LENGTH_LONG).show();
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                ID_Survey.add(""+jo.getString("id_survey"));
                NoOrder.add(""+jo.getString("no_order"));
                NamaPerusahaan.add(""+jo.getString("nama_pt"));
                Status.add(""+jo.getString("status_terima"));
                Status_text.add(""+jo.getString("status_terima_text"));
                Status_Tanggal.add(""+jo.getString("status_tanggal"));
                //String jabatan = jo.getString("jabatan");
                //Toast.makeText(Login.this,"Get Json : "+nama,Toast.LENGTH_LONG).show();

            }
            adapter = new AdapterSurvei(SurveActivity.this,NoOrder,NamaPerusahaan,ID_Survey,Status,Status_text,Status_Tanggal);
            rvView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
