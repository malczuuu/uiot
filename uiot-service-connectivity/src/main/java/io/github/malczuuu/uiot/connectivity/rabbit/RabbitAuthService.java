package io.github.malczuuu.uiot.connectivity.rabbit;

import io.github.malczuuu.uiot.connectivity.core.ConnectivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitAuthService {

  private static final Logger log = LoggerFactory.getLogger(RabbitAuthService.class);

  private final ConnectivityRepository connectivityRepository;

  private final String routingKeyTemplate;
  private final String usernameContextSeparator;
  private final String vhost;

  public RabbitAuthService(
      ConnectivityRepository connectivityRepository,
      @Value("${uiot.rmq-auth.routing-key-template}") String routingKeyTemplate,
      @Value("${uiot.rmq-auth.username-context-separator}") String usernameContextSeparator,
      @Value("${uiot.rmq-auth.vhost}") String vhost) {
    this.connectivityRepository = connectivityRepository;
    this.routingKeyTemplate = routingKeyTemplate;
    this.usernameContextSeparator = usernameContextSeparator;
    this.vhost = vhost;
  }

  public boolean challengeCredentials(CredentialsChallenge credentials) {
    log.info(
        "Attempting to challenge RabbitMQ authorization for credentials, username={}",
        credentials.getUsername());
    String[] usernameSplit = credentials.getUsername().split(usernameContextSeparator, 2);
    if (usernameSplit.length != 2) {
      return false;
    }
    return connectivityRepository
        .findByRoomAndThing(usernameSplit[0], usernameSplit[1])
        .filter(c -> c.getPassword().equals(credentials.getPassword()))
        .isPresent();
  }

  public boolean challengeVhost(VhostChallenge vhost) {
    log.info(
        "Attempting to challenge RabbitMQ authorization for vhost, username={}, vhost={}, ip={}",
        vhost.getUsername(),
        vhost.getVhost(),
        vhost.getIp());
    return this.vhost.equals(vhost.getVhost());
  }

  public boolean challengeResource(ResourceChallenge resource) {
    switch (resource.getResource()) {
      case "exchange":
        return challengeExchange(resource);
      case "queue":
        return challengeQueue(resource);
      case "topic":
        return challengeTopic(resource);
    }
    log.error(
        "Attempting to challenge RabbitMQ authorization for unknown resource, username={}, vhost={}, resource={}, name={}, permission={}",
        resource.getUsername(),
        resource.getVhost(),
        resource.getResource(),
        resource.getName(),
        resource.getPermission());
    return false;
  }

  private boolean challengeExchange(ResourceChallenge resource) {
    log.info(
        "Attempting to challenge RabbitMQ authorization for exchange resource, username={}, vhost={}, resource={}, name={}, permission={}",
        resource.getUsername(),
        resource.getVhost(),
        resource.getResource(),
        resource.getName(),
        resource.getPermission());
    return "amq.topic".equals(resource.getName()) && "write".equals(resource.getPermission());
  }

  private boolean challengeQueue(ResourceChallenge resource) {
    log.info(
        "Attempting to challenge RabbitMQ authorization for queue resource, username={}, vhost={}, resource={}, name={}, permission={}",
        resource.getUsername(),
        resource.getVhost(),
        resource.getResource(),
        resource.getName(),
        resource.getPermission());
    return vhost.equals(resource.getVhost())
        && resource.getName().startsWith("mqtt-subscription-")
        && "configure".equals(resource.getPermission());
  }

  private boolean challengeTopic(ResourceChallenge resource) {
    log.info(
        "Attempting to challenge RabbitMQ authorization for topic resource, username={}, vhost={}, resource={}, name={}, permission={}",
        resource.getUsername(),
        resource.getVhost(),
        resource.getName(),
        resource.getPermission(),
        resource.getResource());
    return false;
  }

  public boolean challengeTopic(TopicChallenge topic) {
    log.info(
        "Attempting to challenge RabbitMQ authorization for exchange resource, username={}, vhost={}, resource={}, name={}, permission={}, routingKey={}",
        topic.getUsername(),
        topic.getVhost(),
        topic.getName(),
        topic.getPermission(),
        topic.getResource(),
        topic.getRoutingKey());
    String[] usernameSplit = topic.getUsername().split(usernameContextSeparator, 2);
    if (usernameSplit.length != 2) {
      return false;
    }
    String allowedRoutingKey =
        String.format(routingKeyTemplate, usernameSplit[0], usernameSplit[1]);
    return vhost.equals(topic.getVhost()) && topic.getRoutingKey().equals(allowedRoutingKey);
  }
}
