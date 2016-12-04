package org.afabl.bunny;

import com.intellij.openapi.diagnostic.Logger;
import org.afabl.bunny.state.Action;
import org.afabl.bunny.state.History;
import org.afabl.bunny.util.Function;
import org.jetbrains.annotations.NotNull;

public class Study {

  private static final Logger logger = Logger.getInstance(Study.class);


  private final Function start;
  private final Function end;
  private History history;

  /**
   * Creates a {@link Study}. If the given {@link History} is already started, the start
   * {@link Function} is run immediately.
   *
   * @param start A
   */
  public Study(Function start, Function end, History history) {
    this.start = null == start ? Function.NOOP : start;
    this.end = null == end ? Function.NOOP : end;
    this.history = history;
    if (history.getStarted()) {
      start.call();
    }
  }

  /**
   * Starts the {@link Study} and runs the provided start function. If the study is already started,
   * this method is a no-op.
   */
  public void start() {
    if (!history.getStarted()) {
      start.call();
      history.setStarted(true);
      history.addEvent(new Action(Action.Type.STUDY_STARTED));
    }
  }

  /**
   * @return whether or not the study has been started
   */
  public boolean isStarted() {
    return history.getStarted();
  }

  /**
   * @return the user id associated with this study
   */
  public String getUserId() {
    return history.getUserId();
  }

  /**
   * Set the user id of this study.
   *
   * @param id the user id to associate with this study
   */
  public void setUserId(@NotNull String id) {
    history.setUserId(id);
  }

  /**
   * @return a JSON representation of this study
   */
  public String getJson() {
    return history.getJson();
  }

  /**
   * Adds at least one {@link Action} to the {@link Study}.
   */
  public void event(Action action, Action... actions) {
    history.addEvent(action, actions);
  }

  /**
   * Ends the {@link Study} and runs the provided end function. If the study is already ended, this
   * method is a no-op.
   */
  public void end() {
    if (!history.getEnded()) {
      end.call();
      history.addEvent(new Action(Action.Type.STUDY_ENDED));
      history.setEnded(true);
    }
  }
}
