package com.example.dummyNote;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Context context;

    private AppBarConfiguration mAppBarConfiguration;
    //資料庫成員變數
    private DB mDB;
    //取資料用的cursor
    private Cursor cursor;
    //
    private long index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //取得資料庫
        mDB = new DB(context);
        mDB.open();
    }

    //建立menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //監聽menu
    int count = 0;
    SimpleCursorAdapter sca;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                //新增項目
                //建立彈出視窗給使用者輸入修改內容
                //建立一個AlertDialog.Builder
                AlertDialog.Builder addBuilder= new AlertDialog.Builder(context);
                //取得View，這裡設定為 res 中的 layout 中的 my_adddialog.xml
                final View myAddDialog = getLayoutInflater().inflate(R.layout.my_adddialog, null);
                //設定AlertDialog.Builder的View
                addBuilder.setView(myAddDialog);
                //建立並顯示對話框
                final AlertDialog addDialog = addBuilder.show();

                //取得myDialog中的按鈕
                Button btnAdd = myAddDialog.findViewById(R.id.btn_confirm);

                //設定按鈕監聽器
                btnAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText etInput = myAddDialog.findViewById(R.id.et_input);
                        Log.d("mTest", "onClick: " + etInput);
                        //本地變數，OK
                        String input = etInput.getText().toString();

                        Log.d("mTest", "已輸入:" + input);

                        //更新
                        if(mDB.create(input) > 0){
                            //從資料庫取得最新cursor後，更換SimpleCursorAdapter的cursor
                            //因為SimpleCursorAdapter是參照物件，因此這邊改變cursor會直接更新listView
                            sca.changeCursor(getCursor());
                            //挑出提示
                            Toast.makeText(context, "新增:" + input, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "新增失敗", Toast.LENGTH_SHORT).show();
                        }

                        //關閉對話框
                        addDialog.dismiss();
                    }
                });

                return true;
            case R.id.action_delete_item:
                //刪除項目
                if(mDB.delete(index)){
                    //從資料庫取得最新cursor後，更換SimpleCursorAdapter的cursor
                    //因為SimpleCursorAdapter是參照物件，因此這邊改變cursor會直接更新listView
                    sca.changeCursor(getCursor());
                    //挑出提示
                    Toast.makeText(context, "刪除:" + "項目" + index, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "刪除失敗", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_update_item:
                //更新項目

                //建立彈出視窗給使用者輸入修改內容
                //建立一個AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //取得View，這裡設定為 res 中的 layout 中的 my_alertdialog.xml
                final View myDialog = getLayoutInflater().inflate(R.layout.my_alertdialog, null);
                //設定AlertDialog.Builder的View
                builder.setView(myDialog);
                //建立並顯示對話框
                final AlertDialog dialog = builder.show();

                //取得myDialog中的按鈕
                Button btn = myDialog.findViewById(R.id.btn_confirm);

                //設定按鈕監聽器
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText etInput = myDialog.findViewById(R.id.et_input);
                        Log.d("mTest", "onClick: " + etInput);
                        //本地變數，OK
                        String input = etInput.getText().toString();


//                        Toast.makeText(context, "已輸入:" + input, Toast.LENGTH_SHORT).show();
                        Log.d("mTest", "已輸入:" + input);

                        //更新
                        if(mDB.update(index, input)){
                            //從資料庫取得最新cursor後，更換SimpleCursorAdapter的cursor
                            //因為SimpleCursorAdapter是參照物件，因此這邊改變cursor會直接更新listView
                            sca.changeCursor(getCursor());
                            //挑出提示
                            Toast.makeText(context, "更新:" + "項目" + index, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "更新失敗", Toast.LENGTH_SHORT).show();
                        }

                        //關閉對話框
                        dialog.dismiss();
                    }
                });
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //??
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //向資料庫取得資料cursor
    public Cursor getCursor() {
        //取得資料，存於cursor
        Cursor cursor = mDB.getAll();
        return cursor;
    }

    //用來讓Fragment設定MainActivity的adapter
    public void setAdapter(SimpleCursorAdapter sca){
        this.sca = sca;
    }

    //用來讓Fragment設定MainActivity的項目索引
    public void setIndex(long index){
        this.index = index;
    }
}