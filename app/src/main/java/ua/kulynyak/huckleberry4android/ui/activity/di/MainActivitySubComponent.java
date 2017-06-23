package ua.kulynyak.huckleberry4android.ui.activity.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.kulynyak.huckleberry4android.ui.activity.MainActivity;

/**
 * App level component
 */
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivitySubComponent extends AndroidInjector<MainActivity> {
  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<MainActivity> {
  }
}
