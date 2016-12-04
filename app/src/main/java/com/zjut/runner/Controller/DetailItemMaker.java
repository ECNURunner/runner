package com.zjut.runner.Controller;

import android.content.Context;

import com.zjut.runner.Model.ActionType;
import com.zjut.runner.R;
import com.zjut.runner.widget.BaseViewHolder;
import com.zjut.runner.widget.DetailActionItemHolder;
import com.zjut.runner.widget.DetailActionItemHolder.ItemClickListener;

import java.io.IOException;
import java.io.OptionalDataException;

/**
 * Created by Phuylai on 2016/12/3.
 */

public class DetailItemMaker {

    private DetailActionItemHolder detailActionItemHolder;
    private Context context;
    private ItemClickListener itemClickListener;

    public DetailItemMaker(Context context, String desc,
                          ItemClickListener itemClickListener){
        detailActionItemHolder = new DetailActionItemHolder(context,0,R.string.str_username,
                desc,ActionType.NAME,true,itemClickListener);
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public BaseViewHolder getDetailActionItemHolder() {
        return detailActionItemHolder;
    }

    public void setDetailActionItemHolder(DetailActionItemHolder detailActionItemHolder) {
        this.detailActionItemHolder = detailActionItemHolder;
    }

    public BaseViewHolder genderItem(String title){
       return new DetailActionItemHolder(context,0,R.string.str_gender,title,
               ActionType.GENDER,true,itemClickListener);
    }

    public BaseViewHolder phoneItem(String phone){
        return new DetailActionItemHolder(context,0,R.string.str_phone,phone,ActionType.PHONE,
                true,itemClickListener);
    }

    public BaseViewHolder emailItem(String email){
        return new DetailActionItemHolder(context,0,R.string.str_email,email,
                ActionType.EMAIL,true,itemClickListener);
    }

    public BaseViewHolder campusID(String campusID){
        return new DetailActionItemHolder(context,0,R.string.str_id,campusID,null,false,
                itemClickListener);
    }

    public BaseViewHolder campusName(String name){
        return new DetailActionItemHolder(context,0,R.string.str_name,name,null,false,
                itemClickListener);
    }

    public BaseViewHolder campusBalance(String balance){
        return new DetailActionItemHolder(context,0,R.string.str_balance,balance,null,false,
                itemClickListener);
    }

    public BaseViewHolder campusUnbind(){
        return new DetailActionItemHolder(context,0,R.string.str_unbind,null,
                ActionType.UNBIND,true,itemClickListener);
    }

    public BaseViewHolder campusBind(){
        return new DetailActionItemHolder(context,0,R.string.str_bind,null,ActionType.BINDING,
                true,itemClickListener);
    }
}
