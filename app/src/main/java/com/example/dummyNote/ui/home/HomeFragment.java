package com.example.dummyNote.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dummyNote.MainActivity;
import com.example.dummyNote.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ListView listViewHome;
//    DB mDB;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mDB = new DB(getActivity());
//        mDB.open();
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //以root找到id
        listViewHome = root.findViewById(R.id.listview_home);
//        //建立資料
//        ArrayList data = new ArrayList();
//        data.add("Android");
//        data.add("Activity");
//        data.add("Java");
//        data.add("ListView");
//        data.add("SQLite");
//        //建立Adapter
//        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, data);
//        //連結Adapter
//        listViewHome.setAdapter(adapter);

        setAdapter();

        //建立listView監聽器
        listViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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