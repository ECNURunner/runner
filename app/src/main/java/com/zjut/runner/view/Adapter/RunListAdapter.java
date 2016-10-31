package com.zjut.runner.view.Adapter;

import android.content.Context;
import android.view.View;

import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.StringUtil;

import java.util.List;

/**
 * Created by Phuylai on 2016/10/31.
 */

public class RunListAdapter extends OrderListAdapter {

    public RunListAdapter(Context context, List<Object> orderModels) {
        super(context, orderModels);
    }

    @Override
    protected void setBackgroundDate(Context context,OrderModel orderModel, ViewHolder viewHolder) {
        switch (orderModel.getStatus()){
            case PENDING:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.tv_helper.setText("");
                viewHolder.iv_helper.setVisibility(View.GONE);
                break;
            case COMPLETED:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.green));
                viewHolder.tv_helper.setText("");
                viewHolder.iv_helper.setVisibility(View.GONE);
                break;
            case CANCELLED:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.gray));
                viewHolder.iv_helper.setVisibility(View.GONE);
                viewHolder.tv_helper.setText("");
                break;
            case REJECTED:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.red));
                viewHolder.iv_helper.setVisibility(View.GONE);
                viewHolder.tv_helper.setText("");
                break;
            case GO:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                viewHolder.iv_helper.setVisibility(View.GONE);
                viewHolder.tv_helper.setText("");
                break;
        }
    }
}
