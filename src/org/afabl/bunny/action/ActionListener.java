package org.afabl.bunny.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a listener of Action events.
 */
public interface ActionListener {

  /**
   * Invoked when the associated Action starts.
   */
  void start();

  /**
   * Invoked when the associated Action changes the User ID.
   */
  void id(@NotNull String id);

  /**
   * Invoked when the associated Action encounters a failure. <b>Note</b>: Invocation
   * of this method does <i>not</i> indicate that the Action has completed.
   */
  void failure(@Nullable Object data);

  /**
   * Invoked when the associated Action completes successfully.
   */
  void success();

  /**
   * Invoked when the associated Action completes because it was cancelled by the
   * user.
   */
  void abort();

  /**
   * A No-Op {@link ActionListener} that performs no operations for any method invocation.
   */
  static final ActionListener NOOP = new ActionListener() {
    @Override
    public void start() {
    }

    @Override
    public void id(@NotNull String id) {
    }

    @Override
    public void failure(@Nullable Object data) {
    }

    @Override
    public void success() {
    }

    @Override
    public void abort() {
    }
  };
}
