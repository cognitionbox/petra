package petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

public class ActOnC extends PEdge<Foo> {
    {
        type(Foo.class);
        pre(foo -> foo.choices == Choices.C);
        func(foo -> {
            foo.result = 3;
        });
        post(foo -> foo.result == 3);
    }
}
