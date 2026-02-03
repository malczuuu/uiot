package io.github.malczuuu.uiot.thing.core;

public interface ConnectivityIntegration {

  boolean onThingDeletion(String roomUid, String thingUid);
}
