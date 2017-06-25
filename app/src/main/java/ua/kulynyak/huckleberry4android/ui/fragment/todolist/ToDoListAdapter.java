package ua.kulynyak.huckleberry4android.ui.fragment.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import ua.kulynyak.huckleberry4android.R;
import ua.kulynyak.huckleberry4android.domain.ToDoTask;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder>
    implements Filterable {

  private static final int VIEW_TYPE_TASK = 0;
  private static final int VIEW_TYPE_EMPTY = 1;

  private List<ToDoTask> tasks = Collections.emptyList();
  private List<ToDoTask> filteredTasks = tasks;
  private TasksFilter tasksFilter;

  public void setTasks(List<ToDoTask> newTasks) {
    if (newTasks == null) {
      tasks = Collections.emptyList();
    } else {
      tasks = newTasks;
    }
    filteredTasks = tasks;
    getFilter();
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
      parent.bind(filteredTasks.get(position));
    }
  }

  @Override
  public int getItemCount() {
    return filteredTasks.size() == 0 ? 1 : filteredTasks.size();
  }

  @Nullable
  public ToDoTask getItemAt(int position) {
    if (filteredTasks.size() > position) {
      return filteredTasks.get(position);
    }
    return null;
  }

  @Override
  public int getItemViewType(int position) {
    return filteredTasks.size() == 0 ? VIEW_TYPE_EMPTY : VIEW_TYPE_TASK;
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

  @Override
  public Filter getFilter() {
    if (tasksFilter == null) {
      tasksFilter = new TasksFilter();
    }
    return tasksFilter;
  }

  /**
   * Custom filter for tasks
   * Filter content in tasks according to the search text
   */
  private class TasksFilter extends Filter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults filterResults = new FilterResults();
      if (constraint == null || constraint.length() <= 0) {
        filterResults.count = tasks.size();
        filterResults.values = tasks;
        return filterResults;
      }
      List<ToDoTask> tempList = new ArrayList<>(tasks.size());
      // search content in tasks
      for (ToDoTask task : tasks) {
        String preparedConstraint = constraint.toString().toLowerCase();
        if (task.getTitle().toLowerCase().contains(preparedConstraint) ||
            task.getDescription().toLowerCase().contains(preparedConstraint)) {
          tempList.add(task);
        }
      }
      filterResults.count = tempList.size();
      filterResults.values = tempList;
      return filterResults;
    }

    /**
     * Notify about filtered list to ui
     *
     * @param constraint text
     * @param results    filtered result
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      filteredTasks = (List<ToDoTask>) results.values;
      notifyDataSetChanged();
    }
  }
}
