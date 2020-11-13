package com.example.dummyNote.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dummyNote.MainActivity;
import com.example.dummyNote.R;

public class HomeFragment extends Fragment {
    private Context context;
    private HomeViewModel homeViewModel;
    private ListView listViewHome;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity)getActivity();
        context = activity;

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        //填充片段
        //引數一為要用的 layout
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //以root找到id
        listViewHome = root.findViewById(R.id.listview_home);

        setAdapter();

        //建立listView監聽器
        listViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //把資料庫ID傳入
                TextView tvID = view.findViewById(R.id.tv1);
                long item_id = Long.parseLong(tvID.getText().toString());

                Toast.makeText(context, "項目" + item_id, Toast.LENGTH_SHORT).show();
                //將MainActivity的項目索引值更新
                activity.setIndex(item_id);
            }
        });

        return root;
    }

    private void setAdapter(){
        //取得MainActivity
        MainActivity activity = (MainActivity)getActivity();

        //以MainActivity之方法取得cursor
        Cursor cursor = activity.getCursor();
        //要查詢的欄位
        String[] from = {"_id", "note", "created"};
        //要放入的view
        int[] to = {R.id.tv1, R.id.tv2, R.id.tv3};

        //建立適配器
        SimpleCursorAdapter sca = new SimpleCursorAdapter(
                //context
                getActivity(),
                //Adapter中各項目要用的外觀
                R.layout.item_view,
                //資料源
                cursor,
                //資料表欄位
                from,
                //該欄位資料要填進的view
                to,
                //??
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        //設定MainActivity的adapter
        activity.setAdapter(sca);

        //連接Adapter
        listViewHome.setAdapter(sca);
    }
}