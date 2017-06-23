package ua.kulynyak.huckleberry4android.di;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.ui.activity.di.MainActivityModule;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.di.ToDoListModule;

import javax.inject.Singleton;

/**
 * Application component, refers to application level modules only.
 */
@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,
    MainActivityModule.class,
    ToDoListModule.class
})
public interface AppComponent {

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(App application);

    AppComponent build();
  }

  void inject(App app);
}
