package com.zjut.runner.view.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.R;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.ViewHolder> {

    private Context context;
    private List<HelperModel> helperModels;
    private HelperClickListener helperClickListener;

    public HelperAdapter(Context context,List<HelperModel> helperModels,HelperClickListener helperClickListener){
        this.context = context;
        this.helperModels = helperModels;
        this.helperClickListener = helperClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.helper_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        HelperModel helperModel = helperModels.get(i);
        viewHolder.tv_name.setText(helperModel.getCampusName());
        viewHolder.tv_charge.setText(context.getString((R.string.str_cost),
                StringUtil.convertIntegerToString(helperModel.getHelperCharge())));
        if(helperModel.getGenderType() != null) {
            viewHolder.tv_gender.setText(helperModel.getGenderType().toString());
        }
        ImageLoader.getInstance().displayImage(helperModel.getUrl(),viewHolder.iv_user, GeneralUtils.getOptions());
    }

    @Override
    public int getItemCount() {
        if(helperModels == null)
            return 0;
        return helperModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView iv_user;
        TextView tv_name;
        TextView tv_gender;
        TextView tv_charge;
        CardView cardView;

        ViewHolder(View view){
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_action_name);
            tv_charge = (TextView) view.findViewById(R.id.tv_desc);
            tv_gender = (TextView) view.findViewById(R.id.tv_gender);
            iv_user = (CircleImageView) view.findViewById(R.id.iv_user);
            cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HelperModel helperModel = helperModels.get(getAdapterPosition());
            performHelperClick(helperModel);
        }
    }

    public interface HelperClickListener{
        void onCardItemClick(HelperModel helperModel);
    }

    private void performHelperClick(HelperModel helperModel){
        if(helperClickListener != null){
            helperClickListener.onCardItemClick(helperModel);
        }
    }

}
