package edu.hitsz.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.hitsz.R;

public class RankListAdapter extends BaseAdapter {
    private final List<RankList> rankLists;
    Context context;

    public RankListAdapter(List<RankList> rankLists, Context context) {
        this.rankLists = rankLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return rankLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.ranklist_item, parent, false);

        RankList rankList = rankLists.get(position);
        @SuppressLint("ResourceType")
        TextView list_name = convertView.findViewById(R.id.list_name);
        TextView list_rank = convertView.findViewById(R.id.list_rank);
        TextView list_time = convertView.findViewById(R.id.list_time);
        TextView list_score = convertView.findViewById(R.id.list_score);
        list_name.setText(rankList.getUser_name());
        list_score.setText(rankList.getScore()+"");
        list_rank.setText(position+1 + "");
        list_time.setText(rankList.getTime());


        return convertView;
    }
}
