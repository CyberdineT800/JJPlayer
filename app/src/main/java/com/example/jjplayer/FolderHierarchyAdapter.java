package com.example.jjplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderHierarchyAdapter extends RecyclerView.Adapter<FolderHierarchyAdapter.ViewHolder> {
    private Context context;
    private List<File> folderStack;
    private OnItemClickListener listener;

    public FolderHierarchyAdapter(Context context, List<File> folderStack) {
        this.context = context;
        this.folderStack = folderStack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_hierarchy_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File folder = folderStack.get(position);
        holder.folderName.setText(folder.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderStack.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;

        public ViewHolder(View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name_text_view);
        }
    }
}

/*
package com.example.jjplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FolderHierarchyAdapter extends RecyclerView.Adapter<FolderHierarchyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<File> folderStack;
    private OnItemClickListener listener;

    public FolderHierarchyAdapter(Context context, ArrayList<File> folderStack) {
        this.context = context;
        this.folderStack = folderStack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_hierarchy_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File folder = folderStack.get(position);
        holder.folderName.setText(folder.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderStack.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;

        public ViewHolder(View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name_text_view);
        }
    }

    public void setFolderStack(ArrayList<File> folderStack) {
        this.folderStack = folderStack;
    }
}
*/
