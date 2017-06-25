package ua.kulynyak.huckleberry4android.ui.fragment.details;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.ui.commons.UiUtils;
import ua.kulynyak.huckleberry4android.ui.fragment.BaseFragment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ToDoTaskDetailsFragment extends BaseFragment {

  private static final int VIEW_BG_COLOR = Color.parseColor("#FFFFFF");
  private static final int EDIT_BG_COLOR = Color.parseColor("#EEEEEE");

  private static final String INITIALIZE = "initialize";
  private static final String TASK_EDIT = "edit";
  private static final String TASK_IS_DELETED = "isDeleted";
  private static final String TASK_ID = "taskId";
  private static final String TASK_TITLE = "title";
  private static final String TASK_DESCRIPTION = "description";
  private static final String TASK_IS_DONE = "isDone";

  private EditText etTitle;
  private CheckBox cbDone;
  private EditText etDescription;
  private Button btnEditSave;
  private Button btnDelete;

  private boolean editMode = true;
  private boolean isNew;
  private boolean isDeleted;
  private ToDoTask task;

  public static ToDoTaskDetailsFragment initInstance(@Nullable ToDoTask task, boolean edit) {
    ToDoTaskDetailsFragment fragment = new ToDoTaskDetailsFragment();
    Bundle args = new Bundle();
    args.putBoolean(INITIALIZE, true);
    args.putBoolean(TASK_EDIT, edit);
    if (task != null) {
      args.putLong(TASK_ID, task.getId());
      args.putBoolean(TASK_IS_DONE, task.isDone());
      args.putString(TASK_TITLE, task.getTitle());
      args.putString(TASK_DESCRIPTION, task.getDescription());
    }
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.todo_details, container, false);
    etTitle = (EditText) rootView.findViewById(R.id.etTitle);
    etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        moveCursorToTheEndOfText((EditText) v, hasFocus);
      }
    });

    cbDone = (CheckBox) rootView.findViewById(R.id.cbDone);
    etDescription = (EditText) rootView.findViewById(R.id.etDescription);
    etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        moveCursorToTheEndOfText((EditText) v, hasFocus);
      }
    });

    btnEditSave = (Button) rootView.findViewById(R.id.btnEditSave);
    btnEditSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editOrSaveButtonClicked();
      }
    });

    btnDelete = (Button) rootView.findViewById(R.id.btnDelete);
    btnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteButtonClicked();
      }
    });
    return rootView;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if ((args != null && args.getBoolean(INITIALIZE))) {
      args.putBoolean(INITIALIZE, false);
    } else {
      if (savedInstanceState != null) {
        args = savedInstanceState;
      }
    }
    if (args != null) {
      editMode = args.getBoolean(TASK_EDIT, false);
      isDeleted = args.getBoolean(TASK_IS_DELETED);
      long id = args.getLong(TASK_ID, 0);
      task = new ToDoTask(args.getString(TASK_TITLE, getEmptyToDoTaskTitle()))
          .setDescription(args.getString(TASK_DESCRIPTION, ""))
          .setDone(args.getBoolean(TASK_IS_DONE));
      isNew = id == 0;
      if (!isNew) {
        task.setId(id);
      }
    } else {
      task = new ToDoTask("");
      isNew = true;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    updateView();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(TASK_EDIT, editMode);
    outState.putLong(TASK_ID, isNew ? 0 : task.getId());
    outState.putString(TASK_TITLE, etTitle.getText().toString());
    outState.putString(TASK_DESCRIPTION, etDescription.getText().toString());
    outState.putBoolean(TASK_IS_DONE, cbDone.isChecked());
    outState.putBoolean(TASK_IS_DELETED, isDeleted);
  }

  private void moveCursorToTheEndOfText(EditText editText, boolean hasFocus) {
    if (hasFocus) {
      editText.setSelection((editText.getText().length()));
    }
  }

  private void updateView() {
    etTitle.setText(task.getTitle());
    etTitle.setFocusable(editMode);
    etTitle.setFocusableInTouchMode(editMode);
    etTitle.setBackgroundColor(getBackgroundColor());

    cbDone.setChecked(task.isDone());
    cbDone.setClickable(editMode);
    cbDone.setBackgroundColor(getBackgroundColor());

    etDescription.setText(task.getDescription());
    etDescription.setFocusable(editMode);
    etDescription.setFocusableInTouchMode(editMode);
    etDescription.setBackgroundColor(getBackgroundColor());

    btnEditSave.setText(editMode ? R.string.save : R.string.edit);
    btnEditSave.setEnabled(!isDeleted);

    btnDelete.setEnabled(!(isNew || isDeleted));
    UiUtils.hideKeyboardFrom(getActivity(), getView());
  }

  private void editOrSaveButtonClicked() {
    if (editMode) {
      task.setDone(cbDone.isChecked())
          .setTitle(etTitle.getText().toString())
          .setDescription(etDescription.getText().toString());
      saveTask();
    } else {
      editMode = true;
      updateView();
    }
  }

  private void deleteButtonClicked() {
    if (isNew) {
      return;
    }
    removeTask();
  }

  private int getBackgroundColor() {
    return editMode ? EDIT_BG_COLOR : VIEW_BG_COLOR;
  }

  private void saveTask() {
    SaveToDoTask saveToDoTask = new SaveToDoTask();
    saveToDoTask.execute(task);
  }

  private void removeTask() {
    RemoveToDoTask removeToDoTask = new RemoveToDoTask();
    removeToDoTask.execute(task);
  }

  @Nonnull
  private String getEmptyToDoTaskTitle() {
    return isDeleted ? "Deleted" : "";
  }

  private class SaveToDoTask extends AsyncTask<ToDoTask, Void, Void> {
    @Override
    protected void onPreExecute() {
      editMode = false;
      updateView();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      isNew = false;
      updateView();
    }

    @Override
    protected Void doInBackground(ToDoTask... toDoTasks) {
      if (isNew) {
        repository.add(toDoTasks[0]);
      } else {
        repository.update(toDoTasks[0]);
      }
      return null;
    }
  }

  private class RemoveToDoTask extends AsyncTask<ToDoTask, Void, Void> {
    @Override
    protected void onPostExecute(Void aVoid) {
      editMode = false;
      isNew = false;
      task = new ToDoTask(getEmptyToDoTaskTitle());
      isDeleted = true;
      updateView();
    }

    @Override
    protected Void doInBackground(ToDoTask... toDoTasks) {
      if (!isNew) {
        repository.remove(toDoTasks[0]);
      }
      return null;
    }
  }
}
