package com.zjut.runner.Controller;

import android.view.Menu;

import com.zjut.runner.Model.MenuType;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.view.fragments.AllOrderFragment;
import com.zjut.runner.view.fragments.BaseFragment;
import com.zjut.runner.view.fragments.MyOrderFragment;
import com.zjut.runner.view.fragments.MyRunListFragment;
import com.zjut.runner.view.fragments.NewRequestFragment;

/**
 * Created by Phuylai on 2016/12/3.
 */

public class FragmentFactory {

    public static BaseFragment getFragment(String param){
        if(StringUtil.isNull(param)){
            return null;
        }
        if(param.equals(MenuType.CREATE.toString())){
            return new NewRequestFragment();
        }else if(param.equals(MenuType.ORDER.toString())){
            return new MyOrderFragment();
        }else if(param.equals(MenuType.HELPS.toString())){
            return new AllOrderFragment();
        }else if(param.equals(MenuType.RUN.toString())){
            return new MyRunListFragment();
        }
        return null;
    }
}
