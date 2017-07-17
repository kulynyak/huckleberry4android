package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

  private static final int VIEW_TYPE_TASK = 0;
  private static final int VIEW_TYPE_EMPTY = 1;

  private List<ToDoTask> tasks = Collections.emptyList();

  public void setTasks(@Nonnull List<ToDoTask> newTasks) {
    tasks = newTasks;
    notifyDataSetChanged();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ViewHolder viewHolder;
    if (viewType == VIEW_TYPE_EMPTY) {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.todo_list_item_empty, parent, false);
      viewHolder = new ViewHolderEmpty(view);
    } else {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.todo_list_item, parent, false);
      viewHolder = new ViewHolderTasks(view);
    }
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder parent, int position) {
    if (getItemViewType(position) == VIEW_TYPE_TASK) {
      parent.bind(tasks.get(position));
    }
  }

  @Override
  public int getItemCount() {
    return tasks.size() == 0 ? 1 : tasks.size();
  }

  @Override
  public int getItemViewType(int position) {
    return tasks.size() == 0 ? VIEW_TYPE_EMPTY : VIEW_TYPE_TASK;
  }

  static abstract class ViewHolder extends RecyclerView.ViewHolder {
    ViewHolder(View itemView) {
      super(itemView);
    }

    abstract void bind(ToDoTask task);
  }

  private static class ViewHolderEmpty extends ViewHolder {
    ViewHolderEmpty(View itemView) {
      super(itemView);
    }

    @Override
    void bind(ToDoTask task) {

    }
  }

  private static class ViewHolderTasks extends ViewHolder {
    final TextView title;
    final CheckBox isDone;
    final TextView description;

    ViewHolderTasks(View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.tvTitle);
      isDone = (CheckBox) itemView.findViewById(R.id.cbDone);
      description = (TextView) itemView.findViewById(R.id.tvDescription);
    }

    @Override
    void bind(ToDoTask task) {
      title.setText(task.getTitle());
      isDone.setChecked(task.isDone());
      description.setText(task.getDescription());
    }
  }
}
