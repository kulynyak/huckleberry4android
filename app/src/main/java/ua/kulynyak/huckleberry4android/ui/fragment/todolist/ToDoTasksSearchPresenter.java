package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ToDoTasksSearchPresenter extends MvpPresenter<ToDoTasksSearchView> {
  private String searchQuery = "";
  private boolean isSubmitted;
  private boolean isOpened;

  public void performSearch(String newSearchQuery, boolean submitted) {
    searchQuery = newSearchQuery == null ? "" : newSearchQuery;
    isSubmitted = submitted;
    getViewState().onSearch(newSearchQuery);
  }

  public void restoreSearchViewState() {
    getViewState().onShowSearchView(searchQuery, isSubmitted, isOpened);
  }

  public void setOpened(boolean opened) {
    isOpened = opened;
  }
}
