package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.*;
import android.widget.ProgressBar;
import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.ui.activity.MainActivity;
import ua.kulynyak.huckleberry4android.ui.commons.ItemClickSupport;

import javax.annotation.Nullable;
import java.util.List;

public class ToDoTaskListFragment extends MvpFragment implements ToDoTaskListView {


  private static final String SEARCH_STATE_KEY = "huckleberrySearchStateKey";
  private static final String SEARCH_QUERY_KEY = "huckleberrySearchQueryKey";
  private static final String SEARCH_SUBMITTED_KEY = "huckleberrySearchSubmittedKey";

  @InjectPresenter
  ToDoTaskListPresenter presenter;

  private ProgressBar progressBar;
  private RecyclerView rvToDoList;
  private SearchView searchView;
  private String searchQuery;
  private boolean searchSubmitted;
  private Bundle searchState = null;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
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
  public void onResume() {
    super.onResume();
    presenter.reloadTasks();
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
  public void onShowTask(ToDoTask task, boolean edit) {
    ((MainActivity) getActivity()).showDetailFragment(task, edit);
  }

  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      restoreSearchState(savedInstanceState.getBundle(SEARCH_STATE_KEY));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBundle(SEARCH_STATE_KEY, saveSearchState());
  }

  @Override
  public void onDestroyView() {
    saveSearchState();
    super.onDestroyView();
  }

  private Bundle saveSearchState() {
    if (searchView == null) {
      searchState = null;
      return null;
    }
    if (searchState == null) {
      searchState = new Bundle();
    }
    searchQuery = searchView.getQuery().toString();
    searchState.putString(SEARCH_QUERY_KEY, searchQuery);
    searchState.putBoolean(SEARCH_SUBMITTED_KEY, searchSubmitted);
    return searchState;
  }

  private void restoreSearchState(Bundle restoredState) {
    Bundle bundle = searchState == null ? restoredState : searchState;
    if (bundle == null) return;
    searchQuery = bundle.getString(SEARCH_QUERY_KEY);
    searchSubmitted = bundle.getBoolean(SEARCH_SUBMITTED_KEY);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main, menu);
    initSearchView(menu);
  }

  private void initSearchView(Menu menu) {
    MenuItem searchItem = menu.findItem(R.id.action_search);
    searchView = (SearchView) searchItem.getActionView();
    if (!TextUtils.isEmpty(searchQuery)) {
      // workaround
      searchView.post(new Runnable() {
        @Override
        public void run() {
          searchView.setIconified(false);
          searchView.setQuery(searchQuery, searchSubmitted);
          if (searchSubmitted) {
            searchView.clearFocus();
          }
        }
      });
    }

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        searchSubmitted = true;
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        searchSubmitted = false;
        presenter.loadTasks(newText);
        return true;
      }
    });
  }
}
