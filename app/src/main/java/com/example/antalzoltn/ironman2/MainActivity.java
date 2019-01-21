package com.example.antalzoltn.ironman2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText etPassword;
    private Button btnUnlock;
    private ConstraintLayout choose;
    private ConstraintLayout layout;
    private boolean visibility = false;
    private String locale;
    private String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        findViews();
        setVolume();
        if (savedInstanceState != null) {
            visibility = savedInstanceState.getBoolean("VISIBILITIES");
            locale = savedInstanceState.getString("LOCALE");

            if(locale.equals( "hu")) {
                etPassword.setHint("jelszó");
                btnUnlock.setText("felold");
            }
            else if(locale.equals("en")) {
                etPassword.setHint("password");
                btnUnlock.setText("unlock");
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        visibility = savedInstanceState.getBoolean("VISIBILITIES");
        if(visibility){
            choose.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
        locale = savedInstanceState.getString("LOCALE");
        if(locale.equals( "hu") ){
            etPassword.setHint("jelszó");
            btnUnlock.setText("felold");
        }
        else if(locale.equals("en")){
            etPassword.setHint("password");
            btnUnlock.setText("unlock");
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("VISIBILITIES", visibility);
        outState.putString("LOCALE", locale);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    private void findViews(){
        etPassword = findViewById(R.id.editText);
        choose = findViewById(R.id.choose);
        layout = findViewById(R.id.Layout);
        btnUnlock = findViewById(R.id.button2);
    }

    public void btnUnlock(View v) {
        String txt = etPassword.getText().toString();
        Log.d("TAG", txt);
        Log.d("TAG", "12345");
        Pattern mPattern = Pattern.compile("^[Aa]rea\\s?51$");

        Matcher matcher = mPattern.matcher(txt);
        if(matcher.find())
        {
            Log.d("TAG", "GOOD PASSWORD");
            Intent intent = new Intent(this, unlocked.class);
            Bundle b = new Bundle();
            Log.d(TAG, "btnUnlock: locale="+locale);
            b.putString("key", locale); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
            finish();
        }
        else{
            etPassword.setText("");
            MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.wrong);
            mPlayer.setVolume(0.5f,0.5f);
            mPlayer.start();
        }
    }

    /**
     * Beállítja, hogy a szövegflolvasó hallható legyen. Alapból le van némítva
     */
    private void setVolume() {
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float percent = 0.7f;
        int seventyVolume = (int) (maxVolume * percent);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, 0);
    }

    public void btnEnglish(View view) {
        visibility = true;
        choose.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        etPassword.setHint("password");
        btnUnlock.setText("unlock");
        locale = "en";
        restartInLocale(Locale.ENGLISH);
    }

    public void btnHungarian(View view) {
        visibility = true;
        choose.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        etPassword.setHint("jelszó");
        btnUnlock.setText("felold");
        locale = "hu";
        restartInLocale(Locale.forLanguageTag("hu"));
    }

    private void restartInLocale(Locale locale)
    {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        recreate();
    }

    public void btnReturnToChoose(View view) {
        visibility = false;
        choose.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
    }
}
