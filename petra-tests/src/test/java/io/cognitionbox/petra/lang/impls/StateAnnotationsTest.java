package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.annotations.propositions.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class StateAnnotationsTest extends BaseExecutionModesTest {

    public StateAnnotationsTest(ExecMode execMode) {
        super(execMode);
    }

    @State interface B {
        default boolean b(){
            return true;
        }
    }
    @State interface C {
        default boolean c(){
            return true;
        }
    }

    @State
    public class A implements @State B, @State C {}
    @State
    public class X implements @State B, C {}

    @Test
    public void test() {
        int count = 0;
        for (AnnotatedType type : A.class.getAnnotatedInterfaces()) {
            System.out.println(type.getType());
            for (Annotation annotation : type.getAnnotations()) {
                if (annotation.annotationType().equals(State.class)){
                    System.out.println("\t" + annotation);
                    count++;
                }
            }
        }
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void test2() {
        int count = 0;
        for (AnnotatedType type : X.class.getAnnotatedInterfaces()) {
            System.out.println(type.getType());
            for (Annotation annotation : type.getAnnotations()) {
                if (annotation.annotationType().equals(State.class)){
                    System.out.println("\t" + annotation);
                    count++;
                }
            }
        }
        assertThat(count).isEqualTo(1);
    }
}
