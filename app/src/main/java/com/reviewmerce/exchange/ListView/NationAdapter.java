package com.reviewmerce.exchange.ListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.NationItem;
import com.reviewmerce.exchange.commonData.SoundSearcher;

import java.util.ArrayList;
import java.util.List;

public class NationAdapter extends BaseAdapter {
    private Context mContext;
    private List<NationItem> mItems = new ArrayList<NationItem>();
    private boolean boolShowCheckBox = false;

    public void setBoolShowCheckBox(boolean boolShowCheckBox) {
        this.boolShowCheckBox = boolShowCheckBox;
    }

    public NationAdapter(Context context){
        mContext = context;
    }

    public void addItem(NationItem item) {
        mItems.add(item);
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public void setListItems(List<NationItem> lit) {
        mItems = lit;
    }

    public long getItemId(int position) {
        return position;
    }

    public List<NationItem> getDataArray(){
        return mItems;
    }
    public void setDataArray(List<NationItem> array){
        mItems = array;
    }

    public int searchNation(String s)
    {
        for(int i=0;i<mItems.size();i++)
        {
            if(SoundSearcher.matchString(mItems.get(i).getKorName(), s)==true)
                return i;
        }
        return 0;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;


        if(convertView == null){

            convertView = new NationView(mContext, mItems.get(position));

            holder = new ViewHolder();
            holder.currency_eng = (TextView)convertView.findViewById(R.id.textView_country);
            holder.currency_kor = (TextView)convertView.findViewById(R.id.textView_value);
            holder.imgView = (ImageView)convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);


        } else {
                holder = (ViewHolder) convertView.getTag();
        }
        holder.currency_eng.setText(mItems.get(position).getEngName());
        holder.currency_kor.setText(mItems.get(position).getKorName());
        holder.imgView.setImageBitmap(mItems.get(position).getIcon());

        /*
        if(boolShowCheckBox == true){
            editHolder.checkbox.setVisibility(View.VISIBLE);
            editHolder.circularIcon.setVisibility(View.VISIBLE);
        } else {
            editHolder.checkbox.setVisibility(View.INVISIBLE);
            editHolder.circularIcon.setVisibility(View.INVISIBLE);
        }
*/
        return convertView;
    }
}


class ViewHolder {
    ImageView imgView;
    TextView currency_eng;
    TextView currency_kor;
}