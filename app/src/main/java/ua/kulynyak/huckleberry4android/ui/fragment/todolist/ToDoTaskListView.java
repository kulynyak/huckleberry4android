package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ToDoTaskListView extends MvpView {

  void onListLoaded(List<ToDoTask> tasks);

  void onStartLoading();

  void onFinishLoading();

}
