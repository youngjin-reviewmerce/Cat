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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.reviewmerce.exchange.Fragment.baseOnebuyFragment;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.apiData.ImageAndLink;
import com.reviewmerce.exchange.publicClass.ShoppingDataLab;


import java.util.List;

/**
 * Created by songmho on 2015-07-12.
 */
public class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ViewHolder> {
    Context context;
    List<ImageAndLink> items;
    public baseOnebuyFragment.FragmentChangeListener mCallback=null;
    int item_layout;
    public ImageItemAdapter(Context context, List<ImageAndLink> items, int item_layout) {
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
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_imageitem, null);

        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        ShoppingDataLab dataLab = ShoppingDataLab.get(null);
        final ImageAndLink item=items.get(position);
        //Bitmap bmp = item.getImage();
        //imageLoader.displayImage("assets://image/"+item.getImgUrl(),holder.image,dataLab.getDisplayOptions());
        imageLoader.displayImage("file://"+item.getLocalUrl(),holder.image,dataLab.getDisplayOptions());
        //Bitmap bmp = imageLoader.loadImageSync("http://site.com/image.png");//item.getImgUrl());
       // if(bmp!=null)
       //     holder.image.setImageBitmap(bmp);
//        Drawable drawable=context.getResources().getDrawable(item.getImage());
 //       holder.image.setBackground(drawable);
     //   holder.title.setText(item.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.newWebpage(item.getLinkUrl());
                }
             //   Toast.makeText(context, item.getLinkUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
    public void removeAllItem()
    {
        if(items != null)
            items.clear();
    }
    public void setItemList(List<ImageAndLink>list)
    {
        items = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
      //  TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
     //       title=(TextView)itemView.findViewById(R.id.title);
            cardview=(CardView)itemView.findViewById(R.id.imageitem_cardview);
        }
    }
}
