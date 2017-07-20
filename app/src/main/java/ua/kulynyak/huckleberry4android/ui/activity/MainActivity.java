package ua.kulynyak.huckleberry4android.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.FragmentNavigator;
import ru.terrakok.cicerone.commands.Replace;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.bus.ShowToDoTaskDetailsAction;
import ua.kulynyak.huckleberry4android.ui.commons.BackButtonListener;
import ua.kulynyak.huckleberry4android.ui.commons.Screens;
import ua.kulynyak.huckleberry4android.ui.fragment.details.ToDoTaskDetailsFragment;
import ua.kulynyak.huckleberry4android.ui.fragment.todolist.ToDoTaskListFragment;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {

  @Inject
  Router router;

  @Inject
  NavigatorHolder navigatorHolder;

  private Navigator navigator = new FragmentNavigator(getFragmentManager(), R.id.main_container) {
    @Override
    protected android.app.Fragment createFragment(String screenKey, Object data) {
      switch (screenKey) {
        case Screens.TODO_TASK_LIST:
          return new ToDoTaskListFragment();
        case Screens.TODO_TASK_VIEW:
          ShowToDoTaskDetailsAction action = (ShowToDoTaskDetailsAction) data;
          return ToDoTaskDetailsFragment.initInstance(action.task(), action.editMode());
        default:
          return null;
      }
    }

    @Override
    protected void showSystemMessage(String message) {
      Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void exit() {
      finish();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    App.getAppComponent().inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      navigator.applyCommand(new Replace(Screens.TODO_TASK_LIST, null));
    }
  }

  @Override
  protected void onResumeFragments() {
    super.onResumeFragments();
    navigatorHolder.setNavigator(navigator);
  }

  @Override
  protected void onPause() {
    navigatorHolder.removeNavigator();
    super.onPause();
  }

  @Override
  public void onBackPressed() {
    Fragment fragment = getFragmentManager().findFragmentById(R.id.main_container);
    if (fragment != null &&
        fragment instanceof BackButtonListener &&
        ((BackButtonListener) fragment).onBackPressed()) {
      return;
    }
    router.exit();
  }
}
