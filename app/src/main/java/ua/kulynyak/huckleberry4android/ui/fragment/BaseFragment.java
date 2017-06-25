package ua.kulynyak.huckleberry4android.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import dagger.android.AndroidInjection;
import ua.kulynyak.huckleberry4android.domain.ToDoTaskRepository;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class BaseFragment extends Fragment {
  @Inject
  protected ToDoTaskRepository repository;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
  }
}
