package sckdn.lisa.utils;

import android.content.SharedPreferences;

import sckdn.lisa.activities.Lisa;
import sckdn.lisa.model.UserModel;

public class Local {

    public static final String LOCAL_DATA = "local_data";
    public static final String USER = "user";
    public static final String SETTINGS = "settings";

    public static void saveUser(UserModel userModel) {
        if (userModel != null) {
            String userString = Lisa.sGson.toJson(userModel, UserModel.class);
            SharedPreferences.Editor editor = Lisa.sPreferences.edit();
            editor.putString(USER, userString);
            if (editor.commit()) {
                Lisa.sUserModel = userModel;
            }
        }
    }

    public static UserModel getUser() {
        return Lisa.sGson.fromJson(
                Lisa.sPreferences
                        .getString(USER, ""),
                UserModel.class);
    }

    public static Settings getSettings() {
        String settingsString = Lisa.sPreferences.getString(SETTINGS, "");
        Settings settings = Lisa.sGson.fromJson(settingsString, Settings.class);
        return settings == null ? new Settings() : settings;
    }

    public static void setSettings(Settings settings) {
        String settingsGson = Lisa.sGson.toJson(settings);
        SharedPreferences.Editor editor = Lisa.sPreferences.edit();
        editor.putString(SETTINGS, settingsGson);
        editor.apply();
        Lisa.sSettings = settings;
    }
}
