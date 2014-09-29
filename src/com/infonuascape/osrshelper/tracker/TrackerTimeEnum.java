package com.infonuascape.osrshelper.tracker;

/**
 * Created by maden on 9/24/14.
 */
public class TrackerTimeEnum {
    public enum TrackerTime {
        Hour(3600),
        Day(86400),
        Week(604800),
        Month(2592000),
        Year(31557600);

        public int getSeconds() {
            return seconds;
        }

        private final int seconds;
        private TrackerTime(int seconds) {
            this.seconds = seconds;
        }
    }
}
