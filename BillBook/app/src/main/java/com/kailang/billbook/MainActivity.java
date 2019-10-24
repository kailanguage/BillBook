package com.kailang.billbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tv_describe, tv_money,tv_info;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private CountAdapter countAdapter;
    private CountViewModel countViewModel;
    private LiveData<List<Count>> counts;
    private List<Count> allCount;
    private SharedPreferences shp;
    private static boolean isFirst=true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        //查找功能
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String pattern = newText.trim();
                counts.removeObservers(MainActivity.this);
                counts = countViewModel.findCountWithPattern(pattern);
                counts.observe(MainActivity.this, new Observer<List<Count>>() {
                    @Override
                    public void onChanged(List<Count> counts) {
                        int temp = countAdapter.getItemCount();
                        countAdapter.setAllCount(counts);
                        if (temp != counts.size()) {
                            countAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_describe = findViewById(R.id.tv_describe);
        tv_money = findViewById(R.id.tv_money);
        tv_info=findViewById(R.id.tvInfo);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        countViewModel = ViewModelProviders.of(this).get(CountViewModel.class);
        recyclerView = findViewById(R.id.recyclerView_count);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        countAdapter = new CountAdapter(countViewModel);
        recyclerView.setAdapter(countAdapter);

        countAdapter.setOnItemClickListener(new CountAdapter.CountClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("count", allCount.get(position).getId());
                startActivity(intent);
            }
        });

        //感知数据库更新，并更新UI
        counts = countViewModel.getAllCountsLive();

        counts.observe(this, new Observer<List<Count>>() {
            @Override
            public void onChanged(List<Count> counts) {
                allCount = counts;
                int tmp = countAdapter.getItemCount();
                countAdapter.setAllCount(counts);
                if (tmp != counts.size())
                    countAdapter.notifyDataSetChanged();
            }
        });

        //左右滑动启动删除功能
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //Item位置移动
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Count countToDelete = allCount.get(viewHolder.getAdapterPosition());
                countViewModel.deleteCount(countToDelete);
                Snackbar.make(findViewById(R.id.activity_main), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countViewModel.insertCount(countToDelete);
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerView);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        if(isFirst) {
            isFirst=false;
            shp = getSharedPreferences(getResources().getString(R.string.shp), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shp.edit();
            if (shp.getAll().isEmpty()) {
                Toast.makeText(MainActivity.this, "第一次登录", Toast.LENGTH_LONG).show();
                editor.putLong("last_time", System.currentTimeMillis());
            } else {
                int c = shp.getInt("count", 1);
                Long t = shp.getLong("last_time", 0);
                if (t != 0 && c != 1) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String str = format.format(new Date(t));
                    Toast.makeText(MainActivity.this, "上次登录时间：" + str + "  " + "登录次数：" + c, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                c++;
                editor.putInt("count", c);
            }
            editor.commit();
        }
    }

}
