package com.example.fujimiya.kjpp_survei;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by fujimiya on 2/9/19.
 */

public class AdapterSurvei extends RecyclerView.Adapter<AdapterSurvei.ViewHolder> {

    private ArrayList<String> NoOrder;
    private ArrayList<String> NamaPerusahaan;
    private ArrayList<String> ID_Survey;
    private ArrayList<String> Status_Terima;
    private ArrayList<String> Status_Terima_Text;
    private ArrayList<String> Status_Tanggal;
    Context context;
    AlertDialog alert;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    String hasil,tglgettt;
    int posisi;

    public AdapterSurvei(Context contxt, ArrayList<String> NoOrderget, ArrayList<String> NamaPerusahaanGet, ArrayList<String> ID_Survey_Get,ArrayList<String> Status_TerimaGet,ArrayList<String> Status_TerimaTextGet,ArrayList<String> Status_Tanggal_Get){
        NoOrder =  NoOrderget;
        NamaPerusahaan =  NamaPerusahaanGet;
        ID_Survey = ID_Survey_Get;
        Status_Terima = Status_TerimaGet;
        Status_Terima_Text = Status_TerimaTextGet;
        Status_Tanggal = Status_Tanggal_Get;
        context = contxt;
    }

    @Override
    public AdapterSurvei.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_order, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterSurvei.ViewHolder vh = new AdapterSurvei.ViewHolder(v);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterSurvei.ViewHolder holder, final int position) {
        holder.txt_NoOrder.setText("No. Order : " + NoOrder.get(position));
        holder.txt_NamaPerusahaan.setText(NamaPerusahaan.get(position));
        holder.txt_StatusPengerjaan.setText(Status_Terima_Text.get(position));
//        holder.txt_TanggalOrder.setText("");
        if(Status_Terima.get(position).equals("0")){
            holder.txt_Status_order.setText("Baca");
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#ffff8800"));
        }else if (Status_Terima.get(position).equals("1")){
            holder.txt_Status_order.setText("On Procces");
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FF99cc00"));
        }else{
            holder.txt_Status_order.setText("Reject");
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FFF72E1A"));
        }
//        Toast.makeText(context,""+Status_Terima.get(position),Toast.LENGTH_SHORT).show();
        posisi = position;

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Status_Terima.get(position).equals("0")) {

                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View promptView = layoutInflater.inflate(R.layout.dialog_konfirm, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(promptView);
                    Button ya = promptView.findViewById(R.id.btn_ya);
                    Button tidak = promptView.findViewById(R.id.btn_tidak);
                    ya.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                            String baca = sp.getString("id_user", "");
                            addEmployee(ID_Survey.get(position), "1", baca, NoOrder.get(position));
                            alert.hide();
                        }
                    });

                    tidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alrtDialogBuilder = new AlertDialog.Builder(context);
                            alrtDialogBuilder.setMessage("Apakah anda yakin ingin menolak?");
                            alrtDialogBuilder.setPositiveButton("YA",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            //Toast.makeText(context,"klik lanjutkan",Toast.LENGTH_LONG).show();
                                            SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                                            String baca = sp.getString("id_user", "");
                                            addEmployee(ID_Survey.get(position), "2", baca, NoOrder.get(position));
                                            alert.hide();

                                        }
                                    });

                            alrtDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });

                            AlertDialog alrtDialog = alrtDialogBuilder.create();
                            alrtDialog.show();
                        }
                    });

                    alert = alertDialogBuilder.create();
                    alert.show();
                }else if(Status_Terima.get(position).equals("1")){
                    if(Status_Tanggal.get(position).equals("1")){
                        Toast.makeText(context,"Tanggal Terima",Toast.LENGTH_SHORT).show();
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View promptView = layoutInflater.inflate(R.layout.dialog_input_data, null);
                        AlertDialog.Builder alertDialogBuildertgl = new AlertDialog.Builder(context);
                        alertDialogBuildertgl.setView(promptView);

                        final EditText txt_tanggal = promptView.findViewById(R.id.txt_tanggal_mulai);
                        final EditText txt_nama = promptView.findViewById(R.id.txt_nama);
                        final EditText txt_nope = promptView.findViewById(R.id.txt_nope);
                        Button simpan = promptView.findViewById(R.id.btn_simpan);

                        txt_tanggal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**
                                 * Calendar untuk mendapatkan tanggal sekarang
                                 */
                                Calendar newCalendar = Calendar.getInstance();

                                /**
                                 * Initiate DatePicker dialog
                                 */
                                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                        /**
                                         * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                                         */

                                        /**
                                         * Set Calendar untuk menampung tanggal yang dipilih
                                         */
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, monthOfYear, dayOfMonth);

                                        /**
                                         * Update TextView dengan tanggal yang kita pilih
                                         */
                                        txt_tanggal.setText(dateFormatter.format(newDate.getTime()));

                                    }

                                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                                /**
                                 * Tampilkan DatePicker dialog
                                 */
                                datePickerDialog.show();

                            }
                        });

                        simpan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                                String baca = sp.getString("id_user", "");
                                simpanTgl(ID_Survey.get(position),NoOrder.get(position),baca,txt_tanggal.getText().toString(),txt_nama.getText().toString(),txt_nope.getText().toString());
                                alert.cancel();
                            }
                        });

                        alert = alertDialogBuildertgl.create();
                        alert.show();

                    }else if(Status_Tanggal.get(position).equals("2")){
                        Toast.makeText(context,"Tanggal Selesai",Toast.LENGTH_SHORT).show();
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View promptView = layoutInflater.inflate(R.layout.dialog_tgl_selesai, null);
                        AlertDialog.Builder alertDialogBuildertgl = new AlertDialog.Builder(context);
                        alertDialogBuildertgl.setView(promptView);

                        final EditText txt_tanggal = promptView.findViewById(R.id.txt_tanggal_mulai);
                        Button simpan = promptView.findViewById(R.id.btn_simpan);

                        txt_tanggal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**
                                 * Calendar untuk mendapatkan tanggal sekarang
                                 */
                                Calendar newCalendar = Calendar.getInstance();

                                /**
                                 * Initiate DatePicker dialog
                                 */
                                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                        /**
                                         * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                                         */

                                        /**
                                         * Set Calendar untuk menampung tanggal yang dipilih
                                         */
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, monthOfYear, dayOfMonth);

                                        /**
                                         * Update TextView dengan tanggal yang kita pilih
                                         */
                                        txt_tanggal.setText(dateFormatter.format(newDate.getTime()));

                                    }

                                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                                /**
                                 * Tampilkan DatePicker dialog
                                 */
                                datePickerDialog.show();

                            }
                        });

                        simpan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                                String baca = sp.getString("id_user", "");
                                simpanTglSelesai(ID_Survey.get(position),NoOrder.get(position),baca,txt_tanggal.getText().toString());
                                alert.cancel();
                            }
                        });

                        alert = alertDialogBuildertgl.create();
                        alert.show();
                    }

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return ID_Survey.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_NamaPerusahaan;
        public TextView txt_NoOrder;
        public TextView txt_StatusPengerjaan;
        public TextView txt_TanggalOrder;
        public CardView cvMain;
        public Button txt_Status_order;
        public ViewHolder(View v) {
            super(v);
            txt_NamaPerusahaan = (TextView) v.findViewById(R.id.title);
            txt_NoOrder = (TextView) v.findViewById(R.id.subtitle);
            txt_StatusPengerjaan= (TextView) v.findViewById(R.id.status_pengerjaan);
//            txt_TanggalOrder = (TextView) v.findViewById(R.id.date);
            txt_Status_order= (Button) v.findViewById(R.id.btn_deal);
            cvMain = (CardView) v.findViewById(R.id.cv_main);


        }
    }

    private void addEmployee(final String id_survey_get,final String status_terima_get,final String id_surveyor_get,final String no_order_get){



        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Menambahkan...","Tunggu...",false,false);
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
                params.put("id_survey",id_survey_get);
                params.put("status_terima",status_terima_get);
                params.put("id_surveyor",id_surveyor_get);
                params.put("no_order",no_order_get);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://45.77.40.141/kjpp/api/survey/post_konfirmasiorder.php/", params);
                //Toast.makeText(LoginActivity.this,""+res, Toast.LENGTH_LONG).show();
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();

    }

    private void simpanTgl(final String id_survey_get,final String no_order_get,final String id_surveyor_get,final String tanggal_get,final String nama_get,final String nope_get){



        class SimpanTgl extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Menambahkan...","Tunggu...",false,false);
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
                params.put("id_survey",id_survey_get);
                params.put("id_surveyor",id_surveyor_get);
                params.put("no_order",no_order_get);
                params.put("tanggal_survey",tanggal_get);
                params.put("nama_cp",nama_get);
                params.put("hp_cp",nope_get);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://45.77.40.141/kjpp/api/survey/post_mulai.php/", params);
                //Toast.makeText(LoginActivity.this,""+res, Toast.LENGTH_LONG).show();
                return res;
            }
        }

        SimpanTgl ae = new SimpanTgl();
        ae.execute();

    }

    private void simpanTglSelesai(final String id_survey_get,final String no_order_get,final String id_surveyor_get,final String tanggal_get){



        class SimpanTglSelesai extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Menambahkan...","Tunggu...",false,false);
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
                params.put("id_survey",id_survey_get);
                params.put("id_surveyor",id_surveyor_get);
                params.put("no_order",no_order_get);
                params.put("tanggal_selesai",tanggal_get);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://45.77.40.141/kjpp/api/survey/post_selesai.php/", params);
                //Toast.makeText(LoginActivity.this,""+res, Toast.LENGTH_LONG).show();
                return res;
            }
        }

        SimpanTglSelesai ae = new SimpanTglSelesai();
        ae.execute();

    }

    private void showEmployee(String kuy){
        Toast.makeText(context,"Get Json : "+kuy,Toast.LENGTH_LONG).show();
        JSONObject jsonObject = null;
//        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(kuy);
//            JSONArray result = jsonObject.getJSONArray("balita");
//            Toast.makeText(LoginActivity.this,"Get Json : "+jsonObject.get("username"),Toast.LENGTH_LONG).show();

//            username = ""+jsonObject.get("username");
//            id_user = "" +jsonObject.get("id_user");
//            hak_akses = ""+jsonObject.get("hak_akses");
//            nama_user = ""+ jsonObject.get("nama_user");
//            msg = ""+jsonObject.get("msg");
//            success = ""+jsonObject.get("success");
//
//            if(hak_akses.equals("SURVEYOR") && msg.equals("ok")&&success.equals("1")){
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//                SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
//                SharedPreferences.Editor ed=sp.edit();
//                ed.putString("id_user", id_user);
//                ed.putString("nama_user", nama_user);
//                ed.putString("username", username);
//                ed.commit();
//
//                finish();
//            }

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
