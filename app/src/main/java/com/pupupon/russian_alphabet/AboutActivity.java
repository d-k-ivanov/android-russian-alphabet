package com.pupupon.russian_alphabet;

import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_GITHUB;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsActivity;
import com.pupupon.russian_alphabet.utils.DefensiveURLSpan;

public class AboutActivity extends GoogleAnalyticsActivity {

    DefensiveURLSpan.OnUrlListener mUrlListener = new DefensiveURLSpan.OnUrlListener() {
        @Override
        public void onClick(String url) {
            userAction(ACTION_GITHUB, url);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        Typeface mainFont = Tools.setFont(this);

        TextView aboutText = findViewById(R.id.aboutText);
        aboutText.setTypeface(mainFont);
        DefensiveURLSpan.setUrlClickListener(aboutText, mUrlListener);

        // TextView aboutCopyright = findViewById(R.id.aboutCopyright);
        // aboutCopyright.setTypeface(mainFont);
        // DefensiveURLSpan.setUrlClickListener(aboutCopyright, mUrlListener);
    }
}
