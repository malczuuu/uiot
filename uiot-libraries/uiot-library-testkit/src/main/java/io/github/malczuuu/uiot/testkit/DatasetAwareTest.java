package io.github.malczuuu.uiot.testkit;

import io.github.malczuuu.uiot.testkit.dataset.DatasetRefreshExtension;
import io.github.malczuuu.uiot.testkit.dataset.DatasetTestConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(DatasetRefreshExtension.class)
@Import(DatasetTestConfiguration.class)
public @interface DatasetAwareTest {}
