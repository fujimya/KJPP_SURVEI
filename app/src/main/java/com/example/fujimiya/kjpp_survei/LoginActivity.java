package com.example.fujimiya.kjpp_survei;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText txt_username,txt_password;
    String hasil,username,id_user,hak_akses,nama_user,msg,success = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);

    }


    private void addEmployee(final String username_get,final String password_get){



        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                hasil = ""+s;
                showEmployee(s);
//                Toast.makeText(LoginActivity.this,""+s, Toast.LENGTH_LONG).show();

//                Intent kelogin = new Intent(BalitaTambahActivity.this, LoginActivity.class);
//                startActivity(kelogin);
//                finish();




            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("username",username_get);
                params.put("password",password_get);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://45.77.40.141//kjpp/api/post_login.php/", params);
                //Toast.makeText(LoginActivity.this,""+res, Toast.LENGTH_LONG).show();
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();

    }

    public void masuk(View view) {
        addEmployee(txt_username.getText().toString(),txt_password.getText().toString());

    }
    private void showEmployee(String kuy){

        JSONObject jsonObject = null;
//        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(kuy);
//            JSONArray result = jsonObject.getJSONArray("balita");
//            Toast.makeText(LoginActivity.this,"Get Json : "+jsonObject.get("username"),Toast.LENGTH_LONG).show();

            username = ""+jsonObject.get("username");
            id_user = "" +jsonObject.get("id_user");
            hak_akses = ""+jsonObject.get("hak_akses");
            nama_user = ""+ jsonObject.get("nama_user");
            msg = ""+jsonObject.get("msg");
            success = ""+jsonObject.get("success");

            if(hak_akses.equals("SURVEYOR") && msg.equals("ok")&&success.equals("1")){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("id_user", id_user);
                ed.putString("nama_user", nama_user);
                ed.putString("username", username);
                ed.commit();

                finish();
            }

//            for(int i = 0; i<result.length(); i++){
//                JSONObject jo = result.getJSONObject(i);
////                nama = jo.getString("nama_user");
//                //String jabatan = jo.getString("jabatan");
//                //Toast.makeText(Login.this,"Get Json : "+nama,Toast.LENGTH_LONG).show();
////                ID.add(""+jo.getString("id_balita"));
////                NamaB.add(""+jo.getString("nama_balita"));
////                TempatB.add(""+jo.getString("tempat_lahir"));
////                TanggalB.add(""+jo.getString("tanggal_lahir"));
////                JK.add(""+jo.getString("jenis_kelamin"));
//
//
//            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
