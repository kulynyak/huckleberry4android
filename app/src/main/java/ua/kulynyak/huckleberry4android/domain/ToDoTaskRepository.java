package ua.kulynyak.huckleberry4android.domain;

import com.android.internal.util.Predicate;

import javax.annotation.Nonnull;
import java.util.List;

public interface ToDoTaskRepository {
  /**
   * @param toDoTask to be added to repository
   */
  void add(@Nonnull ToDoTask toDoTask);

  /**
   * @param toDoTask to be removed from repository
   */
  void remove(@Nonnull ToDoTask toDoTask);

  /**
   * @param toDoTask to be updated in repository
   */
  void update(@Nonnull ToDoTask toDoTask);

  /**
   * @param predicate todo task selection criteria
   * @return list of {@link ToDoTask} matching {@code predicate} criteria
   */
  List<ToDoTask> query(Predicate<ToDoTask> predicate);
}
