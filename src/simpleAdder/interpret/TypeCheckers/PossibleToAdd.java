package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.GetMethods.ViableVariable;

public class PossibleToAdd {
    ViableVariable vv = new ViableVariable();

    public Boolean AddInt;
    public Boolean AddFloat;
    public Boolean AddSpecie;
    public Boolean AddRate;

    public PossibleToAdd(Boolean Int, Boolean Float, Boolean Specie, Boolean Rate){
        this.AddInt = Int;
        this.AddFloat = Float;
        this.AddSpecie = Specie;
        this.AddRate = Rate;
    }

    /***
     * Verify whether or not it is possible to add a given type
     * @param type
     * @return
     */
    public boolean CanBeAdded(String type){
        if (type.equals(ViableVariable.INT) && AddInt == true){
            return true;
        }else if(type.equals(ViableVariable.FLOAT) && AddFloat == true){
            return true;
        }else if(type.equals(ViableVariable.SPECIE) && AddSpecie == true){
            return true;
        }else return type.equals(ViableVariable.RATE) && AddRate == true;
    }
}
