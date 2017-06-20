package ua.kulynyak.huckleberry4android.domain;

import java.util.Date;

public class ToDoTask {
  private long id;
  private String title;
  private boolean done;
  private String description;

  public ToDoTask(String title) {
    // unique id
    id = new Date().getTime();
    this.title = title;
  }

  public ToDoTask(ToDoTask task) {
    // unique id
    id = task.id;
    title = task.title;
    done = task.done;
    description = task.description;
  }

  public long getId() {
    return id;
  }

  public ToDoTask setId(long id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public ToDoTask setTitle(String title) {
    this.title = title;
    return this;
  }

  public boolean isDone() {
    return done;
  }

  public ToDoTask setDone(boolean done) {
    this.done = done;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public ToDoTask setDescription(String description) {
    this.description = description;
    return this;
  }
}
