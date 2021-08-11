package io.github.malczuuu.uiot.models.telemetry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@JsonSerialize(using = Pack.PackSerializer.class)
@JsonDeserialize(using = Pack.PackDeserializer.class)
public class Pack {

  private final List<Record> records;

  public Pack(List<Record> records) {
    this.records = new ArrayList<>(records);
  }

  public Pack(Record... records) {
    this(Arrays.asList(records));
  }

  public Stream<Record> stream() {
    return records.stream();
  }

  public boolean isEmpty() {
    return records.isEmpty();
  }

  public static class PackSerializer extends StdSerializer<Pack> {

    public PackSerializer() {
      super(Pack.class);
    }

    @Override
    public void serialize(Pack value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      gen.writeObject(value.getRecords());
    }
  }

  public static class PackDeserializer extends StdDeserializer<Pack> {

    public PackDeserializer() {
      super(Pack.class);
    }

    @Override
    public Pack deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      Record[] records = p.readValueAs(Record[].class);
      return new Pack(records);
    }
  }

  public List<Record> getRecords() {
    return Collections.unmodifiableList(records);
  }
}
