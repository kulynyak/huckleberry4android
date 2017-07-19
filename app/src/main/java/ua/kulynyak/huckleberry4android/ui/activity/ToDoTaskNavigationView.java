package ua.kulynyak.huckleberry4android.ui.activity;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;

public interface ToDoTaskNavigationView extends MvpView {

  @StateStrategyType(OneExecutionStateStrategy.class)
  void onShowToDoTask(ToDoTask task, boolean edit);

}
