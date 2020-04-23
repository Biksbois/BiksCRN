package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.Objects.STObjects.PrimitiveType;

public class ValueChecker extends Checker{

    /***
     * Chekcis if the input Primitive type is null, if it is return a new instance of a primitive type with each boolenan as input
     * @param ss
     * @param type
     * @param ID
     * @param method
     * @param AddInt
     * @param AddFloat
     * @param AddSpecie
     * @param AddRate
     * @param CanBeNegative
     * @param CanHaveDecimals
     * @return
     */
    public PrimitiveType NewPrimitiveType(PrimitiveType ss, String type, String ID, String method, Boolean AddInt, Boolean AddFloat, Boolean AddSpecie, Boolean AddRate, Boolean CanBeNegative, Boolean CanHaveDecimals){
        if (ss != null){
            TH.terminate_program("PrimitiveType is not null ("  + method + ")");
        }
        return new PrimitiveType(ID, type, AddInt, AddFloat, AddSpecie, AddRate, CanBeNegative, CanHaveDecimals);
    }

    //TODO chekc int og float values er rigtige, decimal point og min max value
}
