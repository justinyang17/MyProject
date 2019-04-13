package com.justin.cheapestproduct;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PriceFeedBack extends AppCompatActivity {

    private Toolbar toolbar;
    private String TAG="priceback";
    private TextView txtShowBarcode,txtShowName;
    private Button btnSendPrice;
    private EditText edtFeedBackPrice,edtFeedBackRemark;
    private Spinner spnList;
    private int store_id;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_feed_back);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date curDate = new Date(System.currentTimeMillis());
        final String dateStr = formatter.format(curDate);

        buildViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        final String barcode = bundle.getString("barcode");
        String goodsName = bundle.getString("goodsName");


        txtShowBarcode.setText(barcode);
        txtShowName.setText(goodsName);

        spnList.setOnItemSelectedListener(spnListener);



        btnSendPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtFeedBackPrice.getText().toString().equals("")){
                    Toast.makeText(PriceFeedBack.this,"請輸入價格 !", Toast.LENGTH_SHORT).show();
                    edtFeedBackPrice.setFocusable(true);
                }else{
                    OkHttpClient okHttpClient = new OkHttpClient();

                    Price price = new Price();
                    price.setUser_id(currentUser.getDisplayName());
                    price.setBarcode(barcode);
                    price.setPrice1(edtFeedBackPrice.getText().toString());
                    price.setRemark(edtFeedBackRemark.getText().toString());
                    price.setArea_id(1);//1=金門
                    price.setStore_id(store_id);
                    price.setUpdate_date(dateStr);

                    Gson gson = new Gson();
                    String json = gson.toJson(price);

                    Log.d(TAG,json);

                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"),json);

                    Request requestPost = new Request.Builder()
                            .url("http://118.163.251.54:8092/api/product/detail/add/")
                            .post(requestBody)
                            .build();

                    Call call = okHttpClient.newCall(requestPost);

                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try{
                                        System.out.println(response.body().string());
                                        Log.d(TAG,response.body().string());
                                    }catch (Exception e){

                                    }

                                }
                            });
                        }
                    });
                    Toast.makeText(PriceFeedBack.this,"價格回報完成", Toast.LENGTH_SHORT).show();

                    finish();
                }


            }
        });





    }

    private void buildViews(){
        spnList =(Spinner)findViewById(R.id.spnList);
        txtShowBarcode = (TextView)findViewById(R.id.txtShowBarcode);
        txtShowName = (TextView)findViewById(R.id.txtShowName);
        btnSendPrice = (Button) findViewById(R.id.btnSendPrice);
        edtFeedBackPrice = (EditText)findViewById(R.id.edtFeedBackPrice);
        edtFeedBackRemark =(EditText)findViewById(R.id.edtFeedBackRemark);

        String[] ArrayList={"風獅爺商場","全聯","寶雅","家樂福","日藥本舖","康是美","屈臣氏"};

//        ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,ArrayList);
//        adapterList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,R.layout.my_spinner,ArrayList);
        adapterList.setDropDownViewResource(R.layout.my_spinner);
        spnList.setAdapter(adapterList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //Toast.makeText(this,"返回了", Toast.LENGTH_SHORT).show();
                this.finish(); //back Button

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "------------------ onStart ------------------");
        FirebaseAuth.getInstance().addAuthStateListener(authListener);
    }
    // [END on_start_check_user]

    private AdapterView.OnItemSelectedListener spnListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            store_id=i+1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    private FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

//            mAuth=firebaseAuth;
            currentUser = firebaseAuth.getCurrentUser();
            if(currentUser!=null){

                Log.d(TAG+"action_state",currentUser.getDisplayName());

            }else {
                Log.d(TAG+"action_state","to Google Sign in 02");

            }
        }
    };


}
