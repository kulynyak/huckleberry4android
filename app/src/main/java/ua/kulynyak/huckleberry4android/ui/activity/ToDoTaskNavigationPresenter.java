package ua.kulynyak.huckleberry4android.ui.activity;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import ua.kulynyak.huckleberry4android.domain.bus.ShowToDoTaskDetailsFragmentAction;

@InjectViewState
public class ToDoTaskNavigationPresenter extends MvpPresenter<ToDoTaskNavigationView> {

  public ToDoTaskNavigationPresenter() {
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroy() {
    EventBus.getDefault().unregister(this);
    super.onDestroy();
  }

  @Subscribe
  void onShowToDoTask(ShowToDoTaskDetailsFragmentAction action) {
    getViewState().onShowToDoTask(action.task(), action.editMode());
  }
}
