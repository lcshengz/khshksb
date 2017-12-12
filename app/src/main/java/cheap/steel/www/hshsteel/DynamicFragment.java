package cheap.steel.www.hshsteel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cheap.steel.www.hshsteel.adater.CustomListAdapter;
import cheap.steel.www.hshsteel.app.AppController;
import cheap.steel.www.hshsteel.model.Item;

public class DynamicFragment extends Fragment {
    // Log tag
    private static final String TAG = DynamicFragment.class.getSimpleName();
    private String phpFile;
    private String url;
    private ProgressDialog pDialog;
    private List<Item> arrayList = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        setHasOptionsMenu(true);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        FloatingActionButton fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setAlpha(0.8f);
        fab1.setAlpha(0.8f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Chat.class);
                startActivity(intent);
            }
        });

        initViews(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(((MainActivity)getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }

    private void initViews(View view){

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);

        if(isNetworkConnected()) {
            getList();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //builder.setTitle("Information");
            builder.setCancelable(false);
            builder.setMessage("Internet connection is required to run this app!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.show();
            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            assert messageView != null;
            messageView.setGravity(Gravity.LEFT);
        }
    }

    public void getList() {
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        if(getArguments() != null) {
            phpFile = getArguments().getString("index_php");
        }

        url = "http://188.166.245.22/hsh/" + phpFile + ".php";

        if(!phpFile.equals("")) {
            // Creating volley request obj
            JsonArrayRequest itemsReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            hidePDialog();

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Item item = new Item();
                                    item.setDescription(obj.getString("description"));
                                    item.setThumbnailUrl(obj.getString("thumbnailUrl"));
                                    item.setItemcode(obj.getString("itemcode"));
                                    item.setQuantityuom(obj.getString("quantityuom"));
                                    item.setQuantityuom1(obj.getString("quantityuom1"));
                                    item.setQuantityuom2(obj.getString("quantityuom2"));
                                    item.setQuantityuom3(obj.getString("quantityuom3"));
                                    item.setUnitprice(obj.getString("unitprice"));
                                    item.setUnitprice1(obj.getString("unitprice1"));
                                    item.setUnitprice2(obj.getString("unitprice2"));
                                    item.setUnitprice3(obj.getString("unitprice3"));
                                    item.setGst(obj.getString("gst"));
                                    item.setGst1(obj.getString("gst1"));
                                    item.setGst2(obj.getString("gst2"));
                                    item.setGst3(obj.getString("gst3"));

                                    // adding item to movies array
                                    arrayList.add(item);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter = new CustomListAdapter(getActivity(), arrayList);
                            listView.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hidePDialog();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    getList();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(itemsReq);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //builder.setTitle("Information");
            builder.setCancelable(false);
            builder.setMessage("Coming soon!\n\nAkan datang!\n\n即将更新！");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.show();
            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            assert messageView != null;
            messageView.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}