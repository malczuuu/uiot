package io.github.malczuuu.uiot.history.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.text.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

@Service
public class StorageNameServiceImpl implements StorageNameService {

  private final Hex hex = new Hex(Charsets.UTF_8);

  @Override
  public String getStorageName(String room) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(room.getBytes());
      String hashAsHex = new String(hex.encode(hash));
      String tableName = "tlm_" + hashAsHex;
      return tableName.substring(0, 64);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
