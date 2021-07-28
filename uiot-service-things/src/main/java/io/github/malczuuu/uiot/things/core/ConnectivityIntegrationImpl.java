package io.github.malczuuu.uiot.things.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

@Service
public class ConnectivityIntegrationImpl implements ConnectivityIntegration {

  private final RestOperations restOperations;
  private final String url;

  public ConnectivityIntegrationImpl(
      RestOperations restOperations, @Value("${uiot.things.connectivity-url}") String url) {
    this.restOperations = restOperations;
    this.url = url;
  }

  @Override
  public boolean onThingDeletion(String roomUid, String thingUid) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("roomUid", roomUid);
    parameters.put("thingUid", thingUid);
    try {
      restOperations.delete(url, parameters);
      return true;
    } catch (RestClientException e) {
      return false;
    }
  }
}
