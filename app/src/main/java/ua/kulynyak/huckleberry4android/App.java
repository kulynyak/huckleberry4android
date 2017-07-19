package ua.kulynyak.huckleberry4android;

import android.app.Application;
import ua.kulynyak.huckleberry4android.di.AppComponent;
import ua.kulynyak.huckleberry4android.di.DaggerAppComponent;

public class App extends Application {

  private static App self;
  private AppComponent appComponent;

  @Override
  public void onCreate() {
    self = this;
    appComponent = DaggerAppComponent.builder().application(self).build();
    super.onCreate();
    getAppComponent().inject(this);

  }

  public static AppComponent getAppComponent() {
    return self.appComponent;
  }

}
