package com.justin.cheapestproduct;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private Toolbar toolbar;
    private ImageButton btnScan;
    private Button btnFeedBack;
    private SearchView queryEdt;
    private TextView txtProductTitle,txtProductDESC,txtProductBarcode,
            txt_drawer_account_information_username,txt_drawer_account_information_email;
    private RecyclerView recycleView;
    private MyAdapter adapter;
    private ArrayList<String> mData = new ArrayList<>();
    private String barcodeNo ="",goodsName = "",loginName,loginEmail,loginPhotoUrl;
    private String TAG="GoogleAcount";
    private ImageView txt_drawer_account_picture_profile;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildViews();

        //使用toolbar做為APP的ActionBar
        setSupportActionBar(toolbar);

        //將DrawerLayout與Toolbar整合，出現「三」按鈕
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
//        drawerLayout.openDrawer(Gravity.START);
        drawerLayout.addDrawerListener(toggle);


        toggle.syncState();


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.setPrompt("請將商品條碼置於掃描框內");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
        queryEdt.setFocusable(false);
        queryEdt.setIconifiedByDefault(false);
        queryEdt.setOnQueryTextListener(queryListener);


        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentUser!= null){
                    if(!barcodeNo.equals("")){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,PriceFeedBack.class);

                        Bundle bundle = new Bundle();

                        bundle.putString("barcode",barcodeNo);
                        bundle.putString("goodsName",goodsName);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }else{
                        Toast.makeText(MainActivity.this,"請先查詢商品後再做價格回報!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"請先登入會員!",Toast.LENGTH_SHORT).show();
                    signIn();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"已取消",Toast.LENGTH_SHORT).show();
            }else {
//                queryEdt.setQuery(result.getContents(),true);

                if(result.getContents().length()==12){
                    product_ApiGet(0+result.getContents());
                }else{
                    product_ApiGet(result.getContents());
                }

            }
        }


    }

    private void buildViews(){
        btnScan = (ImageButton)findViewById(R.id.btnScan);
        queryEdt = (SearchView)findViewById(R.id.queryEdt);
        txtProductTitle = (TextView)findViewById(R.id.txtProductTitle);
        txtProductBarcode = (TextView)findViewById(R.id.txtProductBarcode);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        btnFeedBack = (Button)findViewById(R.id.btnFeedBack);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigation_view = (NavigationView)findViewById(R.id.navigation_view);

        toolbar =(Toolbar) findViewById(R.id.toolbar);

        txt_drawer_account_information_username = (TextView)navigation_view.getHeaderView(0).findViewById(R.id.txt_drawer_account_information_username);
        txt_drawer_account_information_email = (TextView)navigation_view.getHeaderView(0).findViewById(R.id.txt_drawer_account_information_email);
        txt_drawer_account_picture_profile = (ImageView)navigation_view.getHeaderView(0).findViewById(R.id.txt_drawer_account_picture_profile);
//        txt_drawer_account_picture_profile = (ImageView)findViewById(R.id.txt_drawer_account_picture_profile);

    }

    private void product_ApiGet(final String barcode){

        Log.d("ApiGet_Origin_barcodeNo", barcode);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        // http://118.163.251.54
        Request request = new Request.Builder()
                .url("http://118.163.251.54:8092/api/product/"+barcode)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String jsonStr = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{

                            Log.d("ApiGet_jsonStr",jsonStr);
                            if(!jsonStr.equals("[]")){
                                Gson gson = new Gson();
                                ArrayList<Product> getProduct = new ArrayList<Product>();
                                getProduct = gson.fromJson(jsonStr,new TypeToken<ArrayList<Product>>(){}.getType());

                                for(int i=0 ;i<getProduct.size();i++){
                                    barcodeNo=getProduct.get(i).getBarcode();
                                    goodsName=getProduct.get(i).getGoods_name();
                                }

                                Log.d("ApiGet_getBarcode:",barcodeNo);
                                Log.d("ApiGet_getName:",goodsName);

                                txtProductBarcode.setText(barcodeNo);
                                txtProductTitle.setText(goodsName);

                                priceDetail_ApiGet(barcode);

                            }else{
                                queryEdt.setQuery("",true);
                                txtProductBarcode.setText("");
                                txtProductTitle.setText("");
                                Toast.makeText(MainActivity.this,"國際條碼不存在",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JsonIOException e){
                            e.printStackTrace();
                        }
                    }
                });


            }
        });


    }







    public SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Log.d("ApiGet_getS:",s);
//            s="0000000015345";
            product_ApiGet(s);


            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };



    //priceDetail_ApiGet
    public void priceDetail_ApiGet(String barcode){


//"http://118.163.251.54:8092/api/product/detail/
        OkHttpClient mPriceOkHttpClient = new OkHttpClient();
        Request requestPrice = new Request.Builder()
                .url("http://118.163.251.54:8092/api/product/detail/"+barcode).build();

        Call callPrice = mPriceOkHttpClient.newCall(requestPrice);

        callPrice.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonPrice = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        List<String> priceList = new ArrayList<String>();
                        List<String> storeList = new ArrayList<String>();
                        List<String> remarkList = new ArrayList<String>();
                        List<String> dateList = new ArrayList<String>();

                        try{
                            Log.d("ApiGet_Price_jsonPrice",jsonPrice);
                            if(!jsonPrice.equals("[]")){
                                Gson gsonPrice = new Gson();
                                ArrayList<Price> getPrice = new ArrayList<Price>();
                                getPrice = gsonPrice.fromJson(jsonPrice,new TypeToken<ArrayList<Price>>(){}.getType());



                                Log.d("ApiGet_Price_jsonPrice","price取得成功");
                                for(int j=0;j<getPrice.size();j++){
                                    priceList.add(getPrice.get(j).getPrice1());
                                    storeList.add(String.valueOf(getPrice.get(j).getStore_id()));
                                    String remark="";
                                    //remark = getPrice.get(j).getRemark();

                                    if (getPrice.get(j).getRemark() != null){

                                        remark = getPrice.get(j).getRemark()+"," +"由"+getPrice.get(j).getUser_id()+"回報";
                                    }else {
                                        remark = "由"+getPrice.get(j).getUser_id()+"回報";
                                    }

                                    remarkList.add(remark);
                                    //remarkList.add(getPrice.get(j).getRemark()+"由"+getPrice.get(j).getUser_id()+"回報");



                                    dateList.add(  getPrice.get(j).getUpdate_date().substring(0,10));
//                                    Log.d("ApiGet_Price_priceList",priceList.toString());
//                                    Log.d("ApiGet_Price_remarkList",remarkList.toString());
//                                    Log.d("ApiGet_Price_dateList",dateList.toString());


                                    recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                    recycleView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
                                    adapter = new MyAdapter(priceList,storeList,remarkList,dateList);
                                    recycleView.setAdapter(adapter);
                                }
//                                Log.d("ApiGet_Price",getPrice.get(0).getPrice1().toString());



                            }else{
                                Log.d("ApiGet_jsonPrice","price取得失敗");
                                priceList.clear();
                                storeList.clear();
                                remarkList.clear();
                                dateList.clear();
                                adapter = new MyAdapter(priceList,storeList,remarkList,dateList);

                                recycleView.removeAllViews();
                                recycleView.setAdapter(adapter);
                                Log.d("onRestart_barcode0",barcodeNo);
                                Toast.makeText(MainActivity.this,"查無價格",Toast.LENGTH_SHORT).show();

                            }
                        }catch (JsonIOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });




    }//End of priceDetail_ApiGet


    public NavigationView.OnNavigationItemSelectedListener navigationView = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Log.d("drawerLayout:","drawerLayout onNavigationItemSelected");

            switch (item.getItemId()){
                case R.id.action_home:
                    Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_setting:
                    break;
                case R.id.action_state:

                    if(currentUser!=null){
                        signOut();
                        txt_drawer_account_information_username.setText("");
                        txt_drawer_account_information_email.setText("");
                        bmp = null;
                        txt_drawer_account_picture_profile.setImageBitmap(bmp);
                    } else {
                        Log.d("action_state","to Google Sign in 01");
                        signIn();
                    }

                    break;
                case R.id.action_about:
                    break;

            }
            //點選時收起選單
//            drawerLayout.closeDrawer(GravityCompat.START);
            drawerLayout.closeDrawers();

            return true;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    private FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            mAuth=firebaseAuth;
            currentUser = firebaseAuth.getCurrentUser();

            navigation_view.setNavigationItemSelectedListener(navigationView);
            Menu menu = navigation_view.getMenu();
            MenuItem item = menu.findItem(R.id.action_state);






            if(currentUser!=null){
                Log.d("action_state",currentUser.getDisplayName());
                Log.d("action_state_url",currentUser.getPhotoUrl().toString());
                txt_drawer_account_information_username.setText(currentUser.getDisplayName());
                txt_drawer_account_information_email.setText(currentUser.getEmail());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("action_state_bmp","01");
                            URL url = new URL(currentUser.getPhotoUrl().toString());
                            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                            Log.d("action_state_bmp","02");
                            httpConn.connect();
                            InputStream in  = httpConn.getInputStream();
                            Log.d("action_state_bmp","03");
                            bmp = BitmapFactory.decodeStream(in);
                            txt_drawer_account_picture_profile.setImageBitmap(bmp);
                            Log.d("action_state_bmp","05");
                        }catch (Exception e){
                            Log.d("action_state_bmp","06:"+e.toString());
                        }
                    }
                }).start();





                ;
                //txt_drawer_account_picture_profile
                item.setTitle("登出");
            }else {
                Log.d("action_state","to Google Sign in 02");
                item.setTitle("登入");
            }
        }
    };


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "------------------ onStart ------------------");
        FirebaseAuth.getInstance().addAuthStateListener(authListener);


    }
    // [END on_start_check_user]


    @Override
    protected void onRestart() {


        super.onRestart();
        Log.d("onRestart_barcode",barcodeNo);
        if(currentUser != null){
            if(!barcodeNo.equals("")){

                priceDetail_ApiGet(barcodeNo);
            }
        }

    }

    private void signOut(){
        //Firebase登出
        mAuth.signOut();

        //Google登出
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignIn.getClient(this,gso).signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"SignOut",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn(){
        Intent intent_GoogleSign = new Intent(MainActivity.this,LoginMain.class);
        startActivity(intent_GoogleSign);
    }




}
