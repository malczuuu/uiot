package io.github.malczuuu.uiot.history.stream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "uiot")
public class TopicProperties {

  private final String thingEventsTopic;
  private final String thingMetadataTopic;
  private final String keyedThingEventsTopic;
  private final String systemEventsTopic;

  public TopicProperties(
      @DefaultValue("uiot-thing-events") String thingEventsTopic,
      @DefaultValue("uiot-history_thing-metadata") String thingMetadataTopic,
      @DefaultValue("uiot-history_keyed-thing-events") String keyedThingEventsTopic,
      @DefaultValue("uiot-system-events") String systemEventsTopic) {
    this.thingEventsTopic = thingEventsTopic;
    this.thingMetadataTopic = thingMetadataTopic;
    this.keyedThingEventsTopic = keyedThingEventsTopic;
    this.systemEventsTopic = systemEventsTopic;
  }

  public String getThingEventsTopic() {
    return thingEventsTopic;
  }

  public String getThingMetadataTopic() {
    return thingMetadataTopic;
  }

  public String getKeyedThingEventsTopic() {
    return keyedThingEventsTopic;
  }

  public String getSystemEventsTopic() {
    return systemEventsTopic;
  }
}
