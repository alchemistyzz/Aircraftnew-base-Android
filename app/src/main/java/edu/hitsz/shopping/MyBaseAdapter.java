package edu.hitsz.shopping;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import edu.hitsz.R;

public class MyBaseAdapter extends BaseAdapter {
    private String[] titles={"无敌道具","减速道具","炸弹道具"};
    private String[] prices={"100","30","50"};
    private Context context;
    private String[] cnt ={"1","2","3"};
    private  int[] icons={R.drawable.prop_lift, R.drawable.prop_froze, R.drawable.prop_bomb};

    public MyBaseAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount(){       //得到item的总数
        return titles.length;    //返回ListView Item条目代表的对象
    }

    @Override
    public Object getItem(int position){
        return titles[position]; //返回item的数据对象
    }
    @Override
    public long getItemId(int position){
        return position;         //返回item的id
    }

    public void setCnt(String[] cnt) {
        this.cnt = cnt;
    }
    public int getPrice(int position){
        return Integer.parseInt(prices[position]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){//获取item中的View视图
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.shopping_item, null);
            holder=new ViewHolder();
            holder.title=convertView.findViewById(R.id.title);
            holder.price=convertView.findViewById(R.id.price);
            holder.iv=convertView.findViewById(R.id.iv_shopping);
            holder.cnt=convertView.findViewById(R.id.prop_cnt);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.title.setText(titles[position]);
        holder.price.setText(prices[position]);
        holder.iv.setImageResource(icons[position]);
        holder.cnt.setText(cnt[position]);
        return convertView;
    }

}
