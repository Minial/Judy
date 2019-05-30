package pl.wroc.pwr.judy.helpers;

import pl.wroc.pwr.judy.operators.AbstractMutationOperator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to class must specify <code>targetClass</code> which should be
 * mutated during tests. Applied to inner class indicates that it should be
 * mutated during tests.
 * <p/>
 * Annotation applied to inner class overrides data from annotation applied on
 * the base class.
 *
 * @author pmiwaszko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mutate {

	Class<? extends AbstractMutationOperator> operator();

	Class<?> targetClass() default Object.class;

}
