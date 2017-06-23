package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.android.internal.util.Predicate;
import dagger.android.AndroidInjection;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;

import javax.inject.Inject;
import java.util.List;

public class ToDoListFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<List<ToDoTask>> {
  @Inject
  ToDoTaskRepository repository;

  private ProgressBar progressBar;
  private RecyclerView rvToDoList;
  private ToDoListAdapter adapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
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
    rvToDoList.setAdapter(adapter = new ToDoListAdapter());
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
    loadToDoList();
  }

  public void onStartLoading() {
    rvToDoList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  public void onFinishLoading() {
    rvToDoList.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }


  private void loadToDoList() {
    onStartLoading();
    getLoaderManager().initLoader(1, null, this).forceLoad();
  }

  private void onToDoTasksLoaded(List<ToDoTask> toDoTasks) {
    adapter.setTasks(toDoTasks);
    onFinishLoading();
  }

  @Override
  public Loader<List<ToDoTask>> onCreateLoader(int id, Bundle args) {
    return new ToDoTasksLoader(getActivity(), repository);
  }

  @Override
  public void onLoadFinished(Loader<List<ToDoTask>> loader, List<ToDoTask> toDoTasks) {
    onToDoTasksLoaded(toDoTasks);
  }

  @Override
  public void onLoaderReset(Loader<List<ToDoTask>> loader) {
    onToDoTasksLoaded(null);
  }

  private static class ToDoTasksLoader extends AsyncTaskLoader<List<ToDoTask>> {

    ToDoTaskRepository repository;

    ToDoTasksLoader(Context context, ToDoTaskRepository repository) {
      super(context);
      this.repository = repository;
    }

    @Override
    public List<ToDoTask> loadInBackground() {
      return repository.query(new Predicate<ToDoTask>() {
        @Override
        public boolean apply(ToDoTask toDoTask) {
          return true;
        }
      });
    }
  }
}