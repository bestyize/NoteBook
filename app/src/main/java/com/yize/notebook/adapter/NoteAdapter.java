package com.yize.notebook.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yize.notebook.Note;
import com.yize.notebook.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {
    private List<Note> noteList;
    private List<Note> backList;
    private Context context;
    private Filter filter;


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_content_preview,tv_modified_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content_preview=(TextView)itemView.findViewById(R.id.tv_content_preview);
            tv_modified_time=(TextView)itemView.findViewById(R.id.tv_modified_time);
        }
    }

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
        this.backList=noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.item_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Note note=noteList.get(position);
        holder.tv_modified_time.setText(note.getTime());
        if(note.getContent().length()>15){
            holder.tv_content_preview.setText(note.getContent().substring(0,15).replaceAll("\n","")+"...");
        }else {
            holder.tv_content_preview.setText(note.getContent().replaceAll("\n",""));
        }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new SearchFilter();
        }
        return filter;
    }

    class SearchFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result=new FilterResults();
            List<Note> list;
            if(TextUtils.isEmpty(constraint)){
                list=backList;
            }else {
                list=new ArrayList<>();
                if(noteList.size()==0){
                    noteList=backList;
                }
                for (Note note:noteList){
                    if(note.getContent().contains(constraint)){
                        list.add(note);
                    }
                }
            }
            result.values=list;
            result.count=list.size();
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteList=(List<Note>)results.values;
            notifyDataSetChanged();
        }
    }


}
