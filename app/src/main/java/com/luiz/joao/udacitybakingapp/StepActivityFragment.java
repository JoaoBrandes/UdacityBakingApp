package com.luiz.joao.udacitybakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.luiz.joao.udacitybakingapp.utils.Step;

import java.util.ArrayList;


public class StepActivityFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepActivityFragment.class.getSimpleName();
    private static MediaSession mMediaSession;
    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Step> stepList;
    private int position;

    private SimpleExoPlayerView mPlayerView;
    private PlaybackState.Builder mStateBuilder;

    public StepActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intentDetailItem = getActivity().getIntent();

        // Initialize the player view.
        View rootView = inflater.inflate(R.layout.number_list_step_detail, container, false);
        if (intentDetailItem != null) {
            mPlayerView = rootView.findViewById(R.id.playerView);

            position = intentDetailItem.getExtras().getInt("position");
            stepList = intentDetailItem.getExtras().getParcelableArrayList("step");
            Step step = stepList.get(position);

            if (step.getVideoUrl().isEmpty() || step.getVideoUrl().length() == 0) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                initializeMediaSession();
                initializePlayer(Uri.parse(step.getVideoUrl()));
            }

            TextView description = rootView.findViewById(R.id.step_description);
            description.setText(step.getDescription());

            Button nextButton = rootView.findViewById(R.id.button_next);

            if (position == stepList.size() - 1) {
                nextButton.setVisibility(View.INVISIBLE);
            } else {
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextStep(v);
                    }
                });
            }

            Button previousButton = rootView.findViewById(R.id.button_previous);
            if (position == 0) {
                previousButton.setVisibility(View.INVISIBLE);
            } else {
                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousStep(v);
                    }
                });
            }
        }
        return rootView;

    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSession(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackState.Builder()
                .setActions(
                        PlaybackState.ACTION_PLAY |
                        PlaybackState.ACTION_PAUSE |
                        PlaybackState.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSession.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    /**
     * Release the player when the activity is destroyed.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void nextStep(View view) {
        releasePlayer();
        Class destinationClass = StepActivity.class;
        Intent intentStep = new Intent(view.getContext(),destinationClass);
        intentStep.putExtra("step", stepList);
        intentStep.putExtra("position", position + 1);
        view.getContext().startActivity(intentStep);
    }

    public void previousStep(View view) {
        releasePlayer();
        Class destinationClass = StepActivity.class;
        Intent intentStep = new Intent(view.getContext(),destinationClass);
        intentStep.putExtra("step", stepList);
        intentStep.putExtra("position", position - 1);
        view.getContext().startActivity(intentStep);
    }
}