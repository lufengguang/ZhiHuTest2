package com.lfg.administrator.zhihutest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class DrawerAdapter extends BaseAdapter{
    private Context context;
    private List<String> list;
    private OnItemClickListener onItemClickListener;
    public DrawerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(context,R.layout.drawer_item,null);
        }
        TextView textView=(TextView)convertView.findViewById(R.id.title_tv);
        textView.setText(list.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
        return convertView;
    }
    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
}
