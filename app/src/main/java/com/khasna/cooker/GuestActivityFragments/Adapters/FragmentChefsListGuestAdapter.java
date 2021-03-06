package com.khasna.cooker.GuestActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;


/**
 * Created by nachiket on 4/6/2017.
 */

public class FragmentChefsListGuestAdapter extends RecyclerView.Adapter<FragmentChefsListGuestAdapter.ViewHolder> {

    public interface OnClickListener{
        void OnClick(int position);
    }
    OnClickListener mOnClickListener;
    Collection mCollection;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView cookName;
        public TextView cookSpeciality;
        public TextView cookAddress;
        public TextView cookPhoneNumber;
        public ImageView cookImage;
        public ViewHolder(View v) {
            super(v);
            cookName        = (TextView) v.findViewById(R.id.textViewCookName);
            cookSpeciality  = (TextView) v.findViewById(R.id.textViewCookSpeciality);
            cookAddress     = (TextView) v.findViewById(R.id.textViewCookAddress);
            cookImage       = (ImageView) v.findViewById(R.id.imageViewCooksPicture);
            cookPhoneNumber = (TextView) v.findViewById(R.id.textViewChefPhoneNumber);
        }
    }

    public FragmentChefsListGuestAdapter(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        mCollection = Collection.getInstance();
    }

    @Override
    public FragmentChefsListGuestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_layout_cooks, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FragmentChefsListGuestAdapter.ViewHolder vh = new FragmentChefsListGuestAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FragmentChefsListGuestAdapter.ViewHolder holder, int position) {
        holder.cookName.setText(mCollection.GetChefsListForGuest().get(position).getFullName());
        holder.cookSpeciality.setText("");
        holder.cookAddress.setText(mCollection.GetChefsListForGuest().get(position).getFullAddress());
        holder.cookPhoneNumber.setText(mCollection.GetChefsListForGuest().get(position).getPhoneNO());

        if(mCollection.GetChefsListForGuest().get(position).getUriProfilePic() != null){
            holder.cookImage.setImageURI(mCollection.GetChefsListForGuest().get(position).getUriProfilePic());
        }

        final int itemPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.OnClick(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCollection.GetChefsListForGuest().size();
    }
}
