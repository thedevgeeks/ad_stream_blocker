/* Copyright (C) 2016 Julian Andres Klode <jak@jak-linux.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.jak_linux.dns66.main;

import android.content.Context;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jak_linux.dns66.Configuration;
import org.jak_linux.dns66.ItemChangedListener;
import org.jak_linux.dns66.MainActivity;
import org.jak_linux.dns66.R;
import org.jak_linux.dns66.databinding.ViewItemBinding;

import java.util.List;
import java.util.Observable;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {
    public List<Configuration.Item> items;
    private Context context;

    public ItemRecyclerViewAdapter(ObservableList<Configuration.Item> items) {
        this.items = items;
        items.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Configuration.Item>>() {
            @Override
            public void onChanged(ObservableList<Configuration.Item> items) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Configuration.Item> items, int start, int count) {
                notifyItemRangeChanged(start, count);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Configuration.Item> items, int start, int count) {
                notifyItemRangeInserted(start, count);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Configuration.Item> items, int from, int to, int count) {
                if (from < to) {
                    for (int i = 0; i < count; i++)
                        notifyItemMoved(from + i, to + i);
                } else {
                    for (int i = count; i >= 0; i--)
                        notifyItemMoved(from + i, to + i);
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Configuration.Item> items, int start, int count) {
                notifyItemRangeRemoved(start, count);
            }
        });

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewItemBinding viewDataBinding = ViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.viewItemBinding.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewItemBinding viewItemBinding;

        public ViewHolder(ViewItemBinding viewItemBinding) {
            super(viewItemBinding.getRoot());
            this.viewItemBinding = viewItemBinding;

            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.item_enabled).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            final Configuration.Item item = viewItemBinding.getItem();
            if (v.getId() == R.id.item_enabled) {
                item.setState((item.getState() + 1) % 3);
            } else if (v == itemView) {
                // Start edit activity
                MainActivity main = (MainActivity) v.getContext();
                main.editItem(item, new ItemChangedListener() {
                            @Override
                            public void onItemChanged(Configuration.Item changedItem) {
                                items.set(position, changedItem);
                            }
                        }
                );
            }
        }
    }
}