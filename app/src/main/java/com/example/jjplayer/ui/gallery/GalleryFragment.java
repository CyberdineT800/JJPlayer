package com.example.jjplayer.ui.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.jjplayer.R;
import com.example.jjplayer.databinding.FragmentGalleryBinding;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private View rootView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

//        if (checkPermission()) {
//            CreateFileManager();
//        } else {
//            requestPermission();
//        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        CreateFileManager();
//        if (checkPermission()) {
//            CreateFileManager();
//        } else {
//            requestPermission();
//        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestPermission() {
        // Use the activity context for Toast
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), "Storage permission is required, please allow from settings", Toast.LENGTH_SHORT).show();
        } else {
            // Request permissions from the activity
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE }, 111);
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        }
    }


    private boolean checkPermission () {
        int res = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (res == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private void CreateFileManager() {
        String path = Environment.getExternalStorageDirectory().getPath();
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        TextView noFilestextView = rootView.findViewById(R.id.no_files_found);

        // console.log(path);

        File root = new File(path);
        File[] files = root.listFiles();

        if (files == null || files.length == 0) {
            noFilestextView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new FileAdapter(requireContext(), new File[0]));
            //return;
        } else {
            noFilestextView.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(new FileAdapter(requireContext(), files));
        }
    }
}