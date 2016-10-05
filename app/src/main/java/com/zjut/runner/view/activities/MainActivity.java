package com.zjut.runner.view.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.RunnableManager;
import com.zjut.runner.view.fragments.BaseFragment;
import com.zjut.runner.view.fragments.MainPageFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener {

    public static final String tag = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout = null;
    private ActionBarDrawerToggle drawerToggle = null;
    private Toolbar toolbar = null;
    private FloatingActionButton floatingActionButton = null;
    private NavigationView navigationView = null;
    private FrameLayout content = null;

    protected BaseFragment currentFragment = null;
    private MainPageFragment mainPageFragment;
    protected FragmentManager fragmentManager = null;
    private List<BaseFragment> topFragments = new ArrayList<>();
    protected FragmentTransaction transaction = null;

    private static final long SEARCH_WAIT_TIME = 400;
    private SearchRunnable searchRunnable = new SearchRunnable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutId = R.layout.activity_main;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViews() {
        super.findViews();
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        initDrawerLayout();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        goToMainPageFragment();
    }

    private void initDrawerLayout(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            public void onDrawerClosed(View view){
              setHomeMenuVisible(true);
            }
            public void onDrawerOpened(View drawerView){
                setHomeMenuVisible(false);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    protected void setHomeMenuVisible(boolean visible) {
        if (!isHome()) {
            return;
        }
    }

    public boolean isHome() {
        return currentFragment == mainPageFragment;
    }

    public void goToMainPageFragment(){
        if(currentFragment != null && currentFragment == mainPageFragment){
            return;
        }
        if(mainPageFragment != null){
            fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return;
        }
        mainPageFragment = new MainPageFragment();
        skipToFragmentByContentId(mainPageFragment,R.id.content,false, Constants.POP_TO_HOME,0,0);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(!actionBarClick()){
            super.onBackPressed();
        }
        overridePendingTransition(R.animator.back_in,R.animator.back_out);
    }

    public void closeDrawers() {
        if (isDrawerOpen()) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(needLeftDrawer() && drawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                actionBarClick();
                break;
            default:
                break;
        }
        if (currentFragment != null) {
            if (currentFragment.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public synchronized boolean actionBarClick() {
        if (currentFragment != null && currentFragment.onBackPressed()) {
            return true;
        }
        if (topFragments.size() > 0) {
            backToTopFragment();
            backToPrevious();
            return true;
        }
        if(currentFragment != mainPageFragment){
            backToPrevious();
            return true;
        }
        return false;
    }

    protected void backToTopFragment() {
        if (topFragments == null) {
            return;
        }
        if (topFragments.size() == 0) {
            return;
        }
        BaseFragment lasttopFragment = topFragments
                .get(topFragments.size() - 1);
        if (lasttopFragment == null) {
            return;
        }
        lasttopFragment.changeTitle();
        onFragmentResume(lasttopFragment);
        lasttopFragment.setDrawerIndicatorEnabled();
        topFragments.remove(lasttopFragment);
    }

    protected boolean needLeftDrawer() {
        if (currentFragment == null) {
            return false;
        }
        return true;
    }

    private void backToPrevious() {
        super.onBackPressed();
        overridePendingTransition(R.animator.slide_up,
                R.animator.slide_down);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Log.d("gallery","gallery");
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(currentFragment != null){
            currentFragment.initMenu(this,menu);
        }
        setSearchViewStatus(menu,null);
        return super.onPrepareOptionsMenu(menu);
    }

    public void setSearchViewStatus(Menu menu, String searchHint) {
        if (menu == null) {
            return;
        }

        MenuItem searchItem = menu.findItem(R.id.action_settings);
        if (searchItem == null) {
            return;
        }

        SearchView sv = (SearchView) searchItem.getActionView();
        searchItem.setOnActionExpandListener(this);
        sv.setIconifiedByDefault(true);
        sv.setOnQueryTextListener(this);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        if (currentFragment != null) {
            currentFragment.onSearchClose();
        }
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        setDrawerLayoutEnabled(enable);
        closeDrawers();
        if (enable)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);// Lift
            // the
            // locking
            // sidebar
        else
            drawerLayout
                    .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// Locking
        // sidebar
        // that
        // it
        // can
        // not
        // drag
    }

    public void setDrawerLayoutEnabled(boolean enable) {
        if (drawerToggle == null) {
            return;
        }
        drawerToggle.setDrawerIndicatorEnabled(enable);
    }

    /**
     * Confirm which one fragment is currently located
     *
     * @param baseFragment
     */
    public synchronized void onFragmentResume(BaseFragment baseFragment) {
        currentFragment = baseFragment;
        invalidateOptionsMenu();// creates call to onPrepareOptionsMenu()
    }

    public void goToFragment(Fragment fragment, boolean isaddback) {
        skipToFragmentByContentId(fragment, R.id.content, isaddback, null);
    }

    public void goToFragment(Fragment fragment) {
        skipToFragmentByContentId(fragment, R.id.content, true, null);
    }

    public void goToFragment(Fragment fragment, boolean isaddback,
                             String backName) {
        skipToFragmentByContentId(fragment, R.id.content, isaddback,
                backName);
    }

    public void goToFragment(Fragment fragment, String backName) {
        skipToFragmentByContentId(fragment, R.id.content, true, backName);
    }

    public void goToSelectFragment(BaseFragment topFragment, Fragment fragment) {
        addSelectedFragment(topFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_up,
                R.animator.slide_down, R.animator.slide_up,
                R.animator.slide_down);
        transaction.add(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void goToSelectFragment(BaseFragment topFragment, Fragment fragment,
                                   String backName) {
        addSelectedFragment(topFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_up,
                R.animator.slide_down, R.animator.slide_up,
                R.animator.slide_down);
        transaction.add(R.id.content, fragment);
        transaction.addToBackStack(backName);
        transaction.commitAllowingStateLoss();
    }

    public void skipToFragmentByContentId(Fragment fragment, int contentId,
                                          boolean addToStack, String backName) {
        skipToFragmentByContentId(fragment, contentId, addToStack, backName,
                R.animator.slide_up, R.animator.slide_down);
    }

    public void skipToFragmentByContentId(Fragment fragment, int contentId,
                                          boolean addToStack, String backName, int moveInAnimateId,
                                          int moveOutAnimateId) {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(moveInAnimateId, moveOutAnimateId,
                R.animator.slide_up, R.animator.slide_down);
        transaction.replace(contentId, fragment);
        if (addToStack) {
            transaction.addToBackStack(backName);
        }
        transaction.commitAllowingStateLoss();
    }

    public void changeCurrentFragmnet(BaseFragment baseFragment) {
        baseFragment.changeTitle();
        onFragmentResume(baseFragment);
    }

    public void goBackByName(String backName) {
        fragmentManager.popBackStack(backName,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void addSelectedFragment(BaseFragment topFragment) {
        if (topFragment == null) {
            return;
        }
        topFragments.add(topFragment);
    }

    private void search(String query) {
        searchRunnable.setSearchString(query);
        RunnableManager.getInstance().postDelayed(searchRunnable,
                SEARCH_WAIT_TIME);
    }

    class SearchRunnable implements Runnable {

        private String searchString;

        public void setSearchString(String searchString) {
            this.searchString = searchString;
        }

        @Override
        public void run() {
            if (currentFragment != null)
                currentFragment.search(searchString);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
