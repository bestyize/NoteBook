package com.yize.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yize.notebook.util.DefaultParams.OPCODE_DELETE_NOTE;
import static com.yize.notebook.util.DefaultParams.OPCODE_MODIFY_NOTE;
import static com.yize.notebook.util.DefaultParams.OPCODE_NEW_NOTE;
import static com.yize.notebook.util.DefaultParams.OPCODE_NOT_MODIFY;
import static com.yize.notebook.util.DefaultParams.OPEN_MODE_MODIFY;
import static com.yize.notebook.util.DefaultParams.OPEN_MODE_NEW;

public class EditActivity extends AppCompatActivity {
    private Toolbar editToolBar;
    private EditText et_note;
    private int opcode=OPCODE_NOT_MODIFY;
    private Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        viewInit();
        dataInit();
    }

    private void viewInit(){
        et_note=(EditText)findViewById(R.id.et_note);
        editToolBar=(Toolbar)findViewById(R.id.editToolBar);
        setSupportActionBar(editToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editToolBar.setNavigationOnClickListener((v)->{
            beforeReturnToMain();
        });

    }

    public void dataInit(){
        Intent intent=getIntent();
        int openMode=intent.getIntExtra("openMode",0);
        if(openMode==OPEN_MODE_MODIFY){
            opcode=OPEN_MODE_MODIFY;
            note=(Note)intent.getSerializableExtra("note");
            et_note.setText(note.getContent());
        }else if(openMode==OPEN_MODE_NEW){
            opcode=OPEN_MODE_NEW;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit_delele:
                opcode=OPCODE_DELETE_NOTE;

                beforeReturnToMain();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_HOME){

        }else if(keyCode==KeyEvent.KEYCODE_BACK){
            beforeReturnToMain();
        }
        return true;
    }

    private void beforeReturnToMain(){
        if(note!=null&&(note.getContent().equals(et_note.getText().toString())&&note.getContent().length()>0)&&opcode!=OPCODE_DELETE_NOTE){
            opcode=OPCODE_NOT_MODIFY;
        }
        Intent intent=new Intent();
        intent.putExtra("content",et_note.getText().toString());
        intent.putExtra("time",getCurrentTime());
        intent.putExtra("opcode",opcode);
        if(note!=null&&opcode==OPCODE_MODIFY_NOTE||opcode==OPCODE_DELETE_NOTE){
            intent.putExtra("id",note.getId());
            if(et_note.getText().toString().length()==0){
                intent.putExtra("opcode",OPCODE_DELETE_NOTE);
                Log.i("测试","进入删除");
            }
        }
        setResult(RESULT_OK,intent);
        finish();
    }

    public String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
