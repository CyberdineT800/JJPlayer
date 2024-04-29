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
import com.example.jjplayer.ui.home.HomeFragment;

import java.io.File;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private FileAdapter fileAdapter;
    private View rootView;
    private ArrayList<File> folderStack = new ArrayList<>();

    private RecyclerView folderHierarchyRecyclerView;
    FolderHierarchyAdapter folderHierarchyAdapter;

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

        if (!checkPermission()) {
            requestPermission();
        }
        CreateFileManager();

        String path = Environment.getExternalStorageDirectory().getPath();
        File root = new File(path);

        folderStack.add(root);

        setupFolderHierarchyRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            Toast.makeText(requireContext(), "Storage permission is required, please allow from settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
        fileAdapter = new FileAdapter(requireContext(), files, this);
        //fileAdapter.setOnAudioFileSelectedListener(homeFragment);

        if (files == null || files.length == 0) {
            noFilestextView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new FileAdapter(requireContext(), new File[0], this));
        } else {
            noFilestextView.setVisibility(View.INVISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(fileAdapter);
        }
    }

    private void setupFolderHierarchyRecyclerView() {
        folderHierarchyRecyclerView = binding.folderHierarchyRecyclerView;
        folderHierarchyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        folderHierarchyAdapter = new FolderHierarchyAdapter(requireContext(), folderStack);
        folderHierarchyRecyclerView.setAdapter(folderHierarchyAdapter);
        folderHierarchyAdapter.setOnItemClickListener(this::navigateToFolder);
    }

    private void navigateToFolder(int position) {
        // Clear stack from the selected position onwards
        while (folderStack.size() > position + 1) {
            folderStack.remove(folderStack.size() - 1);
        }

        // Get the selected folder from the stack
        File selectedFolder = folderStack.get(position);

        // Update the file adapter with the contents of the selected folder
        File[] files = selectedFolder.listFiles();
        fileAdapter.setFilesAndFolders(files != null ? files : new File[0]);

        // Notify the folder hierarchy adapter that the data has changed
        folderHierarchyAdapter.notifyDataSetChanged();
    }

    public void updateFolderStack(File folder) {
        folderStack.add(folder);
        folderHierarchyAdapter.notifyItemInserted(folderStack.size() - 1);
    }
}
