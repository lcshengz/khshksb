package cheap.steel.www.hshsteel;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cheap.steel.www.hshsteel.adater.SolventRecyclerViewAdapter;
import cheap.steel.www.hshsteel.app.HttpHandler;
import cheap.steel.www.hshsteel.model.ItemObjects;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    int id = 1;
    public static List<ItemObjects> listViewItems;
    private TextView tvMarquee;
    boolean doubleBackToExitPressedOnce = false;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private String LOG_TAG = "XML";
    private SolventRecyclerViewAdapter rcAdapter;
    private final static String url = "http://188.166.245.22/hsh/indexCategory.php";
    RequestQueue requestQueue;
    JsonArrayRequest RequestOfJSonArray;
    RecyclerView recyclerView;
    private FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listViewItems = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        rcAdapter = new SolventRecyclerViewAdapter(MainActivity.this, listViewItems);
        recyclerView.setAdapter(rcAdapter);

        if(isNetworkConnected()) {
            initializeFirebase();
            fetchRemoteConfigValues();

            JSON_HTTP_CALL();
            new GetXMLFromServer().execute();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Information");
            builder.setCancelable(false);
            builder.setMessage("Internet connection is required to run this app!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.show();
            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            assert messageView != null;
            messageView.setGravity(Gravity.START);
        }

        if(getIntent().getBooleanExtra("Exit", false)) {
            finish();
        }
    }

    public void initializeFirebase() {
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        }
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(false)
                .build();
        config.setConfigSettings(configSettings);
    }

    private void fetchRemoteConfigValues() {
        long cacheExpiration = 3600;

        //expire the cache immediately for development mode.
        if (mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            mRemoteConfig.activateFetched();
                            appUpdater(mRemoteConfig.getString("versionCode"));
                        } else {
                            //task failed
                        }
                    }
                });
    }

    private void appUpdater(String playStoreVersionName) {

        int NewVersionName = Integer.parseInt(playStoreVersionName);
        int currentAppVersionName=0;
        try {
            currentAppVersionName = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(currentAppVersionName<NewVersionName){
            //Create Alert Dialog
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("New Version")
                    .setMessage("Update available!\nGo to Play Store now?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=cheap.steel.www.hshsteel"));
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            alertDialog.show();
        }
    }

    public void JSON_HTTP_CALL(){

        pDialog = new ProgressDialog(MainActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        RequestOfJSonArray = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Log", response.toString());
                        ParseJSonResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                JSON_HTTP_CALL();
                VolleyLog.d("Volley", "Error: " + error.getMessage());
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        RequestOfJSonArray.setShouldCache(false);
        requestQueue.add(RequestOfJSonArray);
    }

    public void ParseJSonResponse(JSONArray array){

        for (int i = 0; i < array.length(); i++) {
            ItemObjects itemObjects1 = new ItemObjects();
            JSONObject obj = null;
            try {
                obj = array.getJSONObject(i);

                itemObjects1.setName(obj.getString("Category"));
                itemObjects1.setPhoto(obj.getString("Image"));
                itemObjects1.setFile(obj.getString("File"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listViewItems.add(itemObjects1);
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        rcAdapter = new SolventRecyclerViewAdapter(this, listViewItems);
        recyclerView.setAdapter(rcAdapter);
        requestQueue.getCache().clear();

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pDialog.dismiss();
            }
        });
    }

    public void ParseXML(String xmlString){

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                eventType = parser.next();
            }


        }catch (Exception e){
            Log.d(LOG_TAG,"Error in ParseXML()",e);
        }

    }

    private class GetXMLFromServer extends AsyncTask<String,Void,String> {

        HttpHandler nh;

        @Override
        protected String doInBackground(String... strings) {

            final String URL = "http://steel.cheap/dl/message.xml";
            String res = "";
            nh =  new HttpHandler();
            InputStream is = nh.CallServer(URL);
            if(is!=null){
                res = nh.StreamToString(is);

            }else{
                res = "NotConnected";
            }

            return res;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("NotConnected")){

                Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_SHORT).show();

            }else {
                tvMarquee = (TextView)findViewById(R.id.tvMainMarquee);
                tvMarquee.setSelected(true);
                tvMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tvMarquee.setSingleLine(true);
                tvMarquee.setText(result);
            }
        }
    }

    public void switchContent(int fragment_frame, Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(fragment_frame, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainactivity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fab1:
                Intent homeIntent = new Intent(this, Chat.class);
                startActivity(homeIntent);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit", true);
                startActivity(intent);
                finish();
                return;
            }
        } else {
            getFragmentManager().popBackStack();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}