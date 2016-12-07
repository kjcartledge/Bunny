package org.afabl.bunny.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.HashSet;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.CollectionBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "History", storages = {@Storage("History.xml")})
public class History implements PersistentStateComponent<History> {

  private static final Logger logger = Logger.getInstance(History.class);

  @Attribute
  private boolean started;
  @Attribute
  private boolean ended;
  @Attribute
  private String userId;
  @CollectionBean
  private SortedMap<Long, Set<String>> events;

  /**
   * Creates an empty {@link History}.
   */
  public History() {
    events = new TreeMap<Long, Set<String>>();
  }

  /**
   * Retrieves the ID of the user associated with this history.
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Sets the ID of the user associated with this history.
   */
  public synchronized void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * Retrieves whether or not the user has started work.
   */
  public boolean getStarted() {
    return started;
  }

  /**
   * Sets whether or not the user has started work.
   */
  public void setStarted(boolean started) {
    this.started = started;
  }

  /**
   * Retrieves whether or not the user has ended work.
   */
  public boolean getEnded() {
    return ended;
  }

  /**
   * Sets whether or not the user has ended work.
   */
  public void setEnded(boolean ended) {
    this.ended = ended;
  }

  /**
   * Adds an event to the history with a timestamp of the current time. If an
   * event with the same timestamp already exists, any new given action are
   * added to the existing event.
   *
   * @param action  the action to associate with the event
   * @param actions zero or more additional actions to associate with the event
   */
  public void addEvent(@NotNull Action action, @NotNull Action... actions) {
    addEvent(System.currentTimeMillis() / 1000, action, actions);
  }

  /**
   * Adds an event to the history. If an event with the same timestamp
   * already exists, any new given actions are added to the existing event.
   *
   * @param timestamp the timestamp (in milliseconds since the UNIX epoch) of the event
   * @param action    the action to associate with the event
   * @param actions   zero or more additional actions to associate with the event
   */
  public synchronized void addEvent(long timestamp, @NotNull Action action,
                                    @NotNull Action... actions) {
    if (!events.containsKey(timestamp)) {
      events.put(timestamp, new HashSet<String>());
    }
    Set<String> actionSet = events.get(timestamp);
    actionSet.add(action.getJson());
    for (Action a : actions) {
      actionSet.add(a.getJson());
    }

    // Logging
    StringBuilder builder = new StringBuilder();
    builder.append(timestamp);
    builder.append(" : ");
    builder.append(action.getJson());
    for (Action elem : actions) {
      builder.append(", ");
      builder.append(elem.getJson());
    }
    logger.info(builder.toString());
  }

  @Nullable
  @Override
  public History getState() {
    return this;
  }

  @Override
  public void loadState(History state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  /**
   * Exports this history as a (JSON) String.
   *
   * @throws IllegalStateException if userId is null or invalid
   */
  public String getJson() {
    StringBuilder builder = new StringBuilder();
    if (null == userId) {
      throw new IllegalStateException("userId cannot be null when "
              + "exporting.");
    }
    // Validate userId
    if (userId.length() != 8) {
      throw new IllegalStateException("userId must be exactly 8 digits.");
    }
    for (char c : userId.toCharArray()) {
      if (!Character.isDigit(c)) {
        throw new IllegalStateException("userId must be exactly 8 "
                + "digits.");
      }
    }
    builder.append("{\"user_id\":\"");
    builder.append(userId);
    builder.append("\",\"events\":[");
    boolean first = true;
    for (Map.Entry<Long, Set<String>> entry : events.entrySet()) {
      if (first) {
        first = false;
      } else {
        builder.append(",");
      }
      builder.append("{\"time\":");
      builder.append(entry.getKey());
      builder.append(",\"actions\":[");
      // Workaround for ClassCastException...
      List<String> actions = new ArrayList<String>(entry.getValue());
      Iterator<String> iter = actions.iterator();
      builder.append(iter.next());
      while (iter.hasNext()) {
        builder.append(",");
        builder.append(iter.next());
      }
      builder.append("]}");
    }
    builder.append("]}");
    builder.trimToSize();
    return builder.toString();
  }

  @Nullable
  public static History getInstance(Project project) {
    return ServiceManager.getService(project, History.class);
  }
}
