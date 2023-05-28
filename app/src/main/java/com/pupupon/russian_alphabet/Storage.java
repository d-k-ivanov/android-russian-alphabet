package com.pupupon.russian_alphabet;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

public class Storage {
    private static final String VOICE_DEFAULT = "voice_default";

    public static void init(Context context) {
        Hawk.init(context).build();
    }

    public static boolean getVoiceDefault() {
        return Hawk.get(VOICE_DEFAULT, true);
    }

    public static void setVoiceDefault(boolean bool) {
        Hawk.put(VOICE_DEFAULT, bool);
    }
}
