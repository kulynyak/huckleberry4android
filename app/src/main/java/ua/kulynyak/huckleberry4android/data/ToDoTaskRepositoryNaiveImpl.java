package ua.kulynyak.huckleberry4android.data;

import android.os.SystemClock;
import com.android.internal.util.Predicate;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ToDoTaskRepositoryNaiveImpl implements ToDoTaskRepository {
  private static final int INITIAL_CAPACITY = 30;
  private static final int EXECUTION_DELAY = 1500;

  private final List<ToDoTask> list;

  public ToDoTaskRepositoryNaiveImpl() {
    list = new ArrayList<>(INITIAL_CAPACITY);
    for (int i = 1; i <= INITIAL_CAPACITY; i++) {
      String idx = Integer.toString(i);
      list.add(new ToDoTask("Task " + idx)
          .setDescription("Description for task " + idx)
          .setDone(i % 2 == 0)
          .setId(i));
    }
  }

  @Override
  public void add(@Nonnull ToDoTask toDoTask) {
    list.add(toDoTask);
  }

  @Override
  public void remove(@Nonnull final ToDoTask toDoTask) {
    Iterator<ToDoTask> iterator = list.iterator();
    while (iterator.hasNext()) {
      ToDoTask task = iterator.next();
      if (task.getId() == toDoTask.getId()) {
        iterator.remove();
        break;
      }
    }
  }

  @Override
  public void update(@Nonnull ToDoTask toDoTask) {
    for (ToDoTask task : list) {
      if (task.getId() == toDoTask.getId()) {
        task.setTitle(toDoTask.getTitle());
        task.setDescription(toDoTask.getDescription());
        task.setDone(toDoTask.isDone());
        break;
      }
    }
  }

  @Override
  public List<ToDoTask> query(Predicate<ToDoTask> predicate) {
    // imitate long execution
    SystemClock.sleep(EXECUTION_DELAY);
    List<ToDoTask> results = new ArrayList<>();
    for (ToDoTask task : list) {
      if (predicate.apply(task)) {
        results.add(new ToDoTask(task));
      }
    }
    if (results.isEmpty()) {
      return Collections.emptyList();
    } else {
      return Collections.unmodifiableList(results);
    }
  }
}
