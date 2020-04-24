package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.Objects.SymolTableOBJ.titration;

import java.util.ArrayList;
import java.util.List;

public class TitrationChecker extends Checker {

    /***
     * Chekcs if the input list of titrations is null, if it is return a new instance
     * @param list
     * @param method
     * @return
     */
    public List<titration> CheckTitrations(List<titration> list, String method){
        if(list != null)
        {
            TH.terminate_program("Titrationlist is not null in " + method + " and no one loves you");
        }
        return new ArrayList<>();
    }
}
