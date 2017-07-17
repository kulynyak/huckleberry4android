package ua.kulynyak.huckleberry4android.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.ui.fragment.details.ToDoTaskDetailsFragment;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.ToDoTaskListFragment;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements HasFragmentInjector {
  @Inject
  DispatchingAndroidInjector<Fragment> fragmentInjector;

  private static final String TODO_TASK_LIST = "TODO_TASK_LIST";

  private static final String TODO_TASK_VIEW = "TODO_TASK_VIEW";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.main_container, new ToDoTaskListFragment(), TODO_TASK_LIST)
          .commit();
    }
  }

  public void showDetailFragment(@Nullable ToDoTask task, boolean edit) {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.replace(R.id.main_container,
        ToDoTaskDetailsFragment.initInstance(task, edit), TODO_TASK_VIEW);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public AndroidInjector<Fragment> fragmentInjector() {
    return fragmentInjector;
  }
}
