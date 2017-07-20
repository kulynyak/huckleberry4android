package ua.kulynyak.huckleberry4android.domain.bus;


public class ToDoTaskCreatedAction {
  private final long taskId;

  public ToDoTaskCreatedAction(long taskId) {
    this.taskId = taskId;
  }

  public long taskId() {
    return taskId;
  }
}
