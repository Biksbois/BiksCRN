package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import com.company.node.PProtocol;
import simpleAdder.interpret.GetMethods.ViableVariable;
import simpleAdder.interpret.Objects.ProtocolOBJ.Mix;
import simpleAdder.interpret.Objects.SymolTableOBJ.SymbolTableType;
import simpleAdder.interpret.Objects.SymolTableOBJ.protocolOperation;
import simpleAdder.interpret.Objects.SymolTableOBJ.reaction;
import simpleAdder.interpret.Objects.SymolTableOBJ.titration;
import simpleAdder.interpret.TypeCheckers.BiksPair;
import simpleAdder.interpret.TypeCheckers.Check;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import java.util.*;


public class Protocol extends CodeGenerationMethods {
    HashMap<String, SymbolTableType> global;
    Dict dict = new Dict();
    Check check = new Check();
    Euler euler = new Euler();
    Titration titration = new Titration();
    int level = 0;
    int mixCount = 0;
    int equilibrateCount = 0;

    /***
     * This method generates the Protocols based on the content of the SymbolTable and the operations 
     * that are in the protocols stack. First it initiate the SymbolTable global in the class, and 
     * afterwards goes through the methods that generate the Python code, in the end it returns the
     * Protocol scope of the language.
     * @Param global
     * @Param protocols
     * @return
     */
    public String Generate(HashMap<String, SymbolTableType> global, Stack<protocolOperation> protocols){
        this.global = global;
        String PrettyResult = dict.GenerateDictionary(global, level);
        protocols = check.StackType.ReverseStack(protocols);
        while (!protocols.isEmpty()){
            PrettyResult += ApplyProtocol(protocols.pop());
        }

        if (equilibrateCount > 0){
            PrettyResult += GenerateSavedGraphs();
        }

        return PrettyResult;
    }

    private String GenerateSavedGraphs(){
        String result = "";
        if (equilibrateCount == 1){
            result += "DrawGraph(Species0, Steps0, name0, len(Steps0), taken0)\n" +
                      "\n" +
                      "plt.show()";
        }else{
            result = "count = 1\n";
            result += ApplyTab(level, "def onclick(event):\n");
            level++;
            result += ApplyTab(level, "global count\n");
            result += ApplyTab(level, "event.canvas.figure.clear()\n");
            result += ApplyTab(level, "plt.clf()\n");

            for (int i = 0; i <= equilibrateCount-1; i++){
                if (i == 0){
                    result += ApplyTab(level, "if count % " + equilibrateCount + " == 0:\n");// TODO: 01/05/2020 fix

                }else if(i == equilibrateCount-1){
                    result += ApplyTab(level, "else:\n");
                }else{
                    result += ApplyTab(level, "elif count % " + (i+1) + " == 0: \n");
                }
                level++;
                result += ApplyTab(level, "DrawGraph(Species" + i + ", Steps" + i + ", name" + i + ", len(Steps" + i + "), taken" + i + ")\n");
                level--;
            }

            result += ApplyTab(level, "count += 1\n");
            result += ApplyTab(level, "if count % " + equilibrateCount + " == 0:\n");
            level++;
            result += ApplyTab(level, "count -= " + equilibrateCount + "\n");
            level--;
            result += ApplyTab(level, "event.canvas.draw()\n");
            level--;

            result += "\n" +
                    "fig = plt.figure()\n" +
                    "fig.canvas.mpl_connect('button_press_event', onclick)\n" +
                    "\n" +
                    "DrawGraph(Species0, Steps0, name0, len(Steps0), taken0)\n" +
                    "\n" +
                    "plt.show()\n";

        }
        return result;
    }

    /***
     * This method serves as a check on what protocol operation is used, depending on the specific 
     * conditional result, the code for the protocol will be generated. if there is no match
     * the method will return a empty string.
     * @Param protocol
     * @return
     */
    public String ApplyProtocol(protocolOperation protocol){
        if (protocol.split != null){
            return ApplySplit(protocol);
        }else if(protocol.equili != null){
            return ApplyEquilibrat(protocol);
        }else if(protocol.mix != null){
            return ApplyMix(protocol);
        }else if(protocol.dispose != null){
            return ApplyDispose(protocol);
        }else {
            return "";
        }
    }

    /***
     * The function of this method, is to generate the code that will initiate the mix function in the 
     * Python code, it is reached if the conditional statement in ApplyProtocols is not null.
     * @Param protocol
     * @return
     */
    public String ApplyMix(protocolOperation protocol){
        String prettyResult = "";
        List<reaction> reacs = new ArrayList<>();
        List<titration> addmol = new ArrayList<>();
        List<titration> remmol = new ArrayList<>();
        prettyResult += GetSampleName(protocol.mix.ResultingSample) + ".sample = mix(["+prettyMixers(protocol.mix.Mixers)+"])\n";
        for (String mixer : protocol.mix.Mixers)
        {
            if(global.get(mixer).scope.containsKey(ViableVariable.CRN))
            {
                reacs.addAll(global.get(mixer).scope.get(ViableVariable.CRN).crn);
            }
            if (global.get(mixer).scope.containsKey(ViableVariable.ADDMOL))
            {
                addmol.addAll(global.get(mixer).scope.get(ViableVariable.ADDMOL).titrations);
            }
            if (global.get(mixer).scope.containsKey(ViableVariable.REMMOL))
            {
                remmol.addAll(global.get(mixer).scope.get(ViableVariable.REMMOL).titrations);
            }

        }

        AddToReaction(protocol.mix, ViableVariable.CRN, reacs, new SymbolTableType(ViableVariable.CRN, ViableVariable.CRN,reacs));
        AddToReaction(protocol.mix, ViableVariable.ADDMOL, reacs, new SymbolTableType(ViableVariable.ADDMOL,addmol, ViableVariable.ADDMOL));
        AddToReaction(protocol.mix, ViableVariable.REMMOL, reacs, new SymbolTableType(ViableVariable.REMMOL,remmol, ViableVariable.REMMOL));

        prettyResult += euler.Generate(global.get(protocol.mix.ResultingSample).scope,level,Integer.toString(mixCount));
        prettyResult += GetSampleName(protocol.mix.ResultingSample)+".Euler = Euler"+mixCount+"\n";

        prettyResult += titration.Generate(global.get(protocol.mix.ResultingSample).scope.get(ViableVariable.ADDMOL).titrations, global.get(protocol.mix.ResultingSample).scope.get(ViableVariable.REMMOL).titrations, level,Integer.toString(mixCount));
        prettyResult += GetSampleName(protocol.mix.ResultingSample)+".ApplyTitration = ApplyTitration"+mixCount++ +"\n";

        return prettyResult;
    }

    public void AddToReaction(Mix mix, String key, List<reaction> reacs, SymbolTableType obj){
        if(!global.get(mix.ResultingSample).scope.containsKey(key))
        {
            global.get(mix.ResultingSample).scope.put(key, obj);
        }
        else
        {
            global.get(mix.ResultingSample).scope.get(key).crn = reacs;
        }
    }

    /***
     * This method is used to generate the part of the Python code, that specifies what samples that will be
     * mixed. It first adds the mixers to a set, so that it is possible to iterate over the samples and get 
     * the name, followed by adding them to the string. The functions ends by returning the samples that will
     * be mixed.
     * @Param Mixers
     * @return
     */
    public String prettyMixers(List<String> Mixers)
    {
        String prettyResult ="";
        Set<String> set = new HashSet<>(Mixers);
        Mixers.clear();
        Mixers.addAll(set);
        for (String Mixee:Mixers)
        {
            prettyResult += GetSampleName(Mixee)+".sample, ";
        }
        prettyResult = prettyResult.substring(0,prettyResult.length() - 2);
        return prettyResult;
    }

    /***
     * The function of this method, is to generate the code that will initiate the equilibrate function in the 
     * Python code, it is reached if the conditional statement in ApplyProtocols is not null. It will end by 
     * returning the full string that of the equilibrate.
     * @Param protocol
     * @return
     */
    public String ApplyEquilibrat(protocolOperation protocol){
        HashMap<String, SymbolTableType> local = global.get(protocol.equili.sample).scope;
        List<String> SpeciesToUpdate = GetUpdatedSpecies(local);
        String PrettyResult = "";

        for (String specie: SpeciesToUpdate){
            PrettyResult += GetSampleName(protocol.equili.sample) + ".sample[\"" + specie + "\"] = sample[\"" + specie + "\"]\n";
        }
        PrettyResult += GetSampleName(protocol.equili.sample) + ".index = count()\n";
        PrettyResult += "equilibrate("+ GetSampleName(protocol.equili.sample) +", "+ protocol.equili.stepSize +", "+ protocol.equili.amount + ", " + protocol.equili.timeInterval + ", " + protocol.equili.bitesize + " )\n";

        if (global.containsKey(ViableVariable.SPECIE)){
            for (String key: global.get(ViableVariable.SPECIE).species.keySet()){
                if (VerifySpecie(key, local)){
                    PrettyResult += ApplyTab(level, "sample[\""+ key +"\"] = [" + GetSampleName(protocol.equili.sample) + ".sample.get(\"" + key + "\")[-1]]\n");
                }
            }
        }

        PrettyResult += ApplyTab(level, "Species" + equilibrateCount + ", Steps" + equilibrateCount + ", name" + equilibrateCount + ", taken" + equilibrateCount++ +
                " = SaveGraph(" + GetSampleName(protocol.equili.sample) + ", \"" + protocol.equili.sample + "\", " + GetSampleName(protocol.equili.sample) + ".steps )\n");

        return PrettyResult;
    }

    private List<String> GetUpdatedSpecies(HashMap<String, SymbolTableType> local) {
        List<String> result = new ArrayList<>();
        if (!local.containsKey(vv.CRN)){
            return new ArrayList<>();
        }
        for (reaction specie: local.get(vv.CRN).crn){
            result = CheckOneSide(specie.lhsPair, local, result);
            result = CheckOneSide(specie.rhsPair, local, result);
        }
        return result;
    }

    private List<String> CheckOneSide(List<BiksPair<String, String>> species, HashMap<String, SymbolTableType> local, List<String> list) {
        for (BiksPair lhs : species){
            if (local.containsKey(vv.SPECIE) && local.get(vv.SPECIE).species.containsKey(lhs.getKey())){
                continue;
            }else if(!list.contains(lhs.getKey())){
                list.add((String) lhs.getKey());
            }
        }
        return list;
    }


    /***
     * This method takes a string that contains the name of a specie, and a hashmap with the local scope. 
     * These parameters are then used to verify that the specie is used in the CRN, and check if the 
     * specie in the local scope.
     * @Param specie
     * @Param local
     * @return
     */
    public boolean VerifySpecie(String specie, HashMap<String, SymbolTableType> local){
        if(local != null && local.containsKey(ViableVariable.CRN)){
            return (!local.containsKey(ViableVariable.SPECIE) || ContaionsNotUsed(local, specie)) && UsedInCRN(specie, local.get(ViableVariable.CRN).crn);
        }
        return false;
    }

    /***
     * This method takes the sample and the specie, and checks wheter the specie is used in the sample.
     * @Param sample
     * @Param specie
     * @return
     */
    public boolean ContaionsNotUsed(HashMap<String, SymbolTableType> sample, String specie){
        return sample.containsKey(ViableVariable.SPECIE) && !sample.get(ViableVariable.SPECIE).species.containsKey(specie);
    }

    /***
     * This method takes the reactions and the specie, and checks wheter the specie is used in one of the 
     * reaction sides.
     * @Param reactions
     * @Param specie
     * @return
     */
    private boolean UsedInCRN(String specie, List<reaction> reactions){
        for (reaction r: reactions) {
            if (CheckOnePairlist(r.lhsPair, specie) || CheckOnePairlist(r.rhsPair, specie)){
                return true;
            }
        }
        return false;
    }

    /***
     * This method takes the left or the right side of the reaction, and checks wether the 
     * specie is used in one of them. The method will return a boolean, depending on the result.
     * @Param pairs
     * @Param specie
     * @return
     */
    private boolean CheckOnePairlist(List<BiksPair<String, String>> pairs, String specie){
        for (BiksPair<String, String> p: pairs) {
            if (specie.equals(p.getKey())){
                return true;
            }
        }
        return false;
    }

    /***
     * This method, is used to generate the dispose operation of the language, it will enter the conditional
     * statement, depending if the dispose is based on percentage, or if it is the whole sample that gets 
     * disposed. The string is then returned to the ApplyProtocols method.
     * @Param protocol
     * @return
     */
    public String ApplyDispose(protocolOperation protocol){
        String prettyResult = "";
        if (protocol.dispose.Procentage != null)
        {
            prettyResult += "disposePercent("+GetSampleName(protocol.dispose.InputSample)+".sample,"+protocol.dispose.Procentage+")\n";
        }
        else
            {
                prettyResult += "dispose("+GetSampleName(protocol.dispose.InputSample)+".sample)\n";
            }
        return prettyResult;
    }

    /***
     * This method generates the split operation in the python code. When the generation is done, the string
     * will be returned to the ApplyProtocols method.
     * @Param protocols
     * @return
     */
    public String ApplySplit(protocolOperation protocol){
        String prettyResult = "";

        prettyResult += "split("+GetSampleName(protocol.split.SplitSample)+".sample,[" +prettyMixers(protocol.split.ResultingSamples)+"], ["+prettyDistribution(protocol.split.DestributionValue)+"])\n";
        prettyResult += AddSpecies(protocol.split.DestributionValue, protocol.split.ResultingSamples, protocol.split.SplitSample);
        return prettyResult;
    }

    public String AddSpecies(List<String> procent , List<String> toSample, String fromSample)
    {
        String prettyResult = "";
        boolean newSpecies = false;
        if(global.get(fromSample).scope.get(vv.SPECIE).species == null)
        {
            return prettyResult;
        }

        for(int i = 0; i<toSample.size(); i++) {
            if (global.get(toSample.get(i)).scope.get(vv.SPECIE).species == null){
                global.get(toSample.get(i)).scope.get(vv.SPECIE).species = new HashMap<>();
            }
            for (String species: global.get(fromSample).scope.get(vv.SPECIE).species.keySet() ) {
                float percent = Float.parseFloat(procent.get(i));
                float newConcentration = Float.parseFloat(global.get(fromSample).scope.get(vv.SPECIE).species.get(species)) * percent;

                if (global.get(toSample.get(i)).scope.get(vv.SPECIE).species.containsKey(species)){
                    float updatedConcentration = Float.parseFloat(global.get(toSample.get(i)).scope.get(vv.SPECIE).species.get(species)) + newConcentration;
                    global.get(toSample.get(i)).scope.get(vv.SPECIE).species.replace(species, String.valueOf(updatedConcentration));
                }else {
                    newSpecies = true;
                    global.get(toSample.get(i)).scope.get(vv.SPECIE).species.put(species, String.valueOf(newConcentration));
                }
            }
            if (newSpecies){
                prettyResult += euler.Generate(global.get(toSample.get(i)).scope,level,Integer.toString(mixCount));
                prettyResult += GetSampleName(toSample.get(i))+".Euler = Euler" + mixCount++ + "\n";
                newSpecies = false;
            }
        }
        return prettyResult;
    }

    /***
     * Takes a list of ratios, of the samples that are getting splitted. The ratio will affect the destribution
     * of the split operation.
     * @Param prettyList
     * @return
     */
    private String prettyDistribution(List<String> prettyList)
    {
        String prettyResult = "";
        for (String str:prettyList)
        {
            prettyResult += str+", ";
        }
        prettyResult = prettyResult.substring(0,prettyResult.length() -2);
        return prettyResult;
    }

}
