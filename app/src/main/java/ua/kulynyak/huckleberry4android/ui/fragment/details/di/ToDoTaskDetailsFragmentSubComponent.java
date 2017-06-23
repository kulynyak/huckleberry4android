package ua.kulynyak.huckleberry4android.ui.fragment.details.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.kulynyak.huckleberry4android.ui.fragment.details.ToDoTaskDetailsFragment;

/**
 * ToDo task details level component
 */
@Subcomponent(modules = {ToDoTaskDetailsModule.class})
public interface ToDoTaskDetailsFragmentSubComponent
    extends AndroidInjector<ToDoTaskDetailsFragment> {

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<ToDoTaskDetailsFragment> {
  }
}
