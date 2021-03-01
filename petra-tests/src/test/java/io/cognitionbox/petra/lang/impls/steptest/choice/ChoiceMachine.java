package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.lang.impls.steptest.choice.Choices.*;
import static io.cognitionbox.petra.util.Petra.choice;

public class ChoiceMachine extends PGraph<Foo> {
    {
        type(Foo.class);
        pre(foo -> foo.choices==null);
        begin();
        init(foo -> foo, MakeChoice.class);
        choice(ActOnA.class);
        choice(ActOnB.class);
        choice(ActOnC.class);
        end();
        post(foo -> (foo.choices==A && foo.result==1) ^
                (foo.choices==B && foo.result==2) ^
                (foo.choices==C && foo.result==3));
    }
}
