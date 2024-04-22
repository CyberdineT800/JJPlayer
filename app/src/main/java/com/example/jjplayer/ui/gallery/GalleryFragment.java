package com.example.jjplayer.ui.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjplayer.FileAdapter;
import com.example.jjplayer.FolderHierarchyAdapter;
import com.example.jjplayer.R;
import com.example.jjplayer.databinding.FragmentGalleryBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private FileAdapter fileAdapter;
    private View rootView;
    private Stack<File> folderStack = new Stack<>();
    private RecyclerView folderHierarchyRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        CreateFileManager();

        String path = Environment.getExternalStorageDirectory().getPath();
        File root = new File(path);
        File[] files = root.listFiles();
        folderStack.push(root);

        setupFolderHierarchyRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), "Storage permission is required, please allow from settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        }
    }

    private boolean checkPermission() {
        int res = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    private void CreateFileManager() {
        String path = Environment.getExternalStorageDirectory().getPath();
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        TextView noFilestextView = rootView.findViewById(R.id.no_files_found);

        File root = new File(path);
        File[] files = root.listFiles();
        fileAdapter = new FileAdapter(requireContext(), files);

        if (files == null || files.length == 0) {
            noFilestextView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new FileAdapter(requireContext(), new File[0]));
        } else {
            noFilestextView.setVisibility(View.INVISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(fileAdapter);
        }
    }

    private void setupFolderHierarchyRecyclerView() {
        folderHierarchyRecyclerView = rootView.findViewById(R.id.folder_hierarchy_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        folderHierarchyRecyclerView.setLayoutManager(layoutManager);

        if (folderStack != null && !folderStack.isEmpty()) {
            Log.d("FolderHierarchyAdapter", "FolderStack size: " + folderStack.size());

            FolderHierarchyAdapter folderHierarchyAdapter = new FolderHierarchyAdapter(requireContext(), folderStack);
            folderHierarchyRecyclerView.setAdapter(folderHierarchyAdapter);

            folderHierarchyAdapter.setOnItemClickListener(new FolderHierarchyAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    // Get the selected folder from the folder stack
                    File selectedFolder = folderStack.get(position);

                    // Navigate to the selected folder
                    navigateToFolder(selectedFolder);
                }
            });
        } else {
            Log.d("FolderHierarchyAdapter", "FolderStack is empty or null");
        }
    }


    private void navigateToFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            folderStack.push(folder);
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                fileAdapter.setFilesAndFolders(files);
                fileAdapter.notifyDataSetChanged();

                if (folderHierarchyRecyclerView != null) {
                    FolderHierarchyAdapter folderHierarchyAdapter = (FolderHierarchyAdapter) folderHierarchyRecyclerView.getAdapter();
                    if (folderHierarchyAdapter != null) {
                        folderHierarchyAdapter.setFolderStack(new ArrayList<>(folderStack));
                        folderHierarchyAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Folder is empty or cannot be accessed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Folder does not exist or is not a directory", Toast.LENGTH_SHORT).show();
        }
    }

}
