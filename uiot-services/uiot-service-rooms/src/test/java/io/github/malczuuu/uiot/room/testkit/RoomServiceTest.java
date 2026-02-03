package io.github.malczuuu.uiot.room.testkit;

import io.github.malczuuu.uiot.room.RoomsApplication;
import io.github.malczuuu.uiot.testkit.DatasetAwareTest;
import io.github.malczuuu.uiot.testkit.KafkaAwareTest;
import io.github.malczuuu.uiot.testkit.MongoAwareTest;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(classes = {RoomsApplication.class})
@DatasetAwareTest
@KafkaAwareTest
@MongoAwareTest
@Import(RoomTestConfiguration.class)
public @interface RoomServiceTest {}
