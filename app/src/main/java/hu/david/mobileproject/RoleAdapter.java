package hu.david.mobileproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hu.david.mobileproject.models.UserRole;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.ViewHolder> implements Filterable {

    private ArrayList<UserRole> mRoleData;
    private ArrayList<UserRole> mRoleDataAll;
    private Context mContext;
    private int lastPosition = -1;

    RoleAdapter(Context context, ArrayList<UserRole> itemsData){
        this.mRoleData = itemsData;
        this.mRoleDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_role_items, parent, false));
    }

    @Override
    public void onBindViewHolder(RoleAdapter.ViewHolder holder, int position) {
        UserRole currentItem = mRoleData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_animation);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mRoleData.size();
    }

    @Override
    public Filter getFilter() {
        return roleFilter;
    }

    private Filter roleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<UserRole> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = mRoleDataAll.size();
                results.values = mRoleDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(UserRole item : mRoleDataAll){
                    if(item.getInvolvementRole().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mRoleData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mDescText;

        public ViewHolder(View itemView){
            super(itemView);

            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDescText = itemView.findViewById(R.id.subTitle);
        }

        public void bindTo(UserRole currentItem) {
            mTitleText.setText(currentItem.getInvolvementRole());
            mDescText.setText(currentItem.getId());
        }
    }
}

