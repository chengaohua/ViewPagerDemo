package com.example.ryan.viewpagerdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity需要继承FragmentActivity
 */
public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    //Tab显示内容TextView
    private TextView mainTv, momentTv, settingTv;
    //Tab的那个引导线
    private ImageView tablineIv;

    //三个Fragment页面
    private MainFragment mainFg;
    private MomentFragment momentFg;
    private SettingFragment settingFg;

    //ViewPager的当前选中页
    private int currentIndex;

    //屏幕的宽度
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findById();
        init();
        initTabLineWidth();

    }

    /**
     * 找到控件ID
     */
    private void findById() {
        mainTv = (TextView) this.findViewById(R.id.mainTv);
        momentTv = (TextView) this.findViewById(R.id.momentTv);
        settingTv = (TextView) this.findViewById(R.id.settingTv);
        tablineIv = (ImageView) this.findViewById(R.id.iv_tabline);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
    }

    /**
     * 初始化ViewPager和设置监听器
     */
    private void init() {
        mainFg = new MainFragment();
        momentFg = new MomentFragment();
        settingFg = new SettingFragment();
        //将三个页面添加到容器里面
        mFragmentList.add(mainFg);
        mFragmentList.add(momentFg);
        mFragmentList.add(settingFg);

        //重写一个FragmentAdapter继承FragmentPagerAdapter，需要传FragmentManager和存放页面的容器过去
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        //ViewPager绑定监听器
        viewPager.setAdapter(mFragmentAdapter);
        //ViewPager设置默认当前的项
        viewPager.setCurrentItem(0);
        //ViewPager设置监听器，需要重写onPageScrollStateChanged，onPageScrolled，onPageSelected三个方法
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             * 三个方法的执行顺序为：用手指拖动翻页时，最先执行一遍onPageScrollStateChanged（1），
             * 然后不断执行onPageScrolled，放手指的时候，直接立即执行一次onPageScrollStateChanged（2），
             * 然后立即执行一次onPageSelected，然后再不断执行onPageScrolled，
             * 最后执行一次onPageScrollStateChanged（0）。
             */

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("PageScroll：", "onPageScrollStateChanged" + ":" + state);
            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tablineIv.getLayoutParams();
                Log.i("mOffset", "offset:" + offset + ",position:" + position);
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                tablineIv.setLayoutParams(lp);
            }

            /**
             * 将当前选择的页面的标题设置字体颜色为蓝色
             */
            @Override
            public void onPageSelected(int position) {
                Log.i("PageScroll：", "onPageSelected" + ":" + position);
                resetTextView();
                switch (position) {
                    case 0:
                        mainTv.setTextColor(Color.BLUE);
                        break;
                    case 1:
                        momentTv.setTextColor(Color.BLUE);
                        break;
                    case 2:
                        settingTv.setTextColor(Color.BLUE);
                        break;
                }
                currentIndex = position;
            }
        });

    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tablineIv.getLayoutParams();
        lp.width = screenWidth / 3;
        tablineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mainTv.setTextColor(Color.BLACK);
        momentTv.setTextColor(Color.BLACK);
        settingTv.setTextColor(Color.BLACK);
    }

}
