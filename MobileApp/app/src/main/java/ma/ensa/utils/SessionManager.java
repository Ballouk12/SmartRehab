package ma.ensa.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_PATIENT_ID = "patientId";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void savePatientId(int patientId) {
        editor.putInt(KEY_PATIENT_ID, patientId);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public int getPatientId() {
        return prefs.getInt(KEY_PATIENT_ID, -1);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}