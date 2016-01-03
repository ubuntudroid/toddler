package ubuntudroid.github.io.toddler.provider.jira.di;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ubuntudroid.github.io.toddler.provider.jira.JiraService;

/**
 * Don't convert this class to kotlin as dagger can't handle creating proper kotlin modules at the
 * moment!
 *
 * Created by Sven Bendel.
 */
@Module
public class JiraModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient () {
        return JiraModuleDelegate.INSTANCE.provideOkHttpClient();
    }

    @Provides
    JiraService provideService (OkHttpClient client, Application application) {
        return JiraModuleDelegate.INSTANCE.provideService(client, application);
    }
}
