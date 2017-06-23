package ua.kulynyak.huckleberry4android.ui.fragment.todolist.di;

import android.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.FragmentKey;
import dagger.multibindings.IntoMap;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.ToDoListFragment;

/**
 * ToDo list level module holds all the bindings needed for this feature.
 */
@Module()
public abstract class ToDoListModule {
  @Binds
  @IntoMap
  @FragmentKey(ToDoListFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment> bindToDoListFragmentInjectorFactory(
      ToDoListFragmentSubComponent.Builder builder);
}
