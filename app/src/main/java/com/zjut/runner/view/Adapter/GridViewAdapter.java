package com.zjut.runner.view.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zjut.runner.Model.GridViewIconModel;
import com.zjut.runner.Model.LanguageType;
import com.zjut.runner.R;
import com.zjut.runner.util.LanguageUtil;

import java.util.List;

/**
 * Created by Phuylai on 2016/10/23.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<GridViewIconModel> gridModels;
    private DisplayImageOptions options;

    public GridViewAdapter(Context context,List<GridViewIconModel> gridModels){
        this.context = context;
        this.gridModels = gridModels;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        if(gridModels == null)
            return 0;
        return gridModels.size();
    }

    @Override
    public Object getItem(int position) {
        if(gridModels == null)
            return null;
        return gridModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        GridViewIconModel model = gridModels.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.iv_grid = (ImageView) convertView.findViewById(R.id.iv_grid);
            viewHolder.tv_grid = (TextView) convertView.findViewById(R.id.tv_grid);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(model.getImageId() > 0){
            viewHolder.iv_grid.setImageResource(model.getImageId());
        }
        else {
            ImageLoader.getInstance().displayImage(model.getImageUrl(), viewHolder.iv_grid, options);
        }
        LanguageType lang = LanguageUtil.getLang(context);
        if(lang == LanguageType.ENGLISH){
            viewHolder.tv_grid.setText(model.getEnName());
        }else {
            viewHolder.tv_grid.setText(model.getChName());
        }
        return convertView;
    }

    public class ViewHolder{
        ImageView iv_grid;
        TextView tv_grid;
    }
}

