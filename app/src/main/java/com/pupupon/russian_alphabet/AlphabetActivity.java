package com.pupupon.russian_alphabet;

import static com.pupupon.russian_alphabet.Tools.RAW;
import static com.pupupon.russian_alphabet.Tools.STRING;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

@SuppressLint("DiscouragedApi")
public class AlphabetActivity extends AppCompatActivity implements View.OnClickListener {
    private final int TOTAL_LETTERS = 33;
    // private Typeface mainFont = Tools.setFont(this);
    private final Button[] buttons = new Button[TOTAL_LETTERS];
    // private String[] letters    = new String[BUTTONS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface mainFont = Tools.setFont(this);
        setContentView(R.layout.alphabet_activity);

        for (int i = 1; i <= TOTAL_LETTERS; i++) {
            int index = i - 1;
            final String letterResource = Tools.getFileResourceName() + i;
            final String[] letter = getAlphabetEntryArray(letterResource);
            int id = getResources().getIdentifier("letter_btn_" + i, "id", getApplicationContext().getPackageName());
            buttons[index] = findViewById(id);
            buttons[index].setTypeface(mainFont);
            buttons[index].setText(letter[0]);
            buttons[index].setTag(letter);
            // buttons[index].setTooltipText(letterResource);
            buttons[index].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(final View view) {
        Typeface mainFont = Tools.setFont(this);
        Button button = (Button) view;
        button.setBackgroundResource(R.drawable.button_green);

        // Inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View popupView = inflater.inflate(R.layout.alphabet_popup, null);

        // Create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        String[] letter = (String[]) button.getTag();

        ImageView imageView;
        TextView upperCaseText;
        TextView lowerCaseText;
        TextView pronunciationText;

        imageView = popupView.findViewById(R.id.alphabet_popup_image);
        imageView.setImageDrawable(getDrawableByName(letter[3]));

        upperCaseText = popupView.findViewById(R.id.alphabet_popup_uppercase);
        upperCaseText.setText(letter[0]);
        upperCaseText.setTypeface(mainFont);

        lowerCaseText = popupView.findViewById(R.id.alphabet_popup_lowercase);
        lowerCaseText.setText(letter[1]);
        lowerCaseText.setTypeface(mainFont);

        pronunciationText = popupView.findViewById(R.id.alphabet_popup_pronunciation);
        pronunciationText.setText(letter[2]);
        pronunciationText.setTypeface(mainFont);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Button button_listen = popupView.findViewById(R.id.alphabet_popup_button_listen);
        button_listen.setTypeface(mainFont);
        button_listen.setTag(letter);

        listen(button_listen);

        button_listen.setOnClickListener(this::listen);

        Button button_close = popupView.findViewById(R.id.alphabet_popup_button_close);
        button_close.setTypeface(mainFont);
        button_close.setOnClickListener(v -> popupWindow.dismiss());

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(this::resetButtonsBackground);

        popupView.setOnTouchListener((v, event) -> {
            v.performClick();
            popupWindow.dismiss();
            return true;
        });
    }

    private void listen(final View view) {
        view.setBackgroundResource(R.drawable.button_green);
        view.setEnabled(false);
        String[] letter = (String[]) view.getTag();
        Tools.playSound(this, getResources().getIdentifier(letter[3], RAW, getPackageName()));
        Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(() -> {
            view.setBackgroundResource(R.drawable.button);
            view.setEnabled(true);
        }, 1000);
    }

    private Drawable getDrawableByName(String name) {
        Resources resources = this.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", this.getPackageName());
        return ResourcesCompat.getDrawable(resources, resourceId, null);
    }

    private String[] getAlphabetEntryArray(String letter) {
        String resources = (String) getResources().getText(getResources().getIdentifier(letter, STRING, getPackageName()));
        resources += ";" + letter;
        return (resources).split(";");
    }

    private void resetButtonsBackground() {
        for (Button b : buttons) {
            b.setBackgroundResource(R.drawable.button);
        }
    }
}
