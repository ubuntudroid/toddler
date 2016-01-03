package ubuntudroid.github.io.toddler

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by Sven Bendel.
 */
class ToddlerApplication : Application() {

    companion object {
        lateinit var instance : Application
    }

    override fun onCreate() {
        super.onCreate()
        ToddlerApplication.instance = this

        Stetho.initializeWithDefaults(this)
    }
}
