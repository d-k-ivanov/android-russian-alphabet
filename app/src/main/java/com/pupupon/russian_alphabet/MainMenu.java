package com.pupupon.russian_alphabet;

import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_ABOUT;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_ALPHABET;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_GITHUB;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_LEARN;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_QUIZ;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_SWITCH_PRONUNCIATION;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.LABEL_EASTERN;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.LABEL_WESTERN;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsActivity;
import com.pupupon.russian_alphabet.utils.DefensiveURLSpan;


public class MainMenu extends GoogleAnalyticsActivity {
    // Vars:
    Button[] buttons = new Button[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        Typeface mainFont = Tools.setFont(this);

        // Answer Section
        buttons[0] = findViewById(R.id.menuEntry1);
        buttons[1] = findViewById(R.id.menuEntry2);
        buttons[2] = findViewById(R.id.menuEntry3);
        buttons[3] = findViewById(R.id.menuEntry4);
        buttons[4] = findViewById(R.id.menuEntry5);
        buttons[5] = findViewById(R.id.menuEntry6);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View copyrightView = findViewById(R.id.aboutCopyright);
            copyrightView.setVisibility(View.GONE);
        } else {
            DefensiveURLSpan.setUrlClickListener((TextView) findViewById(R.id.aboutCopyright), url -> userAction(ACTION_GITHUB, url));
        }

        // Init Buttons:
        for (Button i : buttons) {
            i.setTypeface(mainFont);
        }

        Storage.init(getApplicationContext());
        buttons[3].setText(getPronunciationIndication());
    }

    public void startMenuEntry1(View view) {
        userAction(ACTION_ALPHABET);
        Intent alphabet = new Intent(this, AlphabetActivity.class);
        startActivity(alphabet);
    }

    public void startMenuEntry2(View view) {
        userAction(ACTION_LEARN);
        Intent learn = new Intent(this, LearnActivity.class);
        startActivity(learn);
    }

    public void startMenuEntry3(View view) {
        userAction(ACTION_QUIZ);
        Intent quiz = new Intent(this, QuizActivity.class);
        startActivity(quiz);
    }

    public void startMenuEntry4(View view) {
        eventSwitchPronunciation();
        Snackbar.make(findViewById(R.id.activity_main_menu),
            getString(getVoiceIndication()) + getString(R.string.enabled),
            Snackbar.LENGTH_SHORT).show();
        Storage.setVoiceDefault(!Storage.getVoiceDefault());
        buttons[3].setText(getPronunciationIndication());
    }

    public void startMenuEntry5(View view) {
        Intent policy = new Intent(this, PrivacyPolicyActivity.class);
        startActivity(policy);

    }

    public void startMenuEntry6(View view) {
        userAction(ACTION_ABOUT);
        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
    }

    private int getPronunciationIndication() {
        return Storage.getVoiceDefault() ? R.string.westernPronunciation : R.string.easternPronunciation;
    }

    private void eventSwitchPronunciation() {
        if (Storage.getVoiceDefault()) {
            userAction(ACTION_SWITCH_PRONUNCIATION, LABEL_WESTERN);
        } else {
            userAction(ACTION_SWITCH_PRONUNCIATION, LABEL_EASTERN);
        }
    }

    private int getVoiceIndication() {
        return Storage.getVoiceDefault() ? R.string.voice1 : R.string.voice2;
    }
}
