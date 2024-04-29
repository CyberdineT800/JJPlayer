package com.example.jjplayer.ui.home;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.jjplayer.FileAdapter;
import com.example.jjplayer.databinding.FragmentHomeBinding;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private TextView tvSong, tvArtist, tvCurrentTime, tvFullTime;
    private ImageButton btnPlay, btnPrev, btnNext, btnRepeat, btnShuffle;
    private ProgressBar progressBar;
    private ViewPager viewPager;
    private FragmentHomeBinding binding;

    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvSong = binding.tvSong;
        tvArtist = binding.tvArtist;
        tvCurrentTime = binding.tvCurrentTime;
        tvFullTime = binding.tvFullTime;
        btnPlay = binding.btnPlay;
        btnPrev = binding.btnPrev;
        btnNext = binding.btnNext;
        btnRepeat = binding.btnRepeat;
        btnShuffle = binding.btnShuffle;
        progressBar = binding.progressBar;
        viewPager = binding.overlapPager;

        setupMediaPlayer();
        setupClickListeners();

        // final TextView textView = binding.textHome;
        // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    public interface OnAudioFileSelectedListener {
        void onAudioFileSelected(File audioFile);
    }

    private OnAudioFileSelectedListener listener;

    public void setOnAudioFileSelectedListener(OnAudioFileSelectedListener listener) {
        this.listener = listener;
    }


    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    private void setupClickListeners() {
        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                pauseAudio();
            } else {
                playAudio();
            }
        });

        btnNext.setOnClickListener(v -> {
            // Handle next audio file
        });

        btnPrev.setOnClickListener(v -> {
            // Handle previous audio file
        });

        btnRepeat.setOnClickListener(v -> {
            // Handle repeat functionality
        });
    }


    private void playAudio() {
        Toast.makeText(getContext(), "Playing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        tvFullTime.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        tvCurrentTime.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );

        progressBar.setMax((int) finalTime);
        progressBar.setProgress((int) startTime);
        myHandler.postDelayed(updateSongTime, 100);
    }


    private void pauseAudio() {
        Toast.makeText(getContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.pause();
    }

    private Runnable updateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tvCurrentTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
            );
            progressBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };


    /*@Override
    public void onAudioFileSelected(File audioFile) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepare();
            tvSong.setText(audioFile.getName());
            playAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/
}