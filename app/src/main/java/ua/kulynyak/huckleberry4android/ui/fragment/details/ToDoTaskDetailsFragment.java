package ua.kulynyak.huckleberry4android.ui.fragment.details;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.*;
import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import org.greenrobot.eventbus.EventBus;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;
import ua.kulynyak.huckleberry4android.domain.bus.ShowToDoTaskDetailsAction;
import ua.kulynyak.huckleberry4android.ui.commons.BackButtonListener;
import ua.kulynyak.huckleberry4android.ui.commons.UiUtils;

import javax.annotation.Nullable;

public class ToDoTaskDetailsFragment extends MvpFragment
                                     implements ToDoTaskDetailsView, BackButtonListener {

  private static final int VIEW_BG_COLOR = Color.parseColor("#FFFFFF");
  private static final int EDIT_BG_COLOR = Color.parseColor("#EEEEEE");

  @InjectPresenter
  ToDoTaskDetailsPresenter presenter;

  @BindView(R.id.etTitle)
  EditText etTitle;
  @BindView(R.id.cbDone)
  CheckBox cbDone;
  @BindView(R.id.etDescription)
  EditText etDescription;
  @BindView(R.id.btnEditSave)
  Button btnEditSave;
  @BindView(R.id.btnDelete)
  Button btnDelete;

  private Unbinder unbinder;

  private View.OnClickListener viewModeClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      presenter.editTask();
    }
  };

  private View.OnClickListener editModeClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      presenter.saveToDoTask(etTitle.getText().toString(), etDescription.getText().toString(),
          cbDone.isChecked());
    }
  };

  public static ToDoTaskDetailsFragment initInstance(@Nullable ToDoTask task, boolean edit) {
    ToDoTaskDetailsFragment fragment = new ToDoTaskDetailsFragment();
    EventBus.getDefault().postSticky(new ShowToDoTaskDetailsAction(task, edit));
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.todo_details, container, false);
    unbinder = ButterKnife.bind(this, rootView);
    return rootView;
  }

  @OnFocusChange({R.id.etTitle, R.id.etDescription})
  void onFocusChange(View v, boolean hasFocus) {
    moveCursorToTheEndOfText((EditText) v, hasFocus);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @OnClick(R.id.btnDelete)
  void onToDoTaskDeleteClicked() {
    presenter.deleteToDoTask();
  }

  private void moveCursorToTheEndOfText(EditText editText, boolean hasFocus) {
    if (hasFocus) {
      editText.setSelection((editText.getText().length()));
    }
  }

  private int getBackgroundColor(boolean editMode) {
    return editMode ? EDIT_BG_COLOR : VIEW_BG_COLOR;
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
    updateEditTextState(etTitle, editMode);
    updateEditTextState(etDescription, editMode);

    cbDone.setClickable(editMode);
    cbDone.setBackgroundColor(getBackgroundColor(editMode));

    btnEditSave.setText(editMode ? R.string.save : R.string.edit);
    btnEditSave.setEnabled(true);
    btnEditSave.setOnClickListener(editMode ? editModeClickListener : viewModeClickListener);

    btnDelete.setEnabled(!isNew);
    UiUtils.hideKeyboardFrom(getActivity(), getView());
  }

  private void updateEditTextState(EditText etControl, boolean editMode) {
    etControl.setFocusable(editMode);
    etControl.setFocusableInTouchMode(editMode);
    etControl.setBackgroundColor(getBackgroundColor(editMode));
  }

  @Override
  public boolean onBackPressed() {
    presenter.onBackCommandClick();
    return true;
  }
}
