package com.example.simplenewsreader;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rometools.rome.feed.synd.SyndEntry;

import java.util.ArrayList;

import static com.example.simplenewsreader.MainActivity.entries;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private static final String TAG = "RecyclerViewAdapter";

    // All variables we need for class
    private ArrayList<String> mTitleNames = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private Context mContext;


    public RecyclerViewAdapter( Context mContext, ArrayList<String> mTitle,ArrayList<String> mDescription) {
        this.mTitleNames = mTitle;
        this.mDescription = mDescription;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem,
                viewGroup, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        viewHolder.titleName.setText(mTitleNames.get(position));
        viewHolder.articleDesc.setText(mDescription.get(position));
        viewHolder.parentLayout.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Log.d(TAG, "onClick: clicked on:  " + position);

                        SyndEntry entry = (SyndEntry)entries.get(position);
                        String urlArg = entry.getUri();
                        // Transfer to new view,
                        // no need to make toast
                        Intent myIntent = new Intent(mContext, DisplayContent.class);
                        myIntent.putExtra("urlArg", urlArg); //Optional parameters

                        mContext.startActivity(myIntent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return mTitleNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView titleName;
        TextView articleDesc;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            titleName = itemView.findViewById(R.id.image_name);
            articleDesc = itemView.findViewById(R.id.Article_Desc);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
