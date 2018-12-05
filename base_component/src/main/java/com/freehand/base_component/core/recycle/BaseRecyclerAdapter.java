package com.freehand.base_component.core.recycle;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by namhoainguyen on 8/8/17.
 */

public abstract class BaseRecyclerAdapter<T, H  extends ViewHolder> extends RecyclerView.Adapter<H> {
    public static final int REMOVE = 1;
    public static final int CLEAR_ALL = 2;
    public static final int SET_ITEMS = 3;
    public static final int ADD_ITEM_ADD_FIRST = 4;
    public static final int ADD_ITEM = 5;
    public static final int ADD_ITEMS = 6;
    public enum AdapterChange{REMOVE,CLEAR_ALL,SET_ITEMS,ADD_ITEM_ADD_FIRST,ADD_ITEM,ADD_ITEMS}

    protected List<T> items = new ArrayList<>();
    private BehaviorSubject<Pair<AdapterChange, Object>> dataChanged = BehaviorSubject.create();
    protected OnItemListener<T> onItemClick;
    private RecyclerView.LayoutManager layoutManager;

    public BaseRecyclerAdapter(List<T> items) {
        this.items = items;
    }

    public BaseRecyclerAdapter() {
    }

    @Override
    public void onBindViewHolder(final H holder, final int position) {
        bindData(holder, position);
        if (onItemClick != null) {
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onSingleConfirm(view, getItem(position), position);
                }
            });
        }
    }

    protected abstract void bindData(H holder, int position);

    public BaseRecyclerAdapter<T, H> setOnItemListener(OnItemListener<T> handle) {
        this.onItemClick = handle;
        return this;
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(defineItemLayout(viewType),parent,false);
        return CreateHolder(view,viewType);
    }

    protected abstract H CreateHolder(View view, int viewType);

    protected abstract int defineItemLayout(int viewType);

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void addItems(List<T> moreItems) {
        if (moreItems == null || moreItems.size() == 0) return;
        if (items == null) items = new ArrayList<>();
        final int positionStart = items.size();
        items.addAll(moreItems);
        notifyItemRangeInserted(positionStart, moreItems.size());
        if (isDataChangedValid())
            dataChanged.onNext(new Pair(ADD_ITEMS, moreItems));
    }

    public void addItem(T moreItem) {
        if (items == null) items = new ArrayList<>();
        items.add(moreItem);
        notifyDataSetChanged();
        if (isDataChangedValid()) dataChanged.onNext(new Pair(ADD_ITEM, moreItem));
    }

    public void addItem(T moreItem, int index) {
        if (items == null) items = new ArrayList<>();
        items.add(index, moreItem);
        notifyItemInserted(index);
        if (isDataChangedValid()) dataChanged.onNext(new Pair(ADD_ITEM, moreItem));
    }

    public void addItemAtFirst(T moreItem) {
        if (items == null) items = new ArrayList<>();
        items.add(0, moreItem);
        notifyDataSetChanged();
        if (isDataChangedValid())
            dataChanged.onNext(new Pair(ADD_ITEM_ADD_FIRST, moreItem));
    }

    public void addItemAtFirst(List<T> data) {
        if (items == null) items = new ArrayList<>();
        items.addAll(0, data);
        notifyDataSetChanged();
        if (isDataChangedValid())
            dataChanged.onNext(new Pair(ADD_ITEM_ADD_FIRST, data));
    }

    public void removeItem(T removeItem) {
        if (items != null) {
            int index = items.indexOf(removeItem);
            if(index<0 || index >= items.size()) return;
            items.remove(removeItem);
            notifyItemRemoved(index);
            if (isDataChangedValid())
                dataChanged.onNext(new Pair(REMOVE, removeItem));
        }
    }

    public void removeIndex(int position) {
        if (position < 0 || position >= items.size()) return;
        Object removeItem = items.get(position);
        items.remove(position);
        notifyItemRemoved(position);
        if (isDataChangedValid()) dataChanged.onNext(new Pair(REMOVE, removeItem));
    }

    public void clearAll() {
        items = null;
        notifyDataSetChanged();
        if (isDataChangedValid()) dataChanged.onNext(new Pair(CLEAR_ALL, null));
    }

    public T getItem(int position) {
        if (items == null) return null;
        if (position < 0) return items.get(0);
        if (position >= items.size()) return items.get(items.size() - 1);
        return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
        notifyDataSetChanged();
        if (isDataChangedValid()) dataChanged.onNext(new Pair(SET_ITEMS, items));
    }

    public void setItem(T t, int position) {
        if (items != null && position >= 0 && position < items.size()) {
            items.set(position, t);
            notifyItemChanged(position);
        }
    }

    public boolean containsItem(T t) {
        if (t == null) return false;
        return items != null && items.indexOf(t) >= 0;
    }

    public int getPosition(T t) {
        if (items != null) return items.indexOf(t);
        return 0;
    }

    public T getItem(T t) {
        return getItem(getPosition(t));
    }

    public Observable<Pair<AdapterChange, Object>> dataChangedObservable() {
        return dataChanged;
    }

    protected boolean shouldReleaseItemWhenDestroy() {
        return true;
    }

    public void destroy() {
        if (shouldReleaseItemWhenDestroy()) {
            items = null;
        }
        if (dataChanged != null && !dataChanged.hasComplete()) {
            dataChanged.onComplete();
            dataChanged = null;
        }
    }

    private boolean isDataChangedValid() {
        return dataChanged != null && !dataChanged.hasComplete();
    }

    public boolean hasItems(List<T> data) {
        if (data == null || data.size() == 0) return false;
        ArrayList<T> list = new ArrayList<>(data);
        if (items == null || items.size() == 0) return false;
        for (T temp : items) {
            if (list.contains(temp)) {
                list.remove(temp);
            }
        }
        return list.size() != 0;
    }


    public boolean hasItem(T item) {
        if (items == null || items.size() == 0) return false;
        for (T temp : items) {
            if (temp.equals(item)) return true;
        }
        return false;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
}
