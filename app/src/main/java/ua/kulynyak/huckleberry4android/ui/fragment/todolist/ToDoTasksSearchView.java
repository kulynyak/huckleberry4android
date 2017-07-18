package ua.kulynyak.huckleberry4android.ui.fragment.todolist;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;


public interface ToDoTasksSearchView extends MvpView {

  @StateStrategyType(OneExecutionStateStrategy.class)
  void onShowSearchView(String searchQuery, boolean isSubmitted, boolean isOpened);

  @StateStrategyType(AddToEndSingleStrategy.class)
  void onSearch(String searchQuery);
}
