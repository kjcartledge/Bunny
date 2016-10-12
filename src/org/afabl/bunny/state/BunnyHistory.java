package org.afabl.bunny.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@State(name = "BunnyHistory", storages = {@Storage("BunnyHistory.xml")})
public class BunnyHistory implements PersistentStateComponent<BunnyHistory> {

    /**
     * A set of actions that can be associated with an event (timestamp).
     */
    public enum Action {
        INFO_OPEN, INFO_OK, INFO_CANCEL, STUDY_STARTED, PROJECT_OPEN, FILE_OPEN,
        FILE_ACTIVE, USER_ACTIVE, USER_IDLE, FILE_INACTIVE, FILE_CLOSED,
        PROJECT_CLOSE, SUBMIT_OPEN, SUBMIT_OK, SUBMIT_CANCEL, EXPORT_OPEN,
        EXPORT_OK, EXPORT_CANCEL, STUDY_ENDED
    }

    private static final Logger logger = Logger.getInstance(BunnyHistory.class);


    private boolean started;
    private String userId;
    private SortedMap<Long, SortedSet<Action>> events;

    /**
     * Creates an empty {@link BunnyHistory}.
     */
    public BunnyHistory() {
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
     * Adds an event to the history with a timestamp of the current time. If an
     * event with the same timestamp already exists, any new given action are
     * added to the existing event.
     *
     * @param action the action to associate with the event
     * @param actions zero or more additional actions to associate with the
     *                event
     */
    public void addEvent(@NotNull Action action, @NotNull Action... actions) {
        addEvent(System.currentTimeMillis(), action, actions);
    }

    /**
     * Adds an event to the history. If an event with the same timestamp
     * already exists, any new given actions are added to the existing event.
     *
     * @param timestamp the timestamp (in milliseconds since the UNIX epoch) of
     *                  the event
     * @param action the action to associate with the event
     * @param actions zero or more additional actions to associate with the
     *                event
     */
    public synchronized void addEvent(long timestamp, @NotNull Action action,
                         @NotNull Action... actions) {
        if (!events.containsKey(timestamp)) {
            events.put(timestamp, new TreeSet<Action>());
        }
        SortedSet<Action> actionSet = events.get(timestamp);
        actionSet.add(action);
        Collections.addAll(actionSet, actions);

        // Logging
        StringBuilder builder = new StringBuilder();
        builder.append(timestamp);
        builder.append(" : ");
        builder.append(action);
        for (Action elem : actions) {
            builder.append(", ");
            builder.append(elem);
        }
        logger.info(builder.toString());
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

    /**
     * Exports this history as a (JSON) String.
     *
     * @throws IllegalStateException if userId is null or invalid
     */
    public String exportAsString() {
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
        builder.append("{\"user_id\":");
        builder.append(userId);
        builder.append(",\"events\":[");
        for (Map.Entry<Long, SortedSet<Action>> entry : events.entrySet()) {
            builder.append("{\"time\":");
            builder.append(entry.getKey());
            builder.append(",\"actions\":[");
            List<Action> actions = new ArrayList<Action>(entry.getValue());
            Iterator<Action> iter = actions.iterator();
            builder.append("\"");
            builder.append(iter.next());
            while (iter.hasNext()) {
                builder.append("\",\"");
                builder.append(iter.next());
            }
            builder.append("\"]},");
        }
        builder.append("]}");
        builder.trimToSize();
        return builder.toString();
    }

    @Nullable
    public static BunnyHistory getInstance(Project project) {
        return ServiceManager.getService(project, BunnyHistory.class);
    }
}
