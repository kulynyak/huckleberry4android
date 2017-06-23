package ua.kulynyak.huckleberry4android.ui.fragment.details.di;

import android.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.FragmentKey;
import dagger.multibindings.IntoMap;
import ua.kulynyak.huckleberry4android.ui.fragment.details.ToDoTaskDetailsFragment;

/**
 * ToDo list level module holds all the bindings needed for this feature.
 */
@Module()
public abstract class ToDoTaskDetailsModule {
  @Binds
  @IntoMap
  @FragmentKey(ToDoTaskDetailsFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment> bindToDoTaskDetailsFragmentInjectorFactory(
      ToDoTaskDetailsFragmentSubComponent.Builder builder);
}
