package org.logger;

import java.util.Calendar;

/**
 * Created by Robert Szczygielski on 15.10.16.
 */
public class Logger {
    private String log;
    private messState state;

    public enum messState {
        ER("ERROR"),
        WR("WARN"),
        IN("INFO"),
        ;

        private final String stateText;

        messState(String stateText) {
            this.stateText = stateText;
        }

        public String getStateText() {
            return stateText;
        }
    }

    public String getMessage() {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(DataUtils.getCurrentTime());
        logMessage.append(" ");
        logMessage.append(state.getStateText());
        logMessage.append(" ");
        logMessage.append(log);
        return logMessage.toString();
    }


    public void setLog(messState state, String log) {
        this.state = state;
        this.log = log;
    }
}
