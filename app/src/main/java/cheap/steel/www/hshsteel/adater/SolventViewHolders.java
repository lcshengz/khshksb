package cheap.steel.www.hshsteel.adater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cheap.steel.www.hshsteel.R;

public class SolventViewHolders extends RecyclerView.ViewHolder {
    public TextView itemName;
    public ImageView itemImage;
    public TextView itemFile;

    public SolventViewHolders(View itemView) {
        super(itemView);

        itemName = (TextView) itemView.findViewById(R.id.item_name);
        itemImage = (ImageView) itemView.findViewById(R.id.item_image);
        itemFile = (TextView) itemView.findViewById(R.id.item_file);

    }
}