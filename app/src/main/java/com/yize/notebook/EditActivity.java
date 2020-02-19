package com.yize.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yize.notebook.util.DefaultParams.OPCODE_NEW_NOTE;

public class EditActivity extends AppCompatActivity {

    private EditText et_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        et_note=(EditText)findViewById(R.id.et_note);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_HOME){

        }else if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent();
            intent.putExtra("content",et_note.getText().toString());
            intent.putExtra("time",getCurrentTime());
            intent.putExtra("opcode",OPCODE_NEW_NOTE);
            setResult(RESULT_OK,intent);
            finish();
        }
        return true;
    }

    public String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
