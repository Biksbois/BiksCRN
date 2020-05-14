package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.Objects.SymolTableOBJ.function;
import simpleAdder.interpret.Objects.SymolTableOBJ.parameter;

import java.util.List;
import java.util.Stack;

public class FuncChecker extends Checker {

    /***
     * When a function is called with some parameter, this method checks whether the input parameters are
     * valid the the ones the it was instanciated with.
     * @param func
     * @param list
     */
    public void VerifyParameters(function func, List<parameter> list){
        if (func == null || list == null || func.parameters == null){
            TH.terminate_program("The function or list of parameters is null, handle this (VerifyParameters)");
        }

        if (func.parameters.size() != list.size()){
            TH.terminate_program(func.id + " takes " + func.parameters.size() + " parameters");
        }

        for(int i = 0; i < func.parameters.size(); i++){
            if (!ParametersMatchType(list.get(i).type, func.parameters.get(i).type, list.get(i).name)){
                TH.terminate_program(func.id + " takes an " + func.parameters.get(i).type + " as parameter number " + ++i + " (VerifyParameters)");
            }
        }
    }

    /***
     * Chekc if one parameter is equal to another. One exception is if it is a rate and float, they can be used as the same as long
     * as the float is positive.
     * @param called
     * @param actual
     * @param calledValue
     * @return
     */
    private Boolean ParametersMatchType(String called, String actual, String calledValue){
        if (actual.equals(vv.RATE) && called.equals(vv.FLOAT) && !calledValue.contains("-") && !calledValue.contains(".")){
            return true;
        }else {
            return actual.equals(called);
        }
    }

    /***
     * Returns a new instance of function if the input function was null.
     * @param func
     * @param id
     * @param method
     * @return
     */
    public function CheckFunction(function func,String id, String method){
        if (func != null){
            TH.terminate_program("Function was not null ( "+method +" )");
        }
        return new function(id);
    }

    /***
     * When a function is called, this is used to add the values of the parameters it is called with to the
     * body of the function. A pair is return of the stack and a boolean to whether or not it is possible to
     * calcualte it, decided by if it contains reserved words.
     * @param parameters
     * @param func
     * @return
     */
    public BiksPair<Boolean, Stack<String>> ParametersToStack(List<parameter> parameters, function func){
        if (func == null || func.body == null || parameters == null){
            TH.terminate_program("The stack or parameter list is null, handle this (ParameterToExpressoin)");
        }

        boolean IsCalcable = true;

        Stack<String> clone = (Stack<String>) func.body.clone();
        Object[] arrExpr = clone.toArray();

        for (int i = 0; i <= arrExpr.length-1; i++) {
            if (CheckStringLetter(arrExpr[i].toString())) {
                if (parameters.size() != 0){
                    for (int j = 0; j <= parameters.size() - 1; j++) {
                        if (func.parameters.get(j).name.equals(arrExpr[i].toString()) && !vv.isReservedWord(arrExpr[i].toString())) {
                            arrExpr[i] = parameters.get(j).name;
                        }else if (vv.isReservedWord(arrExpr[i].toString())){
                            IsCalcable = false;
                        }
                    }
                }
                else if (vv.isReservedWord(arrExpr[i].toString())){
                    IsCalcable = false;
                }
            }
        }

        clone.clear();
        for (Object arr: arrExpr) {
            clone.push(arr.toString());
        }

        return new BiksPair<>(IsCalcable, clone);
    }
}
