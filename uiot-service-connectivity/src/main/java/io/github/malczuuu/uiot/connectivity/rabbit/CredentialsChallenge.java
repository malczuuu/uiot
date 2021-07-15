package io.github.malczuuu.uiot.connectivity.rabbit;

public final class CredentialsChallenge {

  private final String username;
  private final String password;

  public CredentialsChallenge(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
