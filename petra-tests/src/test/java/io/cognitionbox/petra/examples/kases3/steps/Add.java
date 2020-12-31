package io.cognitionbox.petra.examples.kases3.steps;//package io.cognitionbox.petra.examples.kases.steps;
//
//import io.cognitionbox.petra.examples.kases.math.Bi;
//import io.cognitionbox.petra.examples.kases.math.R;
//import io.cognitionbox.petra.lang.PEdge;
//
//public class Add extends PEdge<Bi> {
//    {
//        type(Bi.class);
//        kase(x -> isPos(x.a()) && isNeg(x.b()), x -> x.c().isPos());
//        kase(x -> isPos(x.a()) && x.b().isPos(), x -> x.c().isPos());
//        kase(x -> isPos(x.a()) && isNeg(x.b()), x -> x.c().isNeg());
//        kase(x -> x.a().isNeg() && x.b().isPos(), x -> x.c().isNeg());
//        kase(x -> x.a().isZero(), x -> x.c().isZero());
//        kase(x -> x.b().isZero(), x -> x.c().isZero());
//        func(x ->{
//            x.c((R)x.a().multiply(x.b()));
//        });
//    }
//}