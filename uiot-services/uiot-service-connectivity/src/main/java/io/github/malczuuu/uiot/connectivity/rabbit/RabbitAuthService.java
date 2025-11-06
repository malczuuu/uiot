package io.github.malczuuu.uiot.connectivity.rabbit;

public interface RabbitAuthService {

  boolean challengeCredentials(CredentialsChallenge credentials);

  boolean challengeVhost(VhostChallenge vhost);

  boolean challengeResource(ResourceChallenge resource);

  boolean challengeTopic(TopicChallenge topic);
}
