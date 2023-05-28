package com.pupupon.russian_alphabet;

import static com.pupupon.russian_alphabet.Tools.RAW;
import static com.pupupon.russian_alphabet.Tools.STRING;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_LISTEN;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_NEXT;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_PREVIOUS;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.CATEGORY_SOUND_EVENTS;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsActivity;

public class LearnActivity extends GoogleAnalyticsActivity implements OnClickListener {
    int globalPosition = 0;
    int MAX_LETTER = 33;
    int MAX_LETTER_PROG = 32;
    // Vars:
    private ImageView cursiveImage;
    private TextView upperCaseText;
    private TextView lowerCaseText;
    private TextView soundText;
    private final Button[] buttons = new Button[3];
    private final String[] letters = new String[MAX_LETTER];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_activity);
        Typeface mainFont = Tools.setFont(this);

        cursiveImage = findViewById(R.id.learn_image);
        upperCaseText = findViewById(R.id.learn_uppercase);
        upperCaseText.setTypeface(mainFont);
        lowerCaseText = findViewById(R.id.learn_lowercase);
        lowerCaseText.setTypeface(mainFont);
        soundText = findViewById(R.id.learn_pronunciation);
        soundText.setTypeface(mainFont);

        setLetterArrayValue();
        buttons[0] = findViewById(R.id.alphabetListen);
        buttons[1] = findViewById(R.id.alphabetPrevious);
        buttons[2] = findViewById(R.id.alphabetNext);
        for (Button i : buttons) {
            i.setOnClickListener(this);
            i.setTypeface(mainFont);
        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.alphabetListen:
                listen();
                userAction(ACTION_LISTEN);
                break;
            case R.id.alphabetPrevious:
                previousLetter();
                setup();
                listen();
                userAction(ACTION_PREVIOUS);
                break;
            case R.id.alphabetNext:
                nextLetter();
                setup();
                listen();
                userAction(ACTION_NEXT);
                break;
        }
    }

    private void setup() {
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(getString(R.string.app_name) + ": " + (globalPosition + 1) + " of 33");
        }
        String[] letter = getLetters(letters[globalPosition]);
        upperCaseText.setText(letter[0]);
        lowerCaseText.setText(letter[1]);
        soundText.setText(letter[2]);
        cursiveImage.setImageDrawable(getDrawableByName(letters[globalPosition]));
    }

    private String[] getLetters(String letter) {
        return ((String) getResources().getText(getResources().getIdentifier(letter, STRING, getPackageName()))).split(";");
    }

    private Drawable getDrawableByName(String name) {
        Resources resources = this.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", this.getPackageName());
        return ResourcesCompat.getDrawable(resources, resourceId, null);
    }

    private void setLetterArrayValue() {
        for (int i = 1; i <= 33; i++) {
            letters[i - 1] = Tools.getFileResourceName() + i;
        }
        setup();
    }

    private void listen() {
        final View view = buttons[0];
        view.setBackgroundResource(R.drawable.button_green);
        view.setEnabled(false);
        String letter = letters[globalPosition];
        setEvent(CATEGORY_SOUND_EVENTS, ACTION_LISTEN, getLetters(letter)[0]);
        Tools.playSound(this, getResources().getIdentifier(letter, RAW, getPackageName()));
        // Execute some code after 2 seconds have passed
        Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setBackgroundResource(R.drawable.button);
                view.setEnabled(true);
            }
        }, 1000);
    }

    private void previousLetter() {
        globalPosition = globalPosition == 0 ? MAX_LETTER_PROG : globalPosition - 1;
    }

    private void nextLetter() {
        globalPosition = globalPosition == MAX_LETTER_PROG ? 0 : globalPosition + 1;
    }
}
