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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cheap.steel.www.hshsteel.R;
import cheap.steel.www.hshsteel.app.AppController;
import cheap.steel.www.hshsteel.model.Item;

public class CustomListAdapter extends BaseAdapter {
	private Activity mActivity;
	private LayoutInflater inflater;
	private List<Item> itemList;
	private ArrayList<Item> arrayList;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Item> itemList) {
		this.mActivity = activity;
		this.itemList = itemList;
		//inflater = LayoutInflater.from(mActivity);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(itemList);
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public Object getItem(int location) {
		return itemList.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) mActivity
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
		TextView quantityuom1 = (TextView) convertView.findViewById(R.id.quantityuom1);
        TextView unitprice1 = (TextView) convertView.findViewById(R.id.unitprice1);
		TextView quantityuom2 = (TextView) convertView.findViewById(R.id.quantityuom2);
        TextView unitprice2 = (TextView) convertView.findViewById(R.id.unitprice2);
        TextView quantityuom3 = (TextView) convertView.findViewById(R.id.quantityuom3);
        TextView unitprice3 = (TextView) convertView.findViewById(R.id.unitprice3);

		// getting item data for the row
		Item m = itemList.get(position);
        //arrayList.addAll(itemList);

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		
		// item description
		description.setText(m.getDescription());
		
		// item code
        if(!m.getItemcode().equals("")) {
            itemcode.setText("Code: " + m.getItemcode());
        } else {
            itemcode.setText(" ");
        }

        // uom and price
		quantityuom.setText(m.getQuantityuom());
        if(!m.getUnitprice().equals("0.00") && !m.getUnitprice().equals("") && !m.getUnitprice().equals("null")) {
            unitprice.setText("RM " + m.getUnitprice() + "\n+GST RM " + m.getGst());
        } else {
            unitprice.setText(" ");
        }
        quantityuom1.setText(m.getQuantityuom1());
        if(!m.getUnitprice1().equals("0.00") && !m.getUnitprice1().equals("") && !m.getUnitprice1().equals("null")) {
            unitprice1.setText("RM " + m.getUnitprice1() + "\n+GST RM " + m.getGst1());
        } else {
            unitprice1.setText(" ");
        }
        quantityuom2.setText(m.getQuantityuom2());
        if(!m.getUnitprice2().equals("0.00") && !m.getUnitprice2().equals("") && !m.getUnitprice2().equals("null")) {
            unitprice2.setText("RM " + m.getUnitprice2() + "\n+GST RM " + m.getGst2());
        } else {
            unitprice2.setText(" ");
        }
        quantityuom3.setText(m.getQuantityuom3());
        if(!m.getUnitprice3().equals("0.00") && !m.getUnitprice3().equals("") && !m.getUnitprice3().equals("null")) {
            unitprice3.setText("RM " + m.getUnitprice3() + "\n+GST RM " + m.getGst3());
        } else {
            unitprice3.setText(" ");
        }

		return convertView;
	}

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arrayList);
        } else {
            for (Item wp : arrayList) {
                if (wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText) || wp.getItemcode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}