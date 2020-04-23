package simpleAdder.interpret.Objects.ProtocolObjects;

import javafx.util.Pair;
import simpleAdder.interpret.TypeCheckers.PossibleToAdd;

import javax.sound.midi.MidiFileFormat;
import java.util.ArrayList;
import java.util.List;

public class Split {
    public List<String> ResultingSamples = new ArrayList<>();
    public String SplitSample = null;
    public List<String> DestributionValue = new ArrayList<>();

    private Boolean AddInt = true;
    private Boolean AddFloat = true;
    private Boolean AddSpecie = false;
    private Boolean AddRate = false;

    private PossibleToAdd ps = new PossibleToAdd(AddInt, AddFloat, AddSpecie, AddRate);

    /**
     * Checks if the sample is valid.
     * @return Pair of a boolean and a string, which denotes what type of error occurred, and if the program should terminate.
     */
    public Pair<Boolean, String> IsDistributionvalueValid(){
        double result = 0.0;

        if (ResultingSamples.size() != DestributionValue.size()){
            return new Pair<>(false, "When splitting sample " + SplitSample + " there should be an equal amount of Sampels to split to " +
                    "(" + ResultingSamples.size() + ") and slices (" + DestributionValue.size() + ")");
        }

        for (String value:DestributionValue){
            try {
                result += Float.parseFloat(value);
            }catch (Exception e){
                System.out.println("Value in distributionValue could not be parsed to a float (Split)");
            }
        }
        return result <= 1.0 ? new Pair<>(true, "") : new Pair<>(false, "The distribution value in split should not equal a value higher than 1. The value is: " + result);
    }

    /**
     *
     * @param value of type string
     * @param type of type string
     * @return boolean true if successful added and false if not.
     */
    public Boolean AddValue(String value, String type){
        if (ps.CanBeAdded(type) && !value.contains("-")){
            DestributionValue.add(value);
            return true;
        }
        return false;
    }
}
