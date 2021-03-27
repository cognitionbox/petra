package io.cognitionbox.petra.examples.simple.parallelcalcs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Calculations implements Serializable {
    List<Integer> list = new ArrayList();
    {
        for (int i=0;i<1000;i++){
            list.add(i);
        }

        for (int i : list){
            System.out.println(i);
        }
    }
    AddPositiveNumbers addPositiveNumbers1 = new AddPositiveNumbers();
    AddPositiveNumbers addPositiveNumbers2 = new AddPositiveNumbers();

    public boolean checkOk() {
        return this.addPositiveNumbers1 !=null &&
                this.addPositiveNumbers2 !=null &&
                this.addPositiveNumbers1.a>0 && this.addPositiveNumbers1.b>0 &&
                this.addPositiveNumbers2.a>0 && this.addPositiveNumbers2.b>0;
    }
}
