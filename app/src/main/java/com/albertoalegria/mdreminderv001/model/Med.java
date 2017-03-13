package com.albertoalegria.mdreminderv001.model;

import java.util.ArrayList;

/**
 * Created by alberto on 27/02/17.
 */

public class Med {
    private String name;
    private int type;
    private double quantity;
    private String firstImagePath;
    private ArrayList<String> hours;

    private Med(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.quantity = builder.quantity;
        this.firstImagePath = builder.firstImagePath;
        this.hours = builder.hours;
    }

    public static class Builder {
        private String name;
        private int type;
        private double quantity;
        private String firstImagePath;
        private ArrayList<String> hours;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setQuantity(double quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
            return this;
        }

        public Builder setHours(ArrayList<String> hours) {
            this.hours = hours;
            return this;
        }
        public Med Build() {
            return new Med(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public ArrayList<String> getHours() {
        return hours;
    }

    public void setHours(ArrayList<String> hours) {
        this.hours = hours;
    }

    public String getHoursAsString() {
        return hours.toString().replace("[", "").replace("]", "");
    }

    public int[] getHourAndRange() {
        int[] fullhour = getFullHourFromString(hours.get(0));
        int range = 24/hours.size();

        //               hour         minute       range (duh)
        return new int[]{fullhour[0], fullhour[1], range};
    }

    private int[] getFullHourFromString(String completeHour) {
        String[] simpleHour = completeHour.split(" ");
        String[] finalHour = simpleHour[0].split(":");
        int hour = Integer.valueOf(finalHour[0]);
        int minute = Integer.valueOf(finalHour[1]);
        return new int[]{hour, minute};
    }

    @Override
    public String toString() {
        return "Med{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", quantity=" + quantity +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", hours=" + hours.toString() +
                '}';
    }
}
