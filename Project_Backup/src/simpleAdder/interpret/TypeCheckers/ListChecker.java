package simpleAdder.interpret.TypeCheckers;
import simpleAdder.interpret.Objects.STObjects.parameter;
import simpleAdder.interpret.Objects.STObjects.reaction;

import java.util.ArrayList;
import java.util.List;

public class ListChecker extends Checker {

    /***
     * Returns a new instance of a list of reactions if the input list is null
     * @param list
     * @param method
     * @return
     */
    public List<reaction> CheckList(List<reaction> list, String method){
        if(list != null)
        {
            TH.terminate_program("Crn is not null in " + method + " and no one loves you");
        }
        return new ArrayList<>();
    }
}
