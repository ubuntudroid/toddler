package ubuntudroid.github.io.toddler

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Sven Bendel.
 */
object User {
    private var applicationPreferences: SharedPreferences? = null

    public fun getPassword(applicationContext: Context): String {
        ensurePreferences(applicationContext)
        return applicationPreferences!!.getString("password", "")
    }

    public fun getLogin(applicationContext: Context): String {
        ensurePreferences(applicationContext)
        return applicationPreferences!!.getString("login", "")
    }

    public fun isUserAvailable(applicationContext: Context): Boolean {
        return getLogin(applicationContext).isNotBlank()
    }

    private fun ensurePreferences(applicationContext: Context) {
        if (applicationPreferences == null) {
            applicationPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        }
    }
}
