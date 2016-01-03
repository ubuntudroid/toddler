package ubuntudroid.github.io.toddler.settings

import android.media.RingtoneManager
import android.net.Uri
import android.preference.ListPreference
import android.preference.Preference
import android.preference.RingtonePreference
import android.text.TextUtils
import com.squareup.otto.Bus
import ubuntudroid.github.io.toddler.R
import ubuntudroid.github.io.toddler.events.AccountChangedEvent
import ubuntudroid.github.io.toddler.events.ServerChangedEvent
import ubuntudroid.github.io.toddler.settings.di.DaggerSettingsComponent
import javax.inject.Inject

/**
 * Created by Sven Bendel.
 */
class BindPreferenceSummaryToValueListener: Preference.OnPreferenceChangeListener {

    @Inject
    lateinit var appBus: Bus

    init {
        DaggerSettingsComponent.create().inject(this)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val stringValue = newValue.toString()

        if (preference is ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            val index = preference.findIndexOfValue(stringValue)

            // Set the summary to reflect the new value.
            preference.setSummary(
                    if (index >= 0)
                        preference.entries[index]
                    else
                        null)

        } else if (preference is RingtonePreference) {
            // For ringtone preferences, look up the correct display value
            // using RingtoneManager.
            if (TextUtils.isEmpty(stringValue)) {
                // Empty values correspond to 'silent' (no ringtone).
                preference.setSummary(R.string.pref_ringtone_silent)

            } else {
                val ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue))

                if (ringtone == null) {
                    // Clear the summary if there was a lookup error.
                    preference.setSummary(null)
                } else {
                    // Set the summary to reflect the new ringtone display
                    // name.
                    val name = ringtone.getTitle(preference.getContext())
                    preference.setSummary(name)
                }
            }

        } else if (!preference?.key.equals("password")) {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference?.summary = stringValue
        }

        when (preference?.key) {
            "password" -> appBus.post(AccountChangedEvent())
            "login" -> appBus.post(AccountChangedEvent())
            "server" -> appBus.post(ServerChangedEvent())
        }
        return true
    }
}