package com.zjut.runner.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.SaveCallback;
import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.LanguageType;
import com.zjut.runner.Model.RefreshType;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.LanguageUtil;
import com.zjut.runner.util.ResourceUtil;
import com.zjut.runner.util.RunnableManager;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.view.fragments.BaseFragment;
import com.zjut.runner.view.fragments.ChangePasswordFragment;
import com.zjut.runner.view.fragments.MainPageFragment;
import com.zjut.runner.view.fragments.MyOrderFragment;
import com.zjut.runner.view.fragments.MyRunListFragment;
import com.zjut.runner.view.fragments.UserProfileFragment;
import com.zjut.runner.widget.CircleImageView;
import com.zjut.runner.widget.MaterialDialog;
import com.zjut.runner.widget.UserHeaderHolder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static com.zjut.runner.util.Constants.REQUEST_IMAGE;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener, UserHeaderHolder.ProfileClick {

    public static final String tag = MainActivity.class.getSimpleName();
    public CampusModel campusModel;

    private DrawerLayout drawerLayout = null;
    private ActionBarDrawerToggle drawerToggle = null;
    protected Toolbar toolbar = null;
    private FloatingActionButton floatingActionButton = null;
    private NavigationView navigationView = null;
    private FrameLayout content = null;
    private LinearLayout ll_nav_header = null;
    private View headerView = null;
    private CircleImageView iv_profile = null;
    private TextView tv_name = null;
    private TextView tv_mobile = null;
    private LinearLayout ll_collapsing = null;
    private ProgressBar progressBar = null;
    private UserHeaderHolder userHeaderHolder = null;
    private AppBarLayout appBarLayout = null;
    public CollapsingToolbarLayout collapsingToolbarLayout = null;

    protected BaseFragment currentFragment = null;
    private MainPageFragment mainPageFragment;
    protected FragmentManager fragmentManager = null;
    private List<BaseFragment> topFragments = new ArrayList<>();
    protected FragmentTransaction transaction = null;

    private static final long SEARCH_WAIT_TIME = 400;
    private SearchRunnable searchRunnable = new SearchRunnable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parseArgument();
        layoutId = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    protected void onNewIntent(Intent intent){
        if(intent == null)
            return;
        Bundle bundle = intent.getExtras();
        if(bundle == null)
            return;
        int notiID = bundle.getInt(Constants.PARAM_STATUS,-1);
        switch (notiID){
            case 1:
            case 3:
                goToFragment(new MyOrderFragment());
                break;
            case 2:
                goToFragment(new MyRunListFragment());
                break;
            default:
                break;
        }
    }

    private void parseArgument(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            campusModel = (CampusModel) bundle.getSerializable(Constants.PARAM_CAMPUS);
            if(StringUtil.isNull(campusModel.getInstallationID())){
                AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            String install = AVInstallation.getCurrentInstallation().getInstallationId();
                            updateToUser(install);
                        }else{
                            ToastUtil.showToast(R.string.push_fail);
                        }
                    }
                });
            }
        }
    }

    private void updateToUser(String install){
        AVUser.getCurrentUser().put(Constants.PARAM_INSTALLATION, install);
        AVUser.getCurrentUser().saveInBackground();
    }

    public void expandToolbar(boolean expand){
        if(expand){
            appBarLayout.setExpanded(expand,expand);
        }else{
            appBarLayout.setExpanded(expand,true);
        }
    }

    @Override
    protected void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void setListeners() {
        floatingActionButton.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle.setToolbarNavigationClickListener(this);
        setHeaderClick();
    }

    protected void setHeaderClick(){
        ll_nav_header.setOnClickListener(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        fragmentManager = getSupportFragmentManager();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);
        //navigation
        initDrawerLayout();
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //collapsing layout
        ll_collapsing = (LinearLayout) findViewById(R.id.ll_collapsing);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        //app bar layout
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(false,true);

        //push
        PushService.setDefaultPushCallback(this,MainActivity.class);
        PushService.subscribe(this,Constants.PARAM_MY_ORDER,MainActivity.class);
        PushService.subscribe(this,Constants.PARAM_MY_RUN,MainActivity.class);

        initHeaderView();
        initFragment();
    }

    public void addHeader(){
        UserHeaderHolder headerHolder = new UserHeaderHolder(this,campusModel,this);
        userHeaderHolder = AddHeader(headerHolder, GeneralUtils.getDimenPx(this,R.dimen.margin_sixty));
    }

    private UserHeaderHolder AddHeader(UserHeaderHolder userHeaderHolder,int marginTop){
        View rootView = userHeaderHolder.getRootView();
        addView(rootView);
        GeneralUtils.setMarginTop(marginTop, rootView);
        return userHeaderHolder;
    }

    protected void addView(View view) {
        if (view == null) {
            return;
        }
        if (ll_collapsing == null) {
            return;
        }
        if(view.getParent() != null){
            ll_collapsing.removeAllViews();
        }
        ll_collapsing.addView(view);
    }

    protected void initHeaderView(){
        headerView = navigationView.getHeaderView(0);
        ll_nav_header = (LinearLayout) headerView;
        iv_profile = (CircleImageView) headerView.findViewById(R.id.imageView);
        tv_name = (TextView) headerView.findViewById(R.id.tv_name);
        tv_mobile = (TextView) headerView.findViewById(R.id.tv_phone);
        if(campusModel.getUrl() != null){
            ImageLoader.getInstance().displayImage(campusModel.getUrl(),iv_profile,GeneralUtils.getOptions());
        }
        tv_name.setText(campusModel.getUsername());
        tv_mobile.setText(campusModel.getMobile());
    }

    protected void initFragment(){
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
        overridePendingTransition(R.animator.back_in,
                R.animator.back_out);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            GeneralUtils.showCallDialog(this);
        } else if (id == R.id.nav_manage) {
            goToFragment(new ChangePasswordFragment());
        } else if (id == R.id.nav_share) {
            showChangeLanguage();
        } else if (id == R.id.nav_send) {
            logOut();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangeLanguage() {
        View layout = getLayoutInflater().inflate(R.layout.dialog_gender, null);
        TextView english = (TextView) layout.findViewById(R.id.tv_name_a);
        TextView chinese = (TextView) layout.findViewById(R.id.tv_name_b);
        TextView title = (TextView) layout.findViewById(R.id.tv_title);
        english.setText(R.string.str_english);
        chinese.setText(R.string.str_chinese);
        title.setText(R.string.str_language);
        final RadioButton rb_english = (RadioButton) layout.findViewById(R.id.rb_female);
        View rl_english = layout.findViewById(R.id.rl_female);
        View rl_chinese = layout.findViewById(R.id.rl_male);
        final RadioButton rb_chinese = (RadioButton) layout.findViewById(R.id.rb_male);
        final MaterialDialog materialDialog = new MaterialDialog(this);
        GeneralUtils.setChecked(this,rb_english, rb_chinese);
        rl_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguage(LanguageType.ENGLISH);
                materialDialog.dismiss();
            }
        });
        rl_chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguage(LanguageType.CHINESE);
                materialDialog.dismiss();
            }
        });
        materialDialog.setView(layout);
        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.show();
    }

    private void saveLanguage(LanguageType languageType) {
        LanguageUtil.changeLanguage(this,languageType);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_CAMPUS,campusModel);
        intent.putExtras(bundle);
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab){
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }else if(v.getId() == R.id.ll_nav_header){
            goToFragment(new UserProfileFragment());
            return;
        }
        actionBarClick();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(currentFragment != null){
            currentFragment.initMenu(this,menu);
        }
        //setSearchViewStatus(menu,null);
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
        transaction.setCustomAnimations(R.animator.back_in,
                R.animator.back_out, R.animator.back_in,
                R.animator.back_out);
        transaction.add(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void goToSelectFragment(BaseFragment topFragment, Fragment fragment,
                                   String backName) {
        addSelectedFragment(topFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.back_in,
                R.animator.back_out, R.animator.back_in,
                R.animator.back_out);
        transaction.add(R.id.content, fragment);
        transaction.addToBackStack(backName);
        transaction.commitAllowingStateLoss();
    }

    public void skipToFragmentByContentId(Fragment fragment, int contentId,
                                          boolean addToStack, String backName) {
        skipToFragmentByContentId(fragment, contentId, addToStack, backName,
                R.animator.back_in, R.animator.back_out);
    }

    public void skipToFragmentByContentId(Fragment fragment, int contentId,
                                          boolean addToStack, String backName, int moveInAnimateId,
                                          int moveOutAnimateId) {
        collapsingToolbarLayout.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(moveInAnimateId,moveOutAnimateId,R.animator.back_in,R.animator.back_out);
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

    @Override
    public void changeProfile() {
        int selectMode = MultiImageSelectorActivity.MODE_SINGLE;
        Intent intent = new Intent(this,MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,false);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,selectMode);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE && data != null && resultCode == Activity.RESULT_OK){
            progressBar.setVisibility(View.VISIBLE);
            String name = AVUser.getCurrentUser().getUsername() + ".jpg";
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            try {
                final AVFile avFile = AVFile.withAbsoluteLocalPath(name, path.get(0));
                avFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            saveToCacheDB(avFile);
                        } else {
                            ToastUtil.showToastShort(getApplicationContext(), e.getMessage());
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToCacheDB(final AVFile avFile) {
        AVUser.getCurrentUser().setFetchWhenSave(true);
        AVUser.getCurrentUser().put(Constants.PARAM_PIC_URL, avFile);
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    campusModel.setUrl(avFile.getThumbnailUrl(false,100,100));
                    CurrentSession.updateProfileWithCache(getApplicationContext(), campusModel);
                    AVUser.getCurrentUser().refreshInBackground(new RefreshCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            progressBar.setVisibility(View.GONE);
                            userHeaderHolder.setProfile(campusModel);
                            refreshNavView(RefreshType.PROFILE,null);
                        }
                    });
                }
            }
        });
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

    public void refreshNavView(RefreshType refreshType, String text){
        switch (refreshType){
            case MOBILE:
                tv_mobile.setText(text);
                break;
            case PROFILE:
                AVFile avFile = AVUser.getCurrentUser().getAVFile(Constants.PARAM_PIC_URL);
                Ion.with(iv_profile)
                        .placeholder(R.drawable.ic_usericon_default)
                        .error(R.drawable.ic_usericon_default)
                        .load(avFile.getThumbnailUrl(false,100,100));
                break;
            case NAME:
                tv_name.setText(text);
                break;
            default:
                break;
        }

    }


    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }

    public void changeTitle(int resID){
        if(collapsingToolbarLayout != null){
            collapsingToolbarLayout.setTitle(getResources().getString(resID));
        }
    }

    public void changeTitle(String title){
        if(StringUtil.isNull(title)){
            return;
        }
        if(collapsingToolbarLayout != null){
            collapsingToolbarLayout.setTitle(title);
        }
    }

    public void removeHeader(){
        ll_collapsing.removeAllViews();
    }
}
