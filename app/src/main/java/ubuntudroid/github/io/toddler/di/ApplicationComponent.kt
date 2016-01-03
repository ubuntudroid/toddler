package ubuntudroid.github.io.toddler.di

import dagger.Component
import ubuntudroid.github.io.toddler.provider.jira.di.JiraModule
import javax.inject.Singleton

/**
 * ATTENTION: Changing components requires a project clean!
 *
 * Created by Sven Bendel.
 */
@Component(modules = arrayOf(JiraModule::class))
@Singleton
interface ApplicationComponent {
}
