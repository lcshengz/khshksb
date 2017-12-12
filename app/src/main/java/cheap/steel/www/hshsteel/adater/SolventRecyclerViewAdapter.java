package cheap.steel.www.hshsteel.adater;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cheap.steel.www.hshsteel.DynamicFragment;
import cheap.steel.www.hshsteel.MainActivity;
import cheap.steel.www.hshsteel.R;
import cheap.steel.www.hshsteel.model.ItemObjects;

public class SolventRecyclerViewAdapter extends RecyclerView.Adapter<SolventViewHolders> {

    private List<ItemObjects> itemList;
    private Context mContext;
    private ProgressDialog pDialog;

    public SolventRecyclerViewAdapter(Context context, List<ItemObjects> itemList) {
        this.itemList = itemList;
        this.mContext = context;
    }

    @Override
    public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.solvent_list, null);
        SolventViewHolders rcv = new SolventViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(SolventViewHolders holder, int position) {

        //ItemObjects itemObjects1 = itemList.get(position);
        String category = itemList.get(position).getName();
        String imageLocation = itemList.get(position).getFile();
        holder.itemName.setText(category);
        holder.itemImage.setImageBitmap(getBitmapFromUrl(itemList.get(position).getPhoto()));
        //int id = mContext.getResources().getIdentifier(itemObjects1.getPhoto(), "drawable", mContext.getPackageName());
        //holder.itemImage.setImageResource(id);
        holder.itemFile.setText(imageLocation);

        final String phpFile = String.valueOf(holder.itemFile.getText());

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentJump(phpFile);
            }
        });

        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentJump(phpFile);
            }
        });
    }

    private Bitmap getBitmapFromUrl(String imageuri){
        HttpURLConnection connection=null;

        try {
            URL url=new URL(imageuri);
            try {
                connection= (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoInput(true);
            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is= null;
            try {
                is = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap mybitmap= BitmapFactory.decodeStream(is);
            return mybitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fragmentJump(String phpFile1) {
        Fragment mFragment;
        mFragment = new DynamicFragment();

        Bundle mBundle = new Bundle();
        mBundle.putString("index_php", phpFile1);
        mFragment.setArguments(mBundle);
        switchContent(R.id.fragment_frame, mFragment);
    }

    private void switchContent(int fragment_frame, Fragment mFragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.switchContent(fragment_frame, mFragment);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList == null ? 0 : this.itemList.size();
    }

}