package org.afabl.bunny.util;

/**
 * Represents a supplier of results.
 *
 * @param <T> the type of results supplied by this supplier
 */
public interface Supplier<T> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  T get();
}
