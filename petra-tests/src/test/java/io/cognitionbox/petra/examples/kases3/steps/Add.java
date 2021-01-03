package io.cognitionbox.petra.examples.kases3.steps;//package io.cognitionbox.petra.examples.kases.steps;
//
//import io.cognitionbox.petra.examples.kases.math.Bi;
//import io.cognitionbox.petra.examples.kases.math.R;
//import io.cognitionbox.petra.lang.PEdge;
//
//public class Add extends PEdge<Bi> {
//    {
//        type(Bi.class);
//        kase(i -> isPos(i.a()) && isNeg(i.b()), i -> i.c().isPos());
//        kase(i -> isPos(i.a()) && i.b().isPos(), i -> i.c().isPos());
//        kase(i -> isPos(i.a()) && isNeg(i.b()), i -> i.c().isNeg());
//        kase(i -> i.a().isNeg() && i.b().isPos(), i -> i.c().isNeg());
//        kase(i -> i.a().isZero(), i -> i.c().isZero());
//        kase(i -> i.b().isZero(), i -> i.c().isZero());
//        func(i ->{
//            i.c((R)i.a().multiply(i.b()));
//        });
//    }
//}