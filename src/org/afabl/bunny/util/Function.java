package org.afabl.bunny.util;

/**
 * Represents a function that takes no arguments and returns void.
 */
public interface Function {

  static final Function NOOP = new Function() {
    @Override
    public void call() {
    }
  };

  /**
   * A void method.
   */
  void call();
}