package com.zjut.runner.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.R;
import com.zjut.runner.util.StringUtil;

import java.util.List;

/**
 * Created by Phuylai on 2016/10/25.
 */

public class OrderListAdapter extends BaseAdapter {

    private Context context;
    private List<Object> orderModels;

    public OrderListAdapter(Context context,List<Object> orderModels){
        this.context = context;
        this.orderModels = orderModels;
    }

    @Override
    public int getCount() {
        if(orderModels == null)
            return 0;
        return orderModels.size();
    }

    @Override
    public OrderModel getItem(int position) {
        if(orderModels == null)
            return null;
        return (OrderModel) orderModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderModel orderModel = getItem(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_card_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_helper = (TextView) convertView.findViewById(R.id.tv_helper);
            viewHolder.tv_day =  (TextView) convertView.findViewById(R.id.tv_day);
            viewHolder.tv_month =  (TextView) convertView.findViewById(R.id.tv_month);
            viewHolder.tv_year =  (TextView) convertView.findViewById(R.id.tv_year);
            viewHolder.tv_remark =  (TextView) convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_title =  (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.rl_date = convertView.findViewById(R.id.rl_date);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.iv_helper = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(orderModel.getTitle());
        viewHolder.tv_remark.setText(orderModel.getRemark());
        setBackgroundDate(orderModel, viewHolder);
        String[] dateTime = orderModel.getOrderDate().split("\\s+");
        String date = dateTime[0];
        String time = dateTime[1];
        String[] dateEach = date.split("-");
        viewHolder.tv_day.setText(dateEach[0]);
        viewHolder.tv_month.setText(dateEach[1]);
        viewHolder.tv_year.setText(dateEach[2]);
        viewHolder.tv_time.setText(time);
        return convertView;
    }

    private void setBackgroundDate(OrderModel orderModel,ViewHolder viewHolder) {
        switch (orderModel.getStatus()){
            case PENDING:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                if(orderModel.isChosen()) {
                    viewHolder.tv_helper.setText(orderModel.getHelper().getCampusName());
                    viewHolder.iv_helper.setImageResource(R.drawable.ic_tag_faces_black_24dp);
                }else if(orderModel.getHelpers() > 0){
                    viewHolder.iv_helper.setImageResource(R.drawable.ic_tag_faces_black_24dp);
                    viewHolder.tv_helper.setText(StringUtil.convertIntegerToString(orderModel.getHelpers()));
                }else{
                    viewHolder.iv_helper.setImageResource(R.drawable.ic_face_black_24dp);
                    viewHolder.tv_helper.setText("");
                }
                break;
            case COMPLETED:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.green));
                viewHolder.tv_title.setText(orderModel.getHelper().getCampusName());
                viewHolder.iv_helper.setImageResource(R.drawable.ic_tag_faces_black_24dp);
                break;
            case CANCELLED:
                viewHolder.rl_date.setBackgroundColor(context.getResources().getColor(R.color.red));
                viewHolder.iv_helper.setImageResource(R.drawable.ic_face_black_24dp);
                viewHolder.tv_helper.setText("");
                break;
        }
    }

    class ViewHolder{
        View rl_date;
        TextView tv_day;
        TextView tv_month;
        TextView tv_year;
        TextView tv_title;
        TextView tv_remark;
        TextView tv_helper;
        TextView tv_time;
        ImageView iv_helper;
    }
}
