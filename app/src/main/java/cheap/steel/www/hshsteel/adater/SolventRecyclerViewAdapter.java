package cheap.steel.www.hshsteel.adater;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cheap.steel.www.hshsteel.HollowFragment;
import cheap.steel.www.hshsteel.MainActivity;
import cheap.steel.www.hshsteel.R;
import cheap.steel.www.hshsteel.RoundPipeFragment;
import cheap.steel.www.hshsteel.model.ItemObjects;

public class SolventRecyclerViewAdapter extends RecyclerView.Adapter<SolventViewHolders> {

    private List<ItemObjects> itemList;
    private Context mContext;

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
        holder.countryName.setText(itemList.get(position).getName());
        holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());

        final int position1 = holder.getAdapterPosition();

        holder.countryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentJump(position1);
            }
        });
    }

    private void fragmentJump(int position) {
        Fragment mFragment = null;
        switch (position) {
            case 0:
                mFragment = new RoundPipeFragment();
                break;
            case 1:
                mFragment = new HollowFragment();
                break;
            default:
                mFragment = new RoundPipeFragment();
                break;
        }

        Bundle mBundle = new Bundle();
        mBundle.putInt("item_selected_key", position);
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
        return this.itemList.size();
    }
}

