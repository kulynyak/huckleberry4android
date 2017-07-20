package ua.kulynyak.huckleberry4android.ui.fragment.details;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import ua.kulynyak.huckleberry4android.App;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;
import ua.kulynyak.huckleberry4android.domain.bus.ShowToDoTaskDetailsAction;
import ua.kulynyak.huckleberry4android.domain.bus.ToDoTaskCreatedAction;
import ua.kulynyak.huckleberry4android.domain.bus.ToDoTaskDeletedAction;
import ua.kulynyak.huckleberry4android.domain.bus.ToDoTaskUpdatedAction;

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
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroy() {
    EventBus.getDefault().unregister(this);
    super.onDestroy();
  }

  @Subscribe(sticky = true)
  public void onShowToDoTask(ShowToDoTaskDetailsAction action) {
    EventBus.getDefault().removeStickyEvent(action);
    editMode = action.editMode();
    isNew = action.task() == null;
    toDoTask = isNew ? new ToDoTask("") : action.task();
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
    if (isNew) {
      isNew = false;
      EventBus.getDefault().post(new ToDoTaskCreatedAction(toDoTask.getId()));
      getViewState().onToDoTaskSaved();
    } else {
      EventBus.getDefault().post(new ToDoTaskUpdatedAction(toDoTask.getId()));
      getViewState().onToDoTaskSaved();
    }
  }

  public void deleteToDoTask() {
    repository.remove(toDoTask);
    isNew = false;
    editMode = false;
    EventBus.getDefault().post(new ToDoTaskDeletedAction(toDoTask.getId()));
    getViewState().onToDoTaskDeleted();
  }
}
