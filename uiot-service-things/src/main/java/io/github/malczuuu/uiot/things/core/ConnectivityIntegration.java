package io.github.malczuuu.uiot.things.core;

public interface ConnectivityIntegration {

  boolean onThingDeletion(String roomUid, String thingUid);
}
