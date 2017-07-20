package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.ProgressBar;
import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.ui.commons.BackButtonListener;
import ua.kulynyak.huckleberry4android.ui.commons.ItemClickSupport;

import javax.annotation.Nullable;
import java.util.List;

public class ToDoTaskListFragment extends MvpFragment
                                  implements ToDoTaskListView, ToDoTasksSearchView,
                                             BackButtonListener {

  @InjectPresenter
  ToDoTaskListPresenter presenter;

  @InjectPresenter
  ToDoTasksSearchPresenter searchPresenter;

  private ProgressBar progressBar;
  private RecyclerView rvToDoList;
  private SearchView searchView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    if (savedInstanceState == null) {
      presenter.loadTasks("", null);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.todo_list, container, false);
    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
    progressBar.setVisibility(View.GONE);
    rvToDoList = (RecyclerView) rootView.findViewById(R.id.rvToDoList);
    rvToDoList.setVisibility(View.VISIBLE);
    rvToDoList.setAdapter(new ToDoListAdapter());
    ItemClickSupport.addTo(rvToDoList)
        .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
          @Override
          public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            presenter.showTask(position, false);
          }
        });
    FloatingActionButton fabAddItem = (FloatingActionButton) rootView.findViewById(R.id.fabAddItem);
    fabAddItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.showTask(-1, true);
      }
    });
    return rootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
    rvToDoList.setLayoutManager(layoutManager);
    rvToDoList.setItemAnimator(new DefaultItemAnimator());
  }

  @Override
  public void onListLoaded(List<ToDoTask> tasks) {
    ((ToDoListAdapter) rvToDoList.getAdapter()).setTasks(tasks);
  }

  public void onStartLoading() {
    rvToDoList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  public void onFinishLoading() {
    rvToDoList.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void onSearch(String searchQuery) {
    presenter.loadTasks(searchQuery, null);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main, menu);
    initSearchView(menu);
  }

  @Override
  public void onShowSearchView(String searchQuery, boolean isSubmitted, boolean isOpened) {
    if (!isOpened) {
      return;
    }

    searchView.setIconified(false);
    searchView.setQuery(searchQuery, isSubmitted);
    if (isSubmitted) {
      searchView.clearFocus();
    }
  }

  @Override
  public void onScrollToPosition(int position) {
    rvToDoList.getLayoutManager().scrollToPosition(position);
  }

  private void initSearchView(Menu menu) {
    MenuItem searchItem = menu.findItem(R.id.action_search);
    searchView = (SearchView) searchItem.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        searchPresenter.performSearch(query, true);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        searchPresenter.performSearch(newText, false);
        return true;
      }

    });
    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
      @Override
      public boolean onClose() {
        searchPresenter.setOpened(false);
        return false;
      }
    });
    searchView.setOnSearchClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchPresenter.setOpened(true);
      }
    });
    searchPresenter.restoreSearchViewState();
  }

  @Override
  public boolean onBackPressed() {
    presenter.onBackCommandClick();
    return true;
  }
}
