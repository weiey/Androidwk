package com.weiey.app.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.weiey.app.AppManager;
import com.weiey.app.R;
import com.weiey.app.activities.base.BaseActivity;
import com.weiey.app.adapter.CommonAdapter;
import com.weiey.app.adapter.VPFragmentAdapter;
import com.weiey.app.adapter.ViewHolder;
import com.weiey.app.utils.ToastUtil;
import com.weiey.app.view.XViewPager;

import java.util.ArrayList;

/**
 * @author zw
 * @Description: 首页
 * @date 2019/11/6
 */
public class HomeActivity extends BaseActivity {




    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    /**
     * 设置当前页面不可侧滑
     *
     * @return
     */
    @Override
    protected boolean isSwipeBackEnable() {
        return false;
    }
    @BindView(R.id.home_container)
    XViewPager homeContainer;
    @BindView(R.id.home_navigation_list)
    ListView homeNavigationList;
    @BindView(R.id.home_drawer)
    DrawerLayout homeDrawer;


    ArrayList<Fragment> fragments = new ArrayList<>();
    ActionBarDrawerToggle mActionBarDrawerToggle = null;
    CommonAdapter mNavListAdapter;

    private int mCurrentMenuCheckedPos = 0;

    @Override
    protected void init() {
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, homeDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        homeDrawer.addDrawerListener(mActionBarDrawerToggle);
        if (null != fragments && !fragments.isEmpty()) {
            homeContainer.setEnableScroll(false);
            homeContainer.setOffscreenPageLimit(fragments.size());
            homeContainer.setAdapter(new VPFragmentAdapter(getSupportFragmentManager(), fragments));
        }

        mNavListAdapter = new CommonAdapter<String>(this,R.layout.list_item_navigation) {
            @Override
            public void convert(ViewHolder holder, String title) {
                ImageView itemIcon = holder.getView(R.id.list_item_navigation_icon);
                TextView itemName = holder.getView(R.id.list_item_navigation_name);
                itemName.setText(title);
            }
        };

        ArrayList<String> navigationList = new ArrayList<>();

        homeNavigationList.setAdapter(mNavListAdapter);
        mNavListAdapter.setData(navigationList);
        mNavListAdapter.notifyDataSetChanged();

        homeNavigationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentMenuCheckedPos = position;
                mNavListAdapter.notifyDataSetChanged();
                homeDrawer.closeDrawer(Gravity.LEFT);
                homeContainer.setCurrentItem(mCurrentMenuCheckedPos, false);
            }
        });
    }

    @Override
    protected View getLoaingTargetView() {
        return null;
    }


    private static long DOUBLE_CLICK_TIME = 0L;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            if (homeDrawer.isDrawerOpen(Gravity.LEFT)) {
                homeDrawer.closeDrawer(Gravity.LEFT);
            } else {
                homeDrawer.openDrawer(Gravity.LEFT);
            }
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (homeDrawer.isDrawerOpen(Gravity.LEFT)) {
                homeDrawer.closeDrawer(Gravity.LEFT);
            } else {
                if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                    ToastUtil.show("再按次退出应用");
                    DOUBLE_CLICK_TIME = System.currentTimeMillis();
                } else {
                    AppManager.getInstance().exitApp(getApplicationContext());
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
