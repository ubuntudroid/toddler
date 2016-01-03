package ubuntudroid.github.io.toddler.main.di

import dagger.Component
import ubuntudroid.github.io.toddler.di.AppModule
import ubuntudroid.github.io.toddler.main.ui.TaskFragment
import ubuntudroid.github.io.toddler.provider.jira.di.JiraModule
import javax.inject.Singleton

/**
 * Created by Sven Bendel.
 */
@Component(modules = arrayOf(AppModule::class, JiraModule::class))
@Singleton
interface MainComponent {
    fun inject(taskFragment: TaskFragment)
}