package org.afabl.bunny.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

@State(name = "BunnyHistory", storages = {@Storage("BunnyHistory.xml")})
public class BunnyHistory implements PersistentStateComponent<BunnyHistory> {

    /**
     * A set of actions that can be associated with an event (timestamp).
     */
    public enum Action {
        PROJECT_OPEN, FILE_OPEN, FILE_ACTIVE, FILE_INACTIVE, FILE_CLOSED,
        PROJECT_CLOSE
    }

    private String userId;
    private SortedMap<Long, SortedSet<Action>> events;

    /**
     * Creates a history and associates it with the given user.
     */
    public BunnyHistory(String userId) {
        this.userId = userId;
        events = new TreeMap<Long, SortedSet<Action>>();
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
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @deprecated Use {@link #addEvent(long, Action, Action...)} to add events
     * to the history.
     */
    public SortedMap<Long, SortedSet<Action>> getEvents() {
        return events;
    }

    /**
     * @deprecated Use {@link #addEvent(long, Action, Action...)} to add events
     * to the history.
     */
    public void setEvents(SortedMap<Long, SortedSet<Action>> events) {
        this.events = events;
    }

    /**
     * Adds an event to the history. If an event with the same timestamp
     * already exists, any new given actions are added to the existing event.
     *
     * @param timestamp the timestamp of the event
     * @param action the action to associate with the event
     * @param actions zero or more additional actions to associate with the
     *                event
     */
    public void addEvent(long timestamp, @NotNull Action action,
                         @NotNull Action... actions) {
        if (!events.containsKey(timestamp)) {
            events.put(timestamp, new TreeSet<Action>());
        }
        SortedSet<Action> actionSet = events.get(timestamp);
        actionSet.add(action);
        for (Action elem : actions) {
            actionSet.add(elem);
        }
    }

    @Nullable
    @Override
    public BunnyHistory getState() {
        return this;
    }

    @Override
    public void loadState(BunnyHistory state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Nullable
    public static BunnyHistory getInstance(Project project) {
        return ServiceManager.getService(project, BunnyHistory.class);
    }
}
