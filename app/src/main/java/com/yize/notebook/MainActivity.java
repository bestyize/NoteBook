package com.yize.notebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yize.notebook.adapter.NoteAdapter;
import com.yize.notebook.util.DataHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yize.notebook.util.DefaultParams.OPCODE_DELETE_NOTE;
import static com.yize.notebook.util.DefaultParams.OPCODE_MODIFY_NOTE;
import static com.yize.notebook.util.DefaultParams.OPCODE_NEW_NOTE;
import static com.yize.notebook.util.DefaultParams.OPCODE_NOT_MODIFY;
import static com.yize.notebook.util.DefaultParams.OPEN_MODE_NEW;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FloatingActionButton fb_add_new_note;
    private RecyclerView rv_note_list;
    private static String TAG="MainActivity";
    private List<Note> noteList=new ArrayList<>();
    private DataHelper helper=new DataHelper(MainActivity.this);
    private NoteAdapter noteAdapter;
    private Toolbar tb_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewInit();

    }

    private void viewInit(){
        tb_main=(Toolbar)findViewById(R.id.tb_main);
        setSupportActionBar(tb_main);
        fb_add_new_note=(FloatingActionButton)findViewById(R.id.fb_add_new_note);
        fb_add_new_note.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        rv_note_list=(RecyclerView)findViewById(R.id.rv_note_list);
        fb_add_new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"fb_add_new_note clicked");
                Intent intent=new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("openMode",OPEN_MODE_NEW);
                startActivityForResult(intent,0);
            }
        });
        noteList=new ArrayList<>();
        noteAdapter=new NoteAdapter(noteList,this);
        rv_note_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv_note_list.setAdapter(noteAdapter);
        refreshView();
    }

    private void refreshView(){
        helper.open();

        if(!noteList.isEmpty()){
            noteList.clear();
        }
        noteList.addAll(helper.getNoteList());
        Collections.sort(noteList,(o1,o2)->o2.getTime().compareTo(o1.getTime()));
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item=menu.findItem(R.id.menu_main_search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setQueryHint("搜索笔记");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_clear:
                showWarning();
                break;
        }
        return true;
    }

    private void showWarning(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("确定要清空所有记事本？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.open();
                helper.removeAllNote(noteList);
                helper.close();
                refreshView();
            }
        });
        builder.setNegativeButton("取消",(dialog,which)->{

        });
        builder.show();
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int opcode=data.getIntExtra("opcode",OPCODE_NEW_NOTE);

        String content=data.getStringExtra("content");
        if(opcode!=OPCODE_DELETE_NOTE&&(content==null||content.length()==0)){
            opcode=OPCODE_NOT_MODIFY;
        }
        String time=data.getExtras().getString("time");
        long id=data.getLongExtra("id",-1);
        int mode=data.getIntExtra("mode",1);
        Note note=new Note(content,time,mode);
        helper.open();
        switch (opcode){
            case OPCODE_NEW_NOTE:
                helper.addNote(note);
                break;
            case OPCODE_DELETE_NOTE:
                note.setId(id);
                helper.removeNote(note);
                break;
            case OPCODE_MODIFY_NOTE:
                note.setId(id);
                helper.updateNote(note);
                break;
            case OPCODE_NOT_MODIFY:
                break;
            default:
                break;
        }
        helper.close();
        if(opcode!=OPCODE_NOT_MODIFY){
            refreshView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
