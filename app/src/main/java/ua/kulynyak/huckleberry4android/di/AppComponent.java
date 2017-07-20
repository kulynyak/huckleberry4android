package ua.kulynyak.huckleberry4android.di;

import dagger.BindsInstance;
import dagger.Component;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.ui.activity.MainActivity;
import ua.kulynyak.huckleberry4android.ui.fragment.details.ToDoTaskDetailsFragment;
import ua.kulynyak.huckleberry4android.ui.fragment.details.ToDoTaskDetailsPresenter;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.ToDoTaskListFragment;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.ToDoTaskListPresenter;

import javax.inject.Singleton;

/**
 * Application component, refers to application level modules only.
 */
@Singleton
@Component(modules = {
    AppModule.class,
    NavigationModule.class
})
public interface AppComponent {

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(App application);

    AppComponent build();
  }

  void inject(App app);

  void inject(MainActivity activity);

  void inject(ToDoTaskDetailsFragment fragment);

  void inject(ToDoTaskListFragment fragment);

  @Singleton
  void inject(ToDoTaskDetailsPresenter presenter);

  @Singleton
  void inject(ToDoTaskListPresenter presenter);

}