package io.github.malczuuu.uiot.connectivity.rest;

import io.github.malczuuu.uiot.connectivity.rabbit.CredentialsChallenge;
import io.github.malczuuu.uiot.connectivity.rabbit.RabbitAuthService;
import io.github.malczuuu.uiot.connectivity.rabbit.ResourceChallenge;
import io.github.malczuuu.uiot.connectivity.rabbit.TopicChallenge;
import io.github.malczuuu.uiot.connectivity.rabbit.VhostChallenge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class RabbitAuthController {

  private static final Logger log = LoggerFactory.getLogger(RabbitAuthController.class);

  private final RabbitAuthService rabbitAuthService;

  public RabbitAuthController(RabbitAuthService RabbitAuthService) {
    this.rabbitAuthService = RabbitAuthService;
  }

  @PostMapping(
      path = "/user",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public String user(
      @RequestParam(name = "username", required = false) String username,
      @RequestParam(name = "password", required = false) String password) {
    if (username == null || password == null) {
      return "deny";
    }
    log.debug("Requested credentials challenge for username={}", username);
    boolean result =
        rabbitAuthService.challengeCredentials(new CredentialsChallenge(username, password));
    String responseBody = result ? "allow" : "deny";
    log.info(
        "Credentials challenge for username={} resulted with status={}", username, responseBody);
    return responseBody;
  }

  @PostMapping(
      path = "/vhost",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public String vhost(
      @RequestParam(name = "username", required = false) String username,
      @RequestParam(name = "vhost", required = false) String vhost,
      @RequestParam(name = "ip", required = false) String ip) {
    if (username == null || vhost == null || ip == null) {
      return "deny";
    }
    log.debug("Requested vhost challenge for username={}, vhost={} and ip={}", username, vhost, ip);
    boolean result = rabbitAuthService.challengeVhost(new VhostChallenge(username, vhost, ip));
    String responseBody = result ? "allow" : "deny";
    log.info(
        "Vhost challenge for username={}, vhost={} and ip={} resulted with status={}",
        username,
        vhost,
        ip,
        responseBody);
    return responseBody;
  }

  @PostMapping(
      path = "/resource",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public String resource(
      @RequestParam("username") String username,
      @RequestParam("vhost") String vhost,
      @RequestParam("resource") String resource,
      @RequestParam("name") String name,
      @RequestParam("permission") String permission) {
    if (username == null
        || vhost == null
        || resource == null
        || name == null
        || permission == null) {
      return "deny";
    }
    log.debug(
        "Requested resource challenge for username={}, vhost={}, resource={}, name={} and permission={}",
        username,
        vhost,
        resource,
        name,
        permission);
    try {
      boolean result =
          rabbitAuthService.challengeResource(
              new ResourceChallenge(username, vhost, resource, name, permission));
      String responseBody = result ? "allow" : "deny";
      log.debug(
          "Resource challenge for username={}, vhost={}, resource={}, name={} and permission={} resulted with status={}",
          username,
          vhost,
          resource,
          name,
          permission,
          responseBody);
      return responseBody;
    } catch (IllegalArgumentException e) {
      log.error(
          "Requested unknown resource challenge for username={}, vhost={}, resource={}, name={}, permission={}",
          username,
          vhost,
          resource,
          name,
          permission);
      return "deny";
    }
  }

  @PostMapping(
      path = "/topic",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public String resource(
      @RequestParam(name = "username", required = false) String username,
      @RequestParam(name = "vhost", required = false) String vhost,
      @RequestParam(name = "resource", required = false) String resource,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "permission", required = false) String permission,
      @RequestParam(name = "routing_key", required = false) String routingKey) {
    if (username == null
        || vhost == null
        || resource == null
        || name == null
        || permission == null
        || routingKey == null) {
      return "deny";
    }
    log.debug(
        "Requested resource challenge for username={}, vhost={}, resource={}, name={}, permission={} and routing_key={}",
        username,
        vhost,
        resource,
        name,
        permission,
        routingKey);
    boolean result =
        rabbitAuthService.challengeTopic(
            new TopicChallenge(username, vhost, resource, name, permission, routingKey));
    String responseBody = result ? "allow" : "deny";
    log.info(
        "Resource challenge for username={}, vhost={}, resource={}, name={}, permission={} and routing_key={} resulted with status={}",
        username,
        vhost,
        resource,
        name,
        permission,
        routingKey,
        responseBody);
    return responseBody;
  }
}
