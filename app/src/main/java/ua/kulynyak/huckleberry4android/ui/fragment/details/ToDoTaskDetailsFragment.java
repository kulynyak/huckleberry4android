package ua.kulynyak.huckleberry4android.ui.fragment.details;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.ui.commons.UiUtils;

import javax.annotation.Nullable;

public class ToDoTaskDetailsFragment extends MvpFragment implements ToDoTaskDetailsView {

  private static final int VIEW_BG_COLOR = Color.parseColor("#FFFFFF");
  private static final int EDIT_BG_COLOR = Color.parseColor("#EEEEEE");

  private static final String TASK_EDIT = "edit";
  private static final String TASK_ID = "taskId";
  private static final String TASK_TITLE = "title";
  private static final String TASK_DESCRIPTION = "description";
  private static final String TASK_IS_DONE = "isDone";

  @InjectPresenter
  ToDoTaskDetailsPresenter presenter;

  private EditText etTitle;
  private CheckBox cbDone;
  private EditText etDescription;
  private Button btnEditSave;
  private Button btnDelete;

  public static ToDoTaskDetailsFragment initInstance(@Nullable ToDoTask task, boolean edit) {
    ToDoTaskDetailsFragment fragment = new ToDoTaskDetailsFragment();
    Bundle args = new Bundle();
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

    btnDelete = (Button) rootView.findViewById(R.id.btnDelete);
    btnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.deleteToDoTask();
      }
    });
    return rootView;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      ToDoTask task = null;
      boolean editMode = false;
      Bundle args = getArguments();
      if (args != null) {
        editMode = args.getBoolean("edit", false);
        long id = args.getLong(TASK_ID, 0);
        if (id != 0) {
          task = new ToDoTask(args.getString(TASK_TITLE))
              .setDescription(args.getString(TASK_DESCRIPTION, ""))
              .setDone(args.getBoolean(TASK_IS_DONE))
              .setId(id);
        }
      }
      presenter.showToDoTask(editMode, task);
    }
  }

  private void moveCursorToTheEndOfText(EditText editText, boolean hasFocus) {
    if (hasFocus) {
      editText.setSelection((editText.getText().length()));
    }
  }

  private int getBackgroundColor(boolean editMode) {
    return editMode ? EDIT_BG_COLOR : VIEW_BG_COLOR;
  }

  private void lockButtons() {
    btnDelete.setEnabled(false);
    btnEditSave.setEnabled(false);
  }

  @Override
  public void showToDoTask(ToDoTask task) {
    etTitle.setText(task.getTitle());
    cbDone.setChecked(task.isDone());
    etDescription.setText(task.getDescription());
  }

  @Override
  public void setMode(boolean edit, boolean isNew) {
    updateControls(edit, isNew);
  }

  private void updateControls(boolean editMode, boolean isNew) {
    etTitle.setFocusable(editMode);
    etTitle.setFocusableInTouchMode(editMode);
    etTitle.setBackgroundColor(getBackgroundColor(editMode));

    cbDone.setClickable(editMode);
    cbDone.setBackgroundColor(getBackgroundColor(editMode));

    etDescription.setFocusable(editMode);
    etDescription.setFocusableInTouchMode(editMode);
    etDescription.setBackgroundColor(getBackgroundColor(editMode));

    btnEditSave.setText(editMode ? R.string.save : R.string.edit);
    btnEditSave.setEnabled(true);
    if (editMode) {
      btnEditSave.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          presenter.saveToDoTask(etTitle.getText().toString(), etDescription.getText().toString(),
              cbDone.isChecked());
        }
      });
    } else {
      btnEditSave.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          presenter.editTask();
        }
      });
    }

    btnDelete.setEnabled(!isNew);
    UiUtils.hideKeyboardFrom(getActivity(), getView());
  }

  @Override
  public void onToDoTaskSaved() {
    updateControls(false, false);
  }

  @Override
  public void onToDoTaskDeleted() {
    etTitle.setText("Deleted");
    cbDone.setChecked(false);
    etDescription.setText("");
    updateControls(false, true);
    lockButtons();
  }

}
