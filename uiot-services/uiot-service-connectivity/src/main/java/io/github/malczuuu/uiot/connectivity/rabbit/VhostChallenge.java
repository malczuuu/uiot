package io.github.malczuuu.uiot.connectivity.rabbit;

public final class VhostChallenge {

  private final String username;
  private final String vhost;
  private final String ip;

  public VhostChallenge(String username, String vhost, String ip) {
    this.username = username;
    this.vhost = vhost;
    this.ip = ip;
  }

  public String getUsername() {
    return username;
  }

  public String getVhost() {
    return vhost;
  }

  public String getIp() {
    return ip;
  }
}
