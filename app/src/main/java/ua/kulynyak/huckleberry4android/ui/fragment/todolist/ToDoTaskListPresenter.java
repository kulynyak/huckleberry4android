package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Pair;
import com.android.internal.util.Predicate;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@InjectViewState
public class ToDoTaskListPresenter extends MvpPresenter<ToDoTaskListView> {
  @Inject
  ToDoTaskRepository repository;

  private String filterString = "";
  private List<ToDoTask> tasks = null;
  private List<ToDoTask> filteredTasks = Collections.emptyList();

  public ToDoTaskListPresenter() {
    App.getAppComponent().inject(this);
  }

  public void reloadTasks() {
    // in this implementation we don't know what happened in ToDoTaskDetailsFragment
    tasks = null;
    loadTasks(filterString);
  }

  public void loadTasks(@Nonnull String filterStr) {
    if (tasks != null && filterString.equalsIgnoreCase(filterStr)) {
      return;
    }
    getViewState().onStartLoading();
    filterString = filterStr.toLowerCase();
    final Handler handler = new Handler();
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        final Pair<List<ToDoTask>, List<ToDoTask>> result =
            loadFromRepository(repository, tasks, filterString);
        handler.post(new Runnable() {
          @Override
          public void run() {
            onTasksFiltered(result.first, result.second);
          }
        });
      }
    });
    thread.start();
  }

  public void showTask(int position, boolean edit) {
    ToDoTask task = position == -1 ? null : filteredTasks.get(position);
    getViewState().onShowTask(task, edit);
  }

  private static Pair<List<ToDoTask>, List<ToDoTask>> loadFromRepository(
      ToDoTaskRepository repository, List<ToDoTask> tasks, String filterStr) {
    if (tasks == null) {
      tasks = repository.query(new Predicate<ToDoTask>() {
        @Override
        public boolean apply(ToDoTask toDoTask) {
          return true;
        }
      });
    }
    List<ToDoTask> filteredTasks;
    if (!filterStr.isEmpty()) {
      SystemClock.sleep(300); // imitate long filtering
      filteredTasks = new ArrayList<>(tasks.size());
      for (ToDoTask task : tasks) {
        if (task.getTitle().toLowerCase().contains(filterStr) ||
            task.getDescription().toLowerCase().contains(filterStr)) {
          filteredTasks.add(task);
        }
      }
    } else {
      filteredTasks = tasks;
    }
    return new Pair<>(tasks, filteredTasks);
  }

  private void onTasksFiltered(List<ToDoTask> tasks, List<ToDoTask> filteredTasks) {
    this.tasks = tasks;
    this.filteredTasks = filteredTasks;
    getViewState().onListLoaded(filteredTasks);
    getViewState().onFinishLoading();
  }
}
