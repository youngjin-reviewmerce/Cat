package com.reviewmerce.exchange.ListView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.reviewmerce.exchange.Fragment.baseOnebuyFragment;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.apiData.ListedItem;
import com.reviewmerce.exchange.publicClass.ShoppingDataLab;


import java.util.List;

/**
 * Created by songmho on 2015-07-12.
 */
public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    Context context;
    List<ListedItem> items;
    public baseOnebuyFragment.FragmentChangeListener mCallback=null;
    int item_layout;

    public SearchItemAdapter(Context context, List<ListedItem> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;

    }
    public void setCallback(baseOnebuyFragment.FragmentChangeListener callback)
    {
        mCallback = callback;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_item,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        ShoppingDataLab dataLab = ShoppingDataLab.get(null);

        final ListedItem item=items.get(position);
        //imageLoader.displayImage("assets://image/"+item.getLocalUrl(),holder.image,dataLab.getDisplayOptions());
        imageLoader.displayImage(item.getImgUrl(),holder.image,dataLab.getDisplayOptions());

        //Bitmap bmp = item.getImage();
        //if(bmp!=null)
        //    holder.image.setImageBitmap(bmp);
        holder.tvTitle.setText(item.getItemName());
        holder.tv1.setText(item.getDescription());
        holder.tv2.setText(item.getPrice());
//        Drawable drawable=context.getResources().getDrawable(item.getImage());
 //       holder.image.setBackground(drawable);
     //   holder.title.setText(item.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null)
                {
                    mCallback.newWebpage(item.getLinkUrl());
                }
               // Toast.makeText(context, item.getLinkUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvTitle;
        TextView tv1;
        TextView tv2;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tv1 = (TextView)itemView.findViewById(R.id.tv1);
            tv2 = (TextView)itemView.findViewById(R.id.tv2);
     //       title=(TextView)itemView.findViewById(R.id.title);
            cardview=(CardView)itemView.findViewById(R.id.listitem_cardview);
        }
    }
}
