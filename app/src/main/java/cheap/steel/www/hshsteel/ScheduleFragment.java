package cheap.steel.www.hshsteel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class ScheduleFragment extends Fragment {
    // Log tag
    private static final String TAG = ScheduleFragment.class.getSimpleName();

    // Movies json url
    private static final String url = "http://188.166.245.22/hsh/indexSCH.php";
    private ProgressDialog pDialog;
    private List<Item> itemList = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        setHasOptionsMenu(true);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), itemList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

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
                                item.setUnitprice(obj.getString("unitprice"));

                                // adding item to movies array
                                itemList.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(itemsReq);
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
}
