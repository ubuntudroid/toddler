package ubuntudroid.github.io.toddler.di;

import android.app.Application;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ubuntudroid.github.io.toddler.ToddlerApplication;

/**
 * Created by Sven Bendel.
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    Application providesApplication () {
        return ToddlerApplication.Companion.getInstance();
    }

    @Provides
    @Singleton
    Bus providesAppBus () {
        return new Bus();
    }
}
