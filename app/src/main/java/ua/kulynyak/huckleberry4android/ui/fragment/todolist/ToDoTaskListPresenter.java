package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Pair;
import com.android.internal.util.Predicate;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;
import ua.kulynyak.huckleberry4android.domain.bus.ShowToDoTaskDetailsFragmentAction;
import ua.kulynyak.huckleberry4android.domain.bus.ToDoTaskCreatedAction;
import ua.kulynyak.huckleberry4android.domain.bus.ToDoTaskDeletedAction;
import ua.kulynyak.huckleberry4android.domain.bus.ToDoTaskUpdatedAction;

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
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroy() {
    EventBus.getDefault().unregister(this);
    super.onDestroy();
  }

  private void reloadTasks(Runnable onLoadDone) {
    // in this implementation we reload tasks only if task created, updated or deleted
    tasks = null;
    loadTasks(filterString, onLoadDone);
  }

  public void loadTasks(@Nonnull String filterStr, final Runnable onLoadDone) {
    if (tasks != null && filterString.equalsIgnoreCase(filterStr)) {
      if (onLoadDone != null) {
        onLoadDone.run();
      }
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
            if (onLoadDone != null) {
              onLoadDone.run();
            }
          }
        });
      }
    });
    thread.start();
  }

  public void showTask(int position, boolean edit) {
    ToDoTask task = position == -1 ? null : filteredTasks.get(position);
    EventBus.getDefault().post(new ShowToDoTaskDetailsFragmentAction(task, edit));
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

  @Subscribe
  void onToDoTaskDeleted(ToDoTaskDeletedAction action) {
    reloadTasks(null);
  }

  @Subscribe
  void onToDoTaskCreated(ToDoTaskCreatedAction action) {
    final long taskId = action.taskId();
    reloadTasks(new Runnable() {
      @Override
      public void run() {
        int position = 0;
        for (ToDoTask task : filteredTasks) {
          if (task.getId() == taskId) {
            getViewState().onScrollToPosition(position);
            break;
          }
          position++;
        }
      }
    });
  }

  @Subscribe
  void onToDoTaskUpdated(ToDoTaskUpdatedAction action) {
    reloadTasks(null);
  }
}
