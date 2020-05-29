package io.cognitionbox.petra.examples.simple.compose;

public interface rwBC extends rwB, rwC , roBC{
    void b(B b);
    void c(C c);
}
