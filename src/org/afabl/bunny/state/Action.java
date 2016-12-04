package org.afabl.bunny.state;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public class Action implements Comparable<Action> {

  /**
   * Types of actions that can be associated with an event (timestamp).
   */
  public enum Type {
    INFO_OPEN, INFO_OK, INFO_CANCEL, STUDY_STARTED, PROJECT_OPEN, FILE_OPEN,
    FILE_ACTIVE, USER_ACTIVE, USER_IDLE, FILE_INACTIVE, FILE_CLOSED,
    PROJECT_CLOSE, SUBMIT_OPEN, SUBMIT_SUCCESS, SUBMIT_FAIL,
    SUBMIT_CANCEL, EXPORT_OPEN, EXPORT_SUCCESS, EXPORT_FAIL,
    EXPORT_CANCEL, STUDY_ENDED
  }

  private static final Logger logger = Logger.getInstance(Action.class);

  private Type type;
  private Object data;

  private Action() {
  };

  public Action(@NotNull Type type) {
    this.type = type;
  }

  public Action(@NotNull Type type, @NotNull Object data) {
    this.type = type;
    this.data = data;
  }

  public Type getType() {
    return type;
  }

  public Object getData() {
    return data;
  }

  public String getJson() {
    if (null == data) {
      return "\"" + type.toString() + "\"";
    } else {
      return "{\"" + type.toString() + "\":\"" + data.toString() + "\"}";
    }
  }

  @Override
  public int compareTo(@NotNull Action o) {
    if (null == type) {
      return null == o.type ? 0 : -1;
    } else {
      return null == o.type ? 1 : this.type.compareTo(o.type);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Action action = (Action) o;

    if (type != action.type) return false;
    return data != null ? data.equals(action.data) : action.data == null;

  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + (data != null ? data.hashCode() : 0);
    return result;
  }
}
