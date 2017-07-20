package ua.kulynyak.huckleberry4android.domain.bus;


public class ToDoTaskDeletedAction {
  private final long taskId;

  public ToDoTaskDeletedAction(long taskId) {
    this.taskId = taskId;
  }

  public long taskId() {
    return taskId;
  }
}
