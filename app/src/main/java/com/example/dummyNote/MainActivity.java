package com.example.dummyNote;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
                mDB.create("項目" + (++count));
                //從資料庫取得最新cursor後，更換SimpleCursorAdapter的cursor
                //因為SimpleCursorAdapter是參照物件，因此這邊改變cursor會直接更新listView
                sca.changeCursor(getCursor());
                //挑出提示
                Toast.makeText(context, "新增:" + "項目" + count, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete_item:
                //TODO 刪除項目
                long id_delete = 3;
                if(mDB.delete(id_delete)){
                    //從資料庫取得最新cursor後，更換SimpleCursorAdapter的cursor
                    //因為SimpleCursorAdapter是參照物件，因此這邊改變cursor會直接更新listView
                    sca.changeCursor(getCursor());
                    //挑出提示
                    Toast.makeText(context, "刪除:" + "項目" + id_delete, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "刪除失敗", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_update_item:
                //TODO 更新項目
                long id_update = 5;
                if(mDB.update(id_update, "更新項目")){
                    //從資料庫取得最新cursor後，更換SimpleCursorAdapter的cursor
                    //因為SimpleCursorAdapter是參照物件，因此這邊改變cursor會直接更新listView
                    sca.changeCursor(getCursor());
                    //挑出提示
                    Toast.makeText(context, "更新:" + "項目" + id_update, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "更新失敗", Toast.LENGTH_SHORT).show();
                }
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
}