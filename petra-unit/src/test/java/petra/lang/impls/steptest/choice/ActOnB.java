package petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

public class ActOnB extends PEdge<Foo> {
    {
        type(Foo.class);
        pre(foo -> foo.choices == Choices.B);
        func(foo -> {
            foo.result = 2;
        });
        post(foo -> foo.result == 2);
    }
}
