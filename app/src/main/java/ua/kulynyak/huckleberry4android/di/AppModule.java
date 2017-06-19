package ua.kulynyak.huckleberry4android.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import ua.kulynyak.huckleberry4android.App;

/**
 * Application module refers to subcomponents, provides application level dependencies.
 */
@Module(subcomponents = {})
public class AppModule {

  @Provides
  Context provideContext(App application) {
    return application.getApplicationContext();
  }
}