package io.github.malczuuu.uiot.connectivity.rabbit;

public final class ResourceChallenge {

  private final String username;
  private final String vhost;
  private final String resource;
  private final String name;
  private final String permission;

  public ResourceChallenge(
      String username, String vhost, String resource, String name, String permission) {
    this.username = username;
    this.vhost = vhost;
    this.resource = resource;
    this.name = name;
    this.permission = permission;
  }

  public String getUsername() {
    return username;
  }

  public String getVhost() {
    return vhost;
  }

  public String getResource() {
    return resource;
  }

  public String getName() {
    return name;
  }

  public String getPermission() {
    return permission;
  }
}
