package ubuntudroid.github.io.toddler.settings.di

import dagger.Component
import ubuntudroid.github.io.toddler.di.AppModule
import ubuntudroid.github.io.toddler.settings.BindPreferenceSummaryToValueListener
import javax.inject.Singleton

/**
 * Created by Sven Bendel.
 */
@Component(modules = arrayOf(AppModule::class))
@Singleton
interface SettingsComponent {
    fun inject(bindPreferenceSummaryToValueListener: BindPreferenceSummaryToValueListener)
}