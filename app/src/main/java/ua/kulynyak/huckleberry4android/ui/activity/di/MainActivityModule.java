package ua.kulynyak.huckleberry4android.ui.activity.di;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import ua.kulynyak.huckleberry4android.ui.activity.MainActivity;

@Module
public abstract class MainActivityModule {
  @Binds
  @IntoMap
  @ActivityKey(MainActivity.class)
  abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(
      MainActivitySubComponent.Builder builder);
}
