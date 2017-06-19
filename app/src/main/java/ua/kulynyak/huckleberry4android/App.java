package ua.kulynyak.huckleberry4android;

import android.app.Activity;
import android.app.Application;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import ua.kulynyak.huckleberry4android.di.AppComponent;
import ua.kulynyak.huckleberry4android.di.DaggerAppComponent;

import javax.inject.Inject;

public class App extends Application implements HasActivityInjector {

  private static App self;
  private AppComponent appComponent;

  @Inject
  DispatchingAndroidInjector<Activity> injector;

  @Override
  public void onCreate() {
    self = this;
    appComponent = DaggerAppComponent.builder().application(self).build();
    super.onCreate();
    getAppComponent().inject(this);

  }

  @Override
  public DispatchingAndroidInjector<Activity> activityInjector() {
    return injector;
  }

  public static AppComponent getAppComponent() {
    return self.appComponent;
  }

}
