package com.kpmg.core.models;

import java.util.Calendar;

public interface EventCard {

  String getEventTitle();

  String getEventDescription();

  Calendar getEventStartTimeAndDate();

  Calendar getEventEndTimeAndDate();

  String getEventTimeZone();

  String getPageURL();

  String getImageURL();
}
