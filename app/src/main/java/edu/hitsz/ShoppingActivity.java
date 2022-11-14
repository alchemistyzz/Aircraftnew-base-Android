package edu.hitsz;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.hitsz.data.Database;
import edu.hitsz.data.User;
import edu.hitsz.shopping.MyBaseAdapter;

public class ShoppingActivity extends AppCompatActivity {
    private ListView shoppingListView;
    private Database database;
    private User user;
    private TextView userView;
    private TextView moneyView;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏掉系统原先的导航栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shopping);

        database=Database.getInstance();
        user = database.getUserById(LaunchActivity.USER_ID);
        //初始化ListView控件
        shoppingListView=findViewById(R.id.shopping_list_view);
        //初始化用户textview
        userView=findViewById(R.id.user_view);
        userView.setText(""+user.getName()+",您当前的积分为：");
        //初始化用户积分
        moneyView=findViewById(R.id.money_view);
        moneyView.setText(user.getMoney()+"");
        //创建一个Adapter的实例
        MyBaseAdapter mAdapter=new MyBaseAdapter(ShoppingActivity.this);
        mAdapter.setCnt(new String[]{user.getSuper_prop_cnt()+"", user.getFroze_prop_cnt()+"", user.getBomb_prop_cnt()+""});
        //设置Adapter
        shoppingListView.setAdapter(mAdapter);
        shoppingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                user = database.getUserById(LaunchActivity.USER_ID);
                if(position == 0){
                    if(user.getMoney()>mAdapter.getPrice(position)){
                        user.setSuper_prop_cnt(user.getSuper_prop_cnt()+1);
                        user.setMoney(user.getMoney()-mAdapter.getPrice(position));
                    }
                    else System.out.println("积分不足，购买失败");

                }
                else if(position == 1){
                    if(user.getMoney()>mAdapter.getPrice(position)){
                        user.setFroze_prop_cnt(user.getFroze_prop_cnt()+1);
                        user.setMoney(user.getMoney()-mAdapter.getPrice(position));
                    }
                    else System.out.println("积分不足，购买失败");
                }
                else if(position == 2){
                    if(user.getMoney()>mAdapter.getPrice(position)){
                        user.setBomb_prop_cnt(user.getBomb_prop_cnt()+1);
                        user.setMoney(user.getMoney()-mAdapter.getPrice(position));
                    }
                    else System.out.println("积分不足，购买失败");
                }
                database.updateUser(user);
                mAdapter.setCnt(new String[]{user.getSuper_prop_cnt()+"", user.getFroze_prop_cnt()+"", user.getBomb_prop_cnt()+""});
                shoppingListView.setAdapter(mAdapter);
                moneyView.setText(user.getMoney()+"");
                //重新写回数据
                System.out.println("无敌道具数目："+user.getSuper_prop_cnt());
                System.out.println("减速道具数目："+user.getFroze_prop_cnt());
                System.out.println("炸弹道具数目："+user.getBomb_prop_cnt());



                return true;
            }
        });


        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        //设置app logo
        //toolbar.setSubtitleTextColor(Integer.parseInt("#C71585"));
        //设置右上角的填充菜单 说白了就是添加一个选项菜单
        toolbar.inflateMenu(R.menu.menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
