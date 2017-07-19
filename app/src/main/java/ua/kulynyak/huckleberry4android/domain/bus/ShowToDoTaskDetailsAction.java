package ua.kulynyak.huckleberry4android.domain.bus;


import ua.kulynyak.huckleberry4android.domain.ToDoTask;

public class ShowToDoTaskDetailsAction {
  private final ToDoTask task;
  private final boolean editMode;

  public ShowToDoTaskDetailsAction(ToDoTask task, boolean editMode) {
    this.task = task;
    this.editMode = editMode;
  }

  public ToDoTask task() {
    return task;
  }

  public boolean editMode() {
    return editMode;
  }
}
