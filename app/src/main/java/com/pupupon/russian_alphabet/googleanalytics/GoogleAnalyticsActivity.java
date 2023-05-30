package com.pupupon.russian_alphabet.googleanalytics;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.ACTION_ORIENTATION_SCREEN;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.CATEGORY_USER_EVENTS;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.LABEL_LANDSCAPE;
import static com.pupupon.russian_alphabet.googleanalytics.GoogleAnalyticsConstants.LABEL_PORTRAIT;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pupupon.russian_alphabet.BuildConfig;

@SuppressWarnings({"FieldCanBeLocal", "SameParameterValue"})
@SuppressLint("Registered")
public class GoogleAnalyticsActivity extends AppCompatActivity {

    private final String ACTION = "action";
    private final String LABEL = "label";

//    private final boolean DEBUG = BuildConfig.DEBUG;
    private final boolean DEBUG = false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) {
            Log.d(GoogleAnalyticsActivity.class.getSimpleName(), "screen : " + this.getLocalClassName());
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, this.getLocalClassName());
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.getLocalClassName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }
    }

    protected void userAction(String action) {
        setEvent(CATEGORY_USER_EVENTS, action);
    }

    protected void userAction(String action, String label) {
        setEvent(CATEGORY_USER_EVENTS, action, label);
    }

    protected void setEvent(String category, String action) {
        setEvent(category, action, null);
    }

    protected void setEvent(String category, String action, String label) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, action);
        if (label != null) bundle.putString(LABEL, label);
        if (DEBUG) {
            Log.d(GoogleAnalyticsActivity.class.getSimpleName(), "CATEGORY : " + category + " ACTION : " + action + " LABEL : " + label);
        } else {
            mFirebaseAnalytics.logEvent(category, bundle);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == SCREEN_ORIENTATION_PORTRAIT) {
            userAction(ACTION_ORIENTATION_SCREEN, LABEL_PORTRAIT);
        } else {
            userAction(ACTION_ORIENTATION_SCREEN, LABEL_LANDSCAPE);
        }
    }
}
