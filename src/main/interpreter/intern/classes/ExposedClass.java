package main.interpreter.intern.classes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExposedClass
{
    String internModule();
    String internName();
}
