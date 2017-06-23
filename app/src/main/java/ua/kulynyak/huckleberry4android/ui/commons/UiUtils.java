package ua.kulynyak.huckleberry4android.ui.commons;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class UiUtils {
  private UiUtils() {
  }

  /**
   * For fragments call UiUtils.hideKeyboardFrom(getActivity(), getView());
   */
  public static void hideKeyboardFrom(Context context, View view) {
    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }
}
