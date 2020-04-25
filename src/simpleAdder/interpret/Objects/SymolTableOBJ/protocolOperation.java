package simpleAdder.interpret.Objects.SymolTableOBJ;

import simpleAdder.interpret.CompilerPhases.TerminateProgram;
import simpleAdder.interpret.Objects.ProtocolOBJ.Dispose;
import simpleAdder.interpret.Objects.ProtocolOBJ.Equilibrate;
import simpleAdder.interpret.Objects.ProtocolOBJ.Mix;
import simpleAdder.interpret.Objects.ProtocolOBJ.Split;
import simpleAdder.interpret.GetMethods.ViableVariable;

public class protocolOperation {
    public TerminateProgram TH = new TerminateProgram();
    public ViableVariable vv = new ViableVariable();
    public Equilibrate equili = null;
    public Mix mix = null;
    public Split split = null;
    public Dispose dispose = null;

    public protocolOperation(String type)
    {

        if(type.equals(vv.MIX))
        {
            this.mix = new Mix();
        }
        else if(type.equals(vv.SPLIT))
        {
            this.split = new Split();
        }
        else
        {
             TH.terminate_program("Invalid type notation (protocolOperation constructor)");
        }
    }

    /**
     * Uses the split object and populates its fields, with sample key.
     * @param str of type string, the key for a specefic sample
     * @param method of type string, used for error messageing
     */
    public void AddStringToField(String str, String method){
        if (split != null && split.SplitSample == null){
            split.SplitSample = str;
        }else if (split != null && split.ResultingSamples != null){
            split.ResultingSamples.add(0, str);
        }else {
            TH.terminate_program("No instance was hit (" + method + ")");
        }
    }

    /**
     * Add the values to the split instance and checks if problem orccure
     * @param value of type string, used to add the value to split
     * @param type of type stringused to add the value to split
     */
    // TODO: 04/04/2020 Remlig misleede navn.
    public void ValueToSplit(String value, String type){
        if (split == null){
            TH.terminate_program("Split is null (valueToSplit)");
        }
        if(!split.AddValue(value, type)){
            TH.terminate_program("The value " + value + " of type " + type + " cannot be added to a Split. Try a positive Int or Float value.");
        }
    }

    public protocolOperation(String type, String sample ,String amount)
    {
        switch (type)
        {
            case ViableVariable.MIX: break;
            case ViableVariable.EQUILIBRATE: equiliToStack(sample,amount); break;
            case ViableVariable.DISPOSE: DisposeInstance(sample,amount); break;
            case ViableVariable.SPLIT: break;
            default:
                TH.terminate_program(type +" is an invalid type (initializeOperation)");

        }
    }

    public protocolOperation(String type, String sample ,String amount, String stepSize)
    {
        switch (type)
        {
            case ViableVariable.EQUILIBRATE: equiliToStack(sample,amount,stepSize); break;
            default:
                TH.terminate_program(type +" is an invalid type (initializeOperation)");

        }
    }

    /**
     * Adds a sample type to the mixer
     * @param sample is a string, which is a key to the position of the sample in the symbol table
     * @param methodname is a string, used for error messaging
     */
    public void addToMix(String sample, String methodname)
    {
        if(mix == null)
        {
            TH.terminate_program("mix was null in ( "+methodname +")");
        }
        else
        {
            mix.Mixers.add(sample);
        }
    }

    /**
     * Uses the split object and populates its fields, with sample key.
     * @param type of type string, used to check with operation should be used
     * @param sample of type string, the key for a specefic sample
     * @param methodName of type string used for error messageing
     */
    public void addToSplit(String type ,String sample, String methodName)
    {
        if(split == null)
        {
            TH.terminate_program("split was null in ("+methodName+")");
        }
        else if(type.equals(vv.SAMPLE))
        {
                split.ResultingSamples.add(sample);
        }else if(type.equals(vv.INT) || type.equals(vv.FLOAT))
        {
            split.DestributionValue.add(sample);
        }
    }

    /**
     *
     * @param sample of type string, a key to the samples postion in the symbol table
     * @param amount of type sting represnts some float value.
     */
    public void equiliToStack(String sample, String amount)
    {
        if(equili != null)
        {
            TH.terminate_program("unexspected instance of equili (equiliToStack)");
        }
        else
        {
            equili = new Equilibrate(sample,amount);
        }
    }

    public void equiliToStack(String sample, String amount, String stepSize)
    {
        if(equili != null)
        {
            TH.terminate_program("unexspected instance of equili (equiliToStack)");
        }
        else
        {
            equili = new Equilibrate(sample,amount,stepSize);
        }
    }
    /**
     * The dispose functionality
     * @param sample of type string, a key to the samples postion in the symbol table
     * @param procent type sting represnts some float value.
     */
    public void DisposeInstance(String sample, String procent)
    {
        this.dispose = new Dispose(sample, procent);
    }
}
