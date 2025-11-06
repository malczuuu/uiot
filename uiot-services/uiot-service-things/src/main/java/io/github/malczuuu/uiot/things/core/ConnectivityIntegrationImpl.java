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
  private final String connectivityIntegrationUrl;

  public ConnectivityIntegrationImpl(
      RestOperations restOperations,
      @Value("${uiot.connectivity-integration-url}") String connectivityIntegrationUrl) {
    this.restOperations = restOperations;
    this.connectivityIntegrationUrl = connectivityIntegrationUrl;
  }

  @Override
  public boolean onThingDeletion(String roomUid, String thingUid) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("roomUid", roomUid);
    parameters.put("thingUid", thingUid);
    try {
      restOperations.delete(connectivityIntegrationUrl, parameters);
      return true;
    } catch (RestClientException e) {
      return false;
    }
  }
}
