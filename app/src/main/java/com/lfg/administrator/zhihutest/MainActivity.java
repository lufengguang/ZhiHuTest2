package com.lfg.administrator.zhihutest;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity
        implements View.OnClickListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
 ;
    private List<TestFragment> fragmentList;
    private ImageView left_iv;
    private TextView title_tv;
  //  private CharSequence mTitle;
    private DrawerLayout drawerLayout;
    private ListView drawer_lv;
    private DrawerAdapter drawerAdapter;
    private List<String> title_list;
    private boolean drawerIsOpen;
    private int startX;
    private int nowPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initDate();
       initView();
       initEvent();

        onNavigationDrawerItemSelected(0);
    }

   private void initDate(){
       title_list=  new ArrayList<String>();
       title_list.add("首页");
       title_list.add("发现");
       title_list.add("关注");
       drawerAdapter=new DrawerAdapter(this,title_list);
       fragmentList=new ArrayList<>(3);
       for (int i=0;i<3;i++) {
           TestFragment fragment=TestFragment.newInstance();
           fragment.setMsg(title_list.get(i)+"fragment");
           fragmentList.add(fragment);
       }
       drawerIsOpen=false;
       nowPosition=0;
   }
    private void initView(){

        left_iv=(ImageView)findViewById(R.id.left_iv);
        title_tv=(TextView) findViewById(R.id.title_tv);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_lv=(ListView) findViewById(R.id.drawer_lv);
        drawer_lv.setAdapter(drawerAdapter);
    }
    private void initEvent(){

        drawerAdapter.setOnItemClickListener(new DrawerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                drawerLayout.closeDrawer(drawer_lv);
                nowPosition=position;
                onNavigationDrawerItemSelected(position);
            }
        });
        drawer_lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                       if(drawerIsOpen){
                           startX=(int)event.getRawX();
                       }
                        break;
                    case MotionEvent.ACTION_MOVE:
                       if(drawerIsOpen){
                           drawer_lv.setPadding((int)(event.getRawX()-startX),0,0,0);
                           return true;
                       }

                        break;

                    case MotionEvent.ACTION_UP:
                       if(drawerIsOpen){
                           if(startX-event.getRawX()>30){
                               drawerLayout.closeDrawer(drawer_lv);
                               drawerIsOpen=false;
                           }else {
                               drawer_lv.setPadding(0,0,0,0);
                           }
                           return true;
                       }
                        break;

                }

                return false;
            }
        });
        left_iv.setOnClickListener(this);

    }

    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragmentList.get(position))
                .addToBackStack(null)
                .commitAllowingStateLoss();

        switch (position) {
            case 0:
                title_tv.setText(title_list.get(position));
                break;
            case 1:
                title_tv.setText(title_list.get(position));
                break;
            case 2:
                title_tv.setText(title_list.get(position));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_iv:
                drawerLayout.openDrawer(drawer_lv);
                drawerIsOpen=true;
                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(nowPosition!=0){
                onNavigationDrawerItemSelected(0);
                nowPosition=0;
            }else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
                intent.addCategory(Intent.CATEGORY_HOME);
                this.startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
