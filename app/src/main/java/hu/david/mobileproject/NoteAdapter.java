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

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {

    private ArrayList<Note> mNoteData;
    private ArrayList<Note> mNoteDataAll;
    private Context mContext;
    private int lastPosition = -1;

    NoteAdapter(Context context, ArrayList<Note> itemsData){
        this.mNoteData = itemsData;
        this.mNoteDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note currentItem = mNoteData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mNoteData.size();
    }

    @Override
    public Filter getFilter() {
        return noteFilter;
    }

    private Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Note> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = mNoteDataAll.size();
                results.values = mNoteDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Note item : mNoteDataAll){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
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
            mNoteData = (ArrayList) results.values;
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

        public void bindTo(Note currentItem) {
            mTitleText.setText(currentItem.getTitle());
            mDescText.setText(currentItem.getDesc());

            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((NoteListActivity)mContext).deleteItem(currentItem));
            itemView.findViewById(R.id.edit).setOnClickListener(view -> ((NoteListActivity)mContext).editItem(currentItem));
        }
    }
}

