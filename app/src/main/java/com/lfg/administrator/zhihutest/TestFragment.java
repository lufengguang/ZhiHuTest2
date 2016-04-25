package com.lfg.administrator.zhihutest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment implements View.OnClickListener{
    private TestAdapter testAdapter;
    private List<String> list= new ArrayList<>();
    private  String  msg;
    private Activity  activity;
    private ListView listView;
    private View rootView;
    private SwipeRefreshLayout swipeLayout;
    private int firstVisibleItem;  //第一个可见Item
    private boolean onRefresh;    //记录下拉刷新状态
    private int startY;             //记录开始下拉Y坐标  -1表示没开始下滑
    private int oldDistanceY;      //记录上一次滑动后距离作为 deltaY值
    private int nowDistanceY;     //当前记录距离
    private int lastNowDistanceY; //符合按钮滑动范围的最后的距离
    private int startTranslateRawY;//记录按钮开始滑动初始Ｙ坐标
    private int oldRawY;           //记录上一次的Y坐标
    private boolean panel_is_open; //记录背景显示状态
    private boolean isRecord;  //标记Ｙ坐标是否有记录，即是否符合按钮滑动状态
    private View panel_bg_rl;   //
    private ImageView  abc_btn_show_iv;
    private boolean btn_is_hide;  //记录 按钮当前状态  true 为隐藏
    private int distanceRangeY;  //按钮向下滑动最大距离
    private RotateAnimation rotateAnimation2left;  //按钮逆时针旋转45度动画
    private RotateAnimation rotateAnimation2right;//按钮顺时针滑动45度动画
    public TestFragment(){

    }
    public static TestFragment newInstance() {
        return new TestFragment();
    }
    public void setMsg(String msg){
        this.msg=msg;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView=inflater.inflate(R.layout.fragment_test, container, false);;

        initDate();
        initView();
        initEvent();
        return rootView;
    }

   private void initDate(){
         activity=getActivity();
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         list.add(msg);
         panel_is_open=false;
         isRecord=false;
         btn_is_hide=false;
         onRefresh=false;
         startY=-1;
         testAdapter=new TestAdapter(getActivity(),list);
         distanceRangeY=DisplayUtils.dp2px(activity,104);
         rotateAnimation2left=new RotateAnimation(0, -45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         rotateAnimation2left.setFillAfter(true);
         rotateAnimation2right=new RotateAnimation(-45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         rotateAnimation2right.setFillAfter(true);
   }
  private void initRefresh(){
      onRefresh=false;
      swipeLayout.setRefreshing(false);
      startY=-1;
  }

  private void initView(){
     listView=(ListView) rootView.findViewById(R.id.test_lv);
     listView.setAdapter(testAdapter);
     swipeLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
     swipeLayout.setColorSchemeResources(
             R.color.c1,
             R.color.c2,
             R.color.c3);
      swipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
  /*    swipeLayout.setProgressViewOffset(false, 0, (int) TypedValue   //首次进入页面加载进度条
              .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                      .getDisplayMetrics()));*/
      swipeLayout.setProgressViewOffset(false,-8,120);
      swipeLayout.setProgressViewEndTarget(false, 120);
      panel_bg_rl=(View)rootView.findViewById(R.id.panel_bg_rl);
      abc_btn_show_iv=(ImageView) rootView.findViewById(R.id.abc_btn_show_iv);
  }

  private void initEvent(){

         listView.setOnScrollListener(new AbsListView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(AbsListView view, int scrollState) {

             }
             @Override
             public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                 if(listView.getChildAt(0)==null) return ;
                 if(firstVisibleItem==0&&listView.getChildAt(0).getTop()!=0){
                     swipeLayout.setEnabled(false);
                 }
                 else {
                     swipeLayout.setEnabled(true);
                 }
             }
         });

      listView.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              switch (event.getAction()) {
                  case MotionEvent.ACTION_DOWN:
                      oldRawY = (int) event.getRawY();
                      break;
                  case MotionEvent.ACTION_MOVE:
                      int nowRawY = (int) event.getRawY();

                      /**
                       * 按钮下滑部分
                       */

                      if (btn_is_hide && nowRawY > oldRawY && !isRecord) {
                          isRecord = true;
                          startTranslateRawY = nowRawY ; //-1防止首次判断的时候被认为滑动达到或超距离
                          oldDistanceY = distanceRangeY;
                      } else if (!btn_is_hide && nowRawY < oldRawY && !isRecord) {
                          isRecord = true;
                          startTranslateRawY = nowRawY; //+1防止首次判断的时候被认为滑动达到或超距离
                          oldDistanceY = 0;
                      }

                      if (isRecord) {
                          nowDistanceY = nowRawY - startTranslateRawY;

                          if (btn_is_hide && nowDistanceY <=distanceRangeY && nowDistanceY >= 0) {        //当按钮隐藏，并开始下滑，且没超出滑动范围
                              TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, oldDistanceY, distanceRangeY - nowDistanceY);
                              translateAnimation.setFillAfter(true);
                              abc_btn_show_iv.startAnimation(translateAnimation);
                              oldDistanceY = distanceRangeY - nowDistanceY;
                              lastNowDistanceY = oldDistanceY;

                          } else if (!btn_is_hide && nowDistanceY <= 0 && nowDistanceY >=-distanceRangeY) {    //当按钮显示并向上滑动，且没超出范围
                              TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, oldDistanceY, -nowDistanceY);
                              translateAnimation.setFillAfter(true);
                              abc_btn_show_iv.startAnimation(translateAnimation);
                              oldDistanceY = -nowDistanceY;
                              lastNowDistanceY = nowDistanceY;
                          } else if (btn_is_hide && nowDistanceY < 0) {                                //当按钮隐藏并向下滑动超出范围，隐藏按钮并退出此次滑动
                              TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, distanceRangeY, distanceRangeY);
                              translateAnimation.setFillAfter(true);
                              abc_btn_show_iv.startAnimation(translateAnimation);
                              isRecord = false;
                              btn_is_hide = true;
                          } else if (btn_is_hide && nowDistanceY >distanceRangeY) {              //当按钮隐藏并且向上滑动超出范围 ，显示按钮并退出此次按钮滑动
                              TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0);
                              translateAnimation.setFillAfter(true);
                              abc_btn_show_iv.startAnimation(translateAnimation);
                              isRecord = false;
                              btn_is_hide = false;
                          } else if (!btn_is_hide && nowDistanceY >0) {                          //当按钮显示且向下滑动超出范围，显示按钮并退出此次按钮滑动
                              TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0);
                              translateAnimation.setFillAfter(true);
                              abc_btn_show_iv.startAnimation(translateAnimation);
                              isRecord = false;
                              btn_is_hide = false;
                          } else if (!btn_is_hide && nowDistanceY < -nowDistanceY) {         //当按钮显示并向上滑动超出范围，隐藏按钮并退出此次按钮滑动
                              TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, distanceRangeY, distanceRangeY);
                              translateAnimation.setFillAfter(true);
                              abc_btn_show_iv.startAnimation(translateAnimation);
                              isRecord = false;
                              btn_is_hide = true;
                          }
                      }
                      oldRawY = nowRawY;

                      break;
                  case MotionEvent.ACTION_UP:
                      //按钮下拉部分
                      if (isRecord) {
                          TranslateAnimation translateAnimation;
                          nowDistanceY = lastNowDistanceY;
                          if (nowDistanceY > 0 && nowDistanceY <2* distanceRangeY / 3) {          //隐藏状态下滑超过1/3
                              translateAnimation = new TranslateAnimation(0, 0, nowDistanceY, 0);
                              btn_is_hide = false;
                          } else if (nowDistanceY > 0 && nowDistanceY >= 2* distanceRangeY / 3) {        //隐藏状态下滑没超过1/3
                              translateAnimation = new TranslateAnimation(0, 0, nowDistanceY, distanceRangeY);
                              btn_is_hide = true;
                          } else if (nowDistanceY < 0 && -nowDistanceY > distanceRangeY / 3) {             //显示状态上滑动超过1/3
                              translateAnimation = new TranslateAnimation(0, 0, -nowDistanceY, distanceRangeY);
                              btn_is_hide = true;
                          } else {
                              translateAnimation = new TranslateAnimation(0, 0, -nowDistanceY, 0);    //显示状态上滑动没超过1/3
                              btn_is_hide = false;
                          }
                          translateAnimation.setFillAfter(true);
                          translateAnimation.setDuration(500);
                          abc_btn_show_iv.startAnimation(translateAnimation);
                          isRecord = false;
                      }
                  //    swipeLayout.setRefreshing(true);
                      onRefresh=false;
                      startY=-1;
                      break;
              }
              return false;
          }
      });

      swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {

              swipeLayout.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      initRefresh();
                  }
              },3000);
          }
      });
      abc_btn_show_iv.setOnClickListener(this);
      panel_bg_rl.setOnClickListener(this);
  }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.panel_bg_rl:
              panel_bg_rl.setVisibility(View.GONE);
              panel_is_open=false;
              abc_btn_show_iv.startAnimation(rotateAnimation2right                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           );
              break;
          case R.id.abc_btn_show_iv:
               if(panel_is_open){
                   panel_bg_rl.setVisibility(View.GONE);
                   panel_is_open=false;
                   abc_btn_show_iv.startAnimation(rotateAnimation2right);
               }else {
                   panel_bg_rl.setVisibility(View.VISIBLE);
                   panel_is_open=true;
                   abc_btn_show_iv.startAnimation(rotateAnimation2left);
               }
              break;
      }
    }

 }
