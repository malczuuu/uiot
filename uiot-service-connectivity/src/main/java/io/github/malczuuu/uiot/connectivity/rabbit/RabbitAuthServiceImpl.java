package io.github.malczuuu.uiot.connectivity.rabbit;

import io.github.malczuuu.uiot.connectivity.core.ConnectivityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitAuthServiceImpl implements RabbitAuthService {

  private final ConnectivityRepository connectivityRepository;

  private final String routingKeyTemplate;
  private final String usernameContextSeparator;
  private final String vhost;

  public RabbitAuthServiceImpl(
      ConnectivityRepository connectivityRepository,
      @Value("${uiot.routing-key-template}") String routingKeyTemplate,
      @Value("${uiot.username-context-separator}") String usernameContextSeparator,
      @Value("${uiot.vhost}") String vhost) {
    this.connectivityRepository = connectivityRepository;
    this.routingKeyTemplate = routingKeyTemplate;
    this.usernameContextSeparator = usernameContextSeparator;
    this.vhost = vhost;
  }

  @Override
  public boolean challengeCredentials(CredentialsChallenge credentials) {
    String[] usernameSplit = credentials.getUsername().split(usernameContextSeparator, 2);
    if (usernameSplit.length != 2) {
      return false;
    }
    return connectivityRepository
        .findByRoomUidAndThingUid(usernameSplit[0], usernameSplit[1])
        .filter(c -> c.getPassword().equals(credentials.getPassword()))
        .isPresent();
  }

  @Override
  public boolean challengeVhost(VhostChallenge vhost) {
    return this.vhost.equals(vhost.getVhost());
  }

  @Override
  public boolean challengeResource(ResourceChallenge resource) {
    switch (resource.getResource()) {
      case "exchange":
        return challengeExchange(resource);
      case "queue":
        return challengeQueue(resource);
      case "topic":
        return challengeTopic(resource);
    }
    throw new IllegalArgumentException("unknown resource");
  }

  private boolean challengeExchange(ResourceChallenge resource) {
    return "amq.topic".equals(resource.getName()) && "write".equals(resource.getPermission());
  }

  private boolean challengeQueue(ResourceChallenge resource) {
    return vhost.equals(resource.getVhost())
        && resource.getName().startsWith("mqtt-subscription-")
        && "configure".equals(resource.getPermission());
  }

  private boolean challengeTopic(ResourceChallenge resource) {
    return false;
  }

  @Override
  public boolean challengeTopic(TopicChallenge topic) {
    String[] usernameSplit = topic.getUsername().split(usernameContextSeparator, 2);
    if (usernameSplit.length != 2) {
      return false;
    }
    String allowedRoutingKey =
        String.format(routingKeyTemplate, usernameSplit[0], usernameSplit[1]);
    return vhost.equals(topic.getVhost()) && topic.getRoutingKey().equals(allowedRoutingKey);
  }
}
