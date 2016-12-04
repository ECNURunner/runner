package com.zjut.runner.Controller;

import android.os.Bundle;

import com.zjut.runner.Model.MenuType;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.view.fragments.AllOrderFragment;
import com.zjut.runner.view.fragments.BaseFragment;
import com.zjut.runner.view.fragments.BindFragment;
import com.zjut.runner.view.fragments.ChangePasswordFragment;
import com.zjut.runner.view.fragments.ChangePhoneFragment;
import com.zjut.runner.view.fragments.EmailFragment;
import com.zjut.runner.view.fragments.HelperFragment;
import com.zjut.runner.view.fragments.MyOrderFragment;
import com.zjut.runner.view.fragments.MyRunListFragment;
import com.zjut.runner.view.fragments.NameFragment;
import com.zjut.runner.view.fragments.NewRequestFragment;
import com.zjut.runner.view.fragments.OrderContentFragment;
import com.zjut.runner.view.fragments.ReplyRequestFragment;
import com.zjut.runner.view.fragments.RequestInfoFragment;
import com.zjut.runner.view.fragments.RunContentFragment;
import com.zjut.runner.view.fragments.RunInfoFragment;
import com.zjut.runner.view.fragments.WebNormalFragment;

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
        }else if(param.equals(Constants.FRAG_CHANGE_PSW)){
            return new ChangePasswordFragment();
        }
        return null;
    }

    public static BaseFragment getFragment(String param, Bundle bundle,
                                           BaseFragment.SelectedItemCallBackListener selected){
        if(StringUtil.isNull(param)){
            return null;
        }
        if(param.equals(Constants.FRAG_ORDER_CONTENT)){
            OrderContentFragment orderContentFragment = new OrderContentFragment();
            orderContentFragment.setArguments(bundle);
            return orderContentFragment;
        }else if(param.equals(Constants.FRAG_ORDER_INFO)){
            RequestInfoFragment request = new RequestInfoFragment();
            request.setArguments(bundle);
            request.registerSelectedCallBackListener(selected,null);
            return request;
        }else if(param.equals(Constants.FRAG_BIND)){
            BindFragment bindFragment = new BindFragment();
            bindFragment.setArguments(bundle);
            return bindFragment;
        }else if(param.equals(Constants.FRAG_CHANGE_PHONE)){
            ChangePhoneFragment phoneFragment = new ChangePhoneFragment();
            phoneFragment.setArguments(bundle);
            return phoneFragment;
        }else if(param.equals(Constants.FRAG_CHANGE_EMAIL)){
            EmailFragment emailFragment = new EmailFragment();
            emailFragment.setArguments(bundle);
            return emailFragment;
        }else if(param.equals(Constants.FRAG_HELPER)){
            HelperFragment helperFragment = new HelperFragment();
            helperFragment.setArguments(bundle);
            helperFragment.registerSelectedCallBackListener(selected,null);
            return helperFragment;
        }else if(param.equals(Constants.FRAG_CHANGE_NAME)){
            NameFragment nameFragment = new NameFragment();
            nameFragment.setArguments(bundle);
            return nameFragment;
        }else if(param.equals(Constants.FRAG_REPLY_REQ)){
            ReplyRequestFragment replyRequestFragment = new ReplyRequestFragment();
            replyRequestFragment.setArguments(bundle);
            replyRequestFragment.registerSelectedCallBackListener(selected,null);
            return replyRequestFragment;
        }else if(param.equals(Constants.FRAG_RUN_CONTENT)){
            RunContentFragment runContentFragment = new RunContentFragment();
            runContentFragment.setArguments(bundle);
            return runContentFragment;
        }else if(param.equals(Constants.FRAG_RUN_INFO)){
            RunInfoFragment runInfoFragment = new RunInfoFragment();
            runInfoFragment.setArguments(bundle);
            runInfoFragment.registerSelectedCallBackListener(selected,null);
            return runInfoFragment;
        }else if(param.equals(Constants.FRAG_WEB)){
            WebNormalFragment webNormalFragment = new WebNormalFragment();
            webNormalFragment.setArguments(bundle);
            return webNormalFragment;
        }
        return null;
    }
}
