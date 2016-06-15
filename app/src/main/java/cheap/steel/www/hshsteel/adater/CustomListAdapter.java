package cheap.steel.www.hshsteel.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import cheap.steel.www.hshsteel.app.AppController;
import cheap.steel.www.hshsteel.model.Item;
import cheap.steel.www.hshsteel.R;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Item> Items;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Item> Items) {
		this.activity = activity;
		this.Items = Items;
	}

	@Override
	public int getCount() {
		return Items.size();
	}

	@Override
	public Object getItem(int location) {
		return Items.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView itemcode = (TextView) convertView.findViewById(R.id.itemcode);
		TextView description = (TextView) convertView.findViewById(R.id.description);
		TextView quantityuom = (TextView) convertView.findViewById(R.id.quantityuom);
		TextView unitprice = (TextView) convertView.findViewById(R.id.unitprice);

		// getting movie data for the row
		Item m = Items.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		
		// title
		description.setText(m.getDescription());
		
		// rating
        if(!m.getItemcode().equals("")) {
            itemcode.setText("Code: " + m.getItemcode());
        } else {
            itemcode.setText(" ");
        }

		quantityuom.setText(m.getQuantityuom());
		
		// release year
		unitprice.setText(m.getUnitprice());

		return convertView;
	}

}