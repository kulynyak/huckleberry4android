package ua.kulynyak.huckleberry4android.ui.fragment.todolist.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.ToDoListFragment;

/**
 * ToDo list level component
 */
@Subcomponent(modules = {ToDoListModule.class})
public interface ToDoListFragmentSubComponent extends AndroidInjector<ToDoListFragment> {
  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<ToDoListFragment> {
  }
}
