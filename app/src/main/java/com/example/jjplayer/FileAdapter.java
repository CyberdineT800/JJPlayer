package com.example.jjplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjplayer.ui.gallery.GalleryFragment;

import java.io.File;
import java.util.Stack;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    Context context;
    File[] filesAndFolders;
    private GalleryFragment galleryFragment;
    private Stack<File> folderStack = new Stack<>();

    public FileAdapter(Context context, File[] files, GalleryFragment galleryFragment) {
        this.context = context;
        this.filesAndFolders = files;
        this.galleryFragment = galleryFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File selectedFile = filesAndFolders[position];
        holder.filename.setText(selectedFile.getName());

        if (selectedFile.isDirectory()) {
            holder.fileicon.setImageResource(R.drawable.baseline_folder_24);
        } else {
            String filename = selectedFile.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

            if (extension.equals("zip") || extension.equals("rar")) {
                holder.fileicon.setImageResource(R.drawable.baseline_folder_zip_24);
            } else if (isAudioFile(extension)) {
                holder.fileicon.setImageResource(R.drawable.baseline_file_audio_24);
            } else if (isVideoFile(extension)) {
                holder.fileicon.setImageResource(R.drawable.baseline_file_video_24);
            } else if (isImageFile(extension)) {
                holder.fileicon.setImageResource(R.drawable.baseline_file_image_24);
            } else {
                // Set a default icon for other file types
                holder.fileicon.setImageResource(R.drawable.baseline_file_other_24);
            }
        }

        // Set click listener for item
        holder.itemView.setOnClickListener(v -> {
            if (selectedFile.isDirectory()) {
                if (selectedFile.exists() && selectedFile.listFiles() != null && selectedFile.listFiles().length > 0) {
                    galleryFragment.updateFolderStack(selectedFile);
                    navigateIntoFolder(selectedFile);
                } else {
                    Toast.makeText(context, "Folder is empty or cannot be accessed", Toast.LENGTH_SHORT).show();
                }
            } else {
                showFileInformation(selectedFile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public void setFilesAndFolders(File[] files) {
        this.filesAndFolders = files;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filename;
        ImageView fileicon;

        public ViewHolder(View view) {
            super(view);
            filename = view.findViewById(R.id.file_name_text_view);
            fileicon = view.findViewById(R.id.file_icon_view);
        }
    }

    private void navigateIntoFolder(File folder) {
        // Check if the folder exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            // Push the current folder to the stack
            folderStack.add(folder);

            // Get the list of files and folders inside the selected folder
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                // Update the filesAndFolders array with the contents of the selected folder
                filesAndFolders = files;
                // Notify the adapter that the data set has changed
                notifyDataSetChanged();
            } else {
                // Handle the case where the folder is empty or cannot be accessed
                Toast.makeText(context, "Folder is empty or cannot be accessed", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the selected folder does not exist or is not a directory
            Toast.makeText(context, "Folder does not exist or is not a directory", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFileInformation(File file) {
        // Display information about the file via Toast message
        String message = "File Name: " + file.getName() + "\nFile Path: " + file.getAbsolutePath();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private boolean isAudioFile(String extension) {
        return extension.equalsIgnoreCase("mp3") || extension.equalsIgnoreCase("wav") ||
                extension.equalsIgnoreCase("ogg") || extension.equalsIgnoreCase("flac") ||
                extension.equalsIgnoreCase("m4a") || extension.equalsIgnoreCase("aac") ||
                extension.equalsIgnoreCase("wma") || extension.equalsIgnoreCase("alac") ||
                extension.equalsIgnoreCase("amr") || extension.equalsIgnoreCase("opus");
    }

    private boolean isVideoFile(String extension) {
        return extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("avi") ||
                extension.equalsIgnoreCase("mkv") || extension.equalsIgnoreCase("mov") ||
                extension.equalsIgnoreCase("wmv") || extension.equalsIgnoreCase("flv") ||
                extension.equalsIgnoreCase("webm") || extension.equalsIgnoreCase("3gp");
    }

    private boolean isImageFile(String extension) {
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif") ||
                extension.equalsIgnoreCase("bmp") || extension.equalsIgnoreCase("tiff") ||
                extension.equalsIgnoreCase("svg") || extension.equalsIgnoreCase("webp");
    }
}
