package com.newbiechen.androidlib.base;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by PC on 2016/9/9.
 * 1、默认使用List作为容器
 */
public abstract class BaseAdapter <E,VH extends ViewHolder>extends Adapter<VH> {
    //在Adapter中建立一个容器
    protected List<E> mItemList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    @Override
    public void onBindViewHolder(VH holder, int position) {
        setUpViewHolder(holder,position);
        setUpClickListener(holder.itemView,position);
    }

    /**
     * 设置每个Item的点击事件
     */
    private void setUpClickListener(final View view , final int position){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置点击回调
                if (mItemClickListener != null){
                    mItemClickListener.itemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * 设置ITEM点击事件的接口
     */
    public interface OnItemClickListener{
        void itemClick(View view, int pos);
    }

    /**
     * 执行ViewHolder的逻辑
     * @param holder
     * @param position
     */
    public abstract void setUpViewHolder(VH holder, int position);
/******************************公共方法*****************************************/
    /**
     * 设置点击事件的监听器
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mItemClickListener = listener;
    }

    /**
     * 添加单个数据
     * @param item
     */
    public void addItem(E item){
        mItemList.add(item);
        notifyDataSetChanged();
    }

    /**
     * 添加多个数据
     * @param items
     */
    public void addItems(List<E> items){
        mItemList.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 移除全部数据
     */
    public void removeItems(){
        mItemList.clear();
    }

    /**
     * 专门为刷新数据的方法
     * @param items
     */
    public void refreshItems(List<E> items){
        removeItems();
        addItems(items);
    }

    public E getItem(int position){
        return mItemList.get(position);
    }
}

