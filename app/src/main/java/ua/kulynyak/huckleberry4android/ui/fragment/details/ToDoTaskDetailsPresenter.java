package ua.kulynyak.huckleberry4android.ui.fragment.details;

import com.android.internal.util.Predicate;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;

import javax.inject.Inject;

@InjectViewState
public class ToDoTaskDetailsPresenter extends MvpPresenter<ToDoTaskDetailsView> {
  @Inject
  ToDoTaskRepository repository;

  private ToDoTask toDoTask;

  public boolean isNew() {
    return isNew;
  }

  private boolean editMode;
  private boolean isNew;

  public ToDoTaskDetailsPresenter() {
    App.getAppComponent().inject(this);
  }

  public void showToDoTask(boolean editMode, ToDoTask task) {
    this.editMode = editMode;
    isNew = task == null;
    toDoTask = isNew ? new ToDoTask("") : task;
    getViewState().showToDoTask(toDoTask);
    getViewState().setMode(editMode, isNew);
  }

  @Override
  protected void onFirstViewAttach() {
    super.onFirstViewAttach();
  }

  public void editTask() {
    editMode = true;
    getViewState().setMode(editMode, isNew);
  }

  public void saveToDoTask(String title, String description, boolean done) {
    toDoTask.setTitle(title);
    toDoTask.setDescription(description);
    toDoTask.setDone(done);
    repository.update(toDoTask);
    isNew = false;
    getViewState().onToDoTaskSaved();
  }

  public void deleteToDoTask() {
    repository.remove(toDoTask);
    isNew = false;
    editMode = false;
    getViewState().onToDoTaskDeleted();
  }

  private static class SingleTaskPredicate implements Predicate<ToDoTask> {

    private final long taskId;

    private SingleTaskPredicate(long taskId) {
      this.taskId = taskId;
    }

    @Override
    public boolean apply(ToDoTask toDoTask) {
      return toDoTask.getId() == taskId;
    }

  }
}
