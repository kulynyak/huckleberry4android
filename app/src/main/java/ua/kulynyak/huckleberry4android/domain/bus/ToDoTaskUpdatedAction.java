package ua.kulynyak.huckleberry4android.domain.bus;


public class ToDoTaskUpdatedAction {
  private final long taskId;

  public ToDoTaskUpdatedAction(long taskId) {
    this.taskId = taskId;
  }

  public long taskId() {
    return taskId;
  }
}
