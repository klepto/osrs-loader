package dev.klepto.osrs.transform;

import dev.klepto.osrs.OsrsDefinitions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Setter {

    OsrsDefinitions value();

}
