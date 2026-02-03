package io.github.malczuuu.uiot.testkit;

import io.github.malczuuu.uiot.testkit.container.KafkaContainerConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Tag(CommonTestTag.TESTCONTAINERS)
@Import(KafkaContainerConfiguration.class)
public @interface KafkaAwareTest {}
