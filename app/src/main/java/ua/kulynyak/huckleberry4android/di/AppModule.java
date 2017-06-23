package ua.kulynyak.huckleberry4android.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.data.ToDoTaskRepositoryNaiveImpl;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;
import ua.kulynyak.huckleberry4android.ui.activity.di.MainActivitySubComponent;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.di.ToDoListFragmentSubComponent;

import javax.inject.Singleton;

/**
 * Application module refers to subcomponents, provides application level dependencies.
 */
@Module(subcomponents = {MainActivitySubComponent.class, ToDoListFragmentSubComponent.class})
public class AppModule {

  @Provides
  Context provideContext(App application) {
    return application.getApplicationContext();
  }

  /**
   * @param context is unused here, but convenient if sqlite db based repository is used.
   * @return preferred {@link ToDoTaskRepository} for the current application
   */
  @Provides
  @Singleton
  ToDoTaskRepository provideToDoTaskRepository(Context context) {
    // configure whole app to dial with ToDoTaskRepositoryNaiveImpl
    return new ToDoTaskRepositoryNaiveImpl();
  }
}