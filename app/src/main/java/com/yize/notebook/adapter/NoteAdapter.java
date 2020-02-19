package com.yize.notebook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yize.notebook.EditActivity;
import com.yize.notebook.MainActivity;
import com.yize.notebook.Note;
import com.yize.notebook.R;

import java.util.ArrayList;
import java.util.List;

import static com.yize.notebook.util.DefaultParams.OPEN_MODE_MODIFY;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {
    private List<Note> noteList;
    private List<Note> backList;
    private Context context;
    private Filter filter;
    private Activity activity;


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_content_preview,tv_modified_time;
        CardView cv_note_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content_preview=(TextView)itemView.findViewById(R.id.tv_content_preview);
            tv_modified_time=(TextView)itemView.findViewById(R.id.tv_modified_time);
            cv_note_item=(CardView)itemView.findViewById(R.id.cv_note_item);
        }
    }
    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
        this.backList=noteList;
    }
    public NoteAdapter(List<Note> noteList, Activity activity) {
        this.noteList = noteList;
        this.backList=noteList;
        this.activity=activity;
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
        holder.cv_note_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EditActivity.class);
                intent.putExtra("note",note);
                intent.putExtra("openMode",OPEN_MODE_MODIFY);
                if(activity!=null){
                    activity.startActivityForResult(intent,0);
                }else {
                    context.startActivity(intent);
                }


            }
        });
        holder.cv_note_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

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
