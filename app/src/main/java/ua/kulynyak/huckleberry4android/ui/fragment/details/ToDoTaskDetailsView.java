package ua.kulynyak.huckleberry4android.ui.fragment.details;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;


public interface ToDoTaskDetailsView extends MvpView {

  @StateStrategyType(OneExecutionStateStrategy.class)
  void showToDoTask(ToDoTask task);

  @StateStrategyType(AddToEndSingleStrategy.class)
  void setMode(boolean edit, boolean isNew);
}
