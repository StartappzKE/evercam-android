package io.evercam.androidapp;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.IOException;

import io.evercam.androidapp.utils.Constants;

public class OnBoardingActivity extends ParentAppCompatActivity implements
        TextureView.SurfaceTextureListener,MediaPlayer.OnPreparedListener {
    private final String TAG = "OnBoardingActivity";

    private MediaPlayer player;
    private TextureView textureView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        textureView = (TextureView) findViewById(R.id.intro_texture_view);
        textureView.setSurfaceTextureListener(this);

        Button signUpButton = (Button) findViewById(R.id.btn_welcome_signup);
        Button loginButton = (Button) findViewById(R.id.btn_welcome_login);

        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(OnBoardingActivity.this, SignUpActivity.class);
                startActivityForResult(signup, Constants.REQUEST_CODE_SIGN_UP);
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(OnBoardingActivity.this, LoginActivity.class);
                startActivityForResult(login, Constants.REQUEST_CODE_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_SIGN_IN || requestCode == Constants
                .REQUEST_CODE_SIGN_UP) {
            if (resultCode == Constants.RESULT_TRUE) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    private void playIntro() {
        if (player != null) {
            player.release();
            player = null;
        }

        try {
            player = new MediaPlayer();
            AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.gpo);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            player.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setSurface(new Surface(textureView.getSurfaceTexture()));
        mp.setLooping(true);
        mp.start();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        playIntro();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        player.stop();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}