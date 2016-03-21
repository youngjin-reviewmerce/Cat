package com.reviewmerce.exchange.ListView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.reviewmerce.exchange.Fragment.baseOnebuyFragment;
import com.reviewmerce.exchange.R;
import com.reviewmerce.exchange.commonData.apiData.SuggestKeyword;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 2015-07-12.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    Context context;
    List<SuggestKeyword> MySearchedItems;
    List<SuggestKeyword> SuggestItems;
    private baseOnebuyFragment.SearchFragmentListener mCallbackSearchFragment;

    public SearchResultAdapter(Context context) {
        this.context=context;

        SuggestItems = new ArrayList<SuggestKeyword>();
        MySearchedItems = new ArrayList<SuggestKeyword>();
    }
    public void setSuggestItems(List <SuggestKeyword> items)
    {
        if(SuggestItems.size() > 0)
            SuggestItems.clear();
        SuggestItems = items;
    }
    public void setSearchedItems(List <SuggestKeyword> items)
    {
        if(MySearchedItems.size() > 0)
            MySearchedItems.clear();
        MySearchedItems = items;
    }
    public void setCallback(baseOnebuyFragment.SearchFragmentListener callback)
    {
        mCallbackSearchFragment = callback;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_keyword,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SuggestKeyword item;
        if(position < MySearchedItems.size())
        {
            item = MySearchedItems.get(position);
        }
        else
        {
            item = SuggestItems.get(position-MySearchedItems.size());
        }
       // holder.image.setImageBitmap(item.getImage());
//        Drawable drawable=context.getResources().getDrawable(item.getImage());
 //       holder.image.setBackground(drawable);
        if(item.getType() == 1)
        {
            holder.type.setText("( 지난 검색어 )");
        }
        else
        {
            holder.type.setText("( 추천 상품 )");
        }
        holder.keyword.setText(item.getKeyword());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallbackSearchFragment != null)
                {
                    mCallbackSearchFragment.selectKeyword(item);
                }
                //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        int nRtnVal = MySearchedItems.size() + SuggestItems.size();

        return nRtnVal;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView keyword;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            type=(TextView)itemView.findViewById(R.id.tv_type);
            keyword = (TextView)itemView.findViewById(R.id.tv_keyword);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
        }
    }
    public void clearAll()
    {

    }

}
