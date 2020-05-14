package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import simpleAdder.interpret.Objects.ProtocolOBJ.Mix;
import simpleAdder.interpret.Objects.SymolTableOBJ.reaction;
import simpleAdder.interpret.Objects.SymolTableOBJ.protocolOperation;
import simpleAdder.interpret.Objects.SymolTableOBJ.SymbolTableType;
import simpleAdder.interpret.Objects.SymolTableOBJ.titration;
import simpleAdder.interpret.TypeCheckers.BiksPair;
import simpleAdder.interpret.TypeCheckers.Check;

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
            if(global.get(mixer).scope.containsKey(vv.CRN))
            {
                reacs.addAll(global.get(mixer).scope.get(vv.CRN).crn);
            }
            if (global.get(mixer).scope.containsKey(vv.ADDMOL))
            {
                addmol.addAll(global.get(mixer).scope.get(vv.ADDMOL).titrations);
            }
            if (global.get(mixer).scope.containsKey(vv.REMMOL))
            {
                remmol.addAll(global.get(mixer).scope.get(vv.REMMOL).titrations);
            }

        }

        AddToReaction(protocol.mix, vv.CRN, reacs, new SymbolTableType(vv.CRN,vv.CRN,reacs));
        AddToReaction(protocol.mix, vv.ADDMOL, reacs, new SymbolTableType(vv.ADDMOL,addmol, vv.ADDMOL));
        AddToReaction(protocol.mix, vv.REMMOL, reacs, new SymbolTableType(vv.REMMOL,remmol, vv.REMMOL));

        prettyResult += euler.Generate(global.get(protocol.mix.ResultingSample).scope,level,Integer.toString(mixCount));
        prettyResult += GetSampleName(protocol.mix.ResultingSample)+".Euler = Euler"+mixCount+"\n";

        prettyResult += titration.Generate(global.get(protocol.mix.ResultingSample).scope.get(vv.ADDMOL).titrations, global.get(protocol.mix.ResultingSample).scope.get(vv.REMMOL).titrations, level,Integer.toString(mixCount));
        prettyResult += GetSampleName(protocol.mix.ResultingSample)+".ApplyTitration = ApplyTitration"+mixCount+"\n";

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
        String PrettyResult = "";
        PrettyResult += "equilibrate("+ GetSampleName(protocol.equili.sample) +", "+ protocol.equili.stepSize +", "+ protocol.equili.amount + ", " + protocol.equili.timeInterval + ")\n";

        if (global.containsKey(vv.SPECIE)){
            for (String key: global.get(vv.SPECIE).species.keySet()){
                if (VerifySpecie(key, local)){
                    PrettyResult += ApplyTab(level, "sample[\""+ key +"\"] = [" + GetSampleName(protocol.equili.sample) + ".sample.get(\"" + key + "\")[-1]]\n");
                }
            }
        }

        PrettyResult += ApplyTab(level, "Species" + equilibrateCount + ", Steps" + equilibrateCount + ", name" + equilibrateCount + ", taken" + equilibrateCount++ +
                " = SaveGraph(" + GetSampleName(protocol.equili.sample) + ", \"" + protocol.equili.sample + "\", " + GetSampleName(protocol.equili.sample) + ".steps )\n");

        return PrettyResult;
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
        if(local != null && local.containsKey(vv.CRN)){
            if ((!local.containsKey(vv.SPECIE) || ContaionsNotUsed(local, specie)) && UsedInCRN(specie, local.get(vv.CRN).crn)){
                return true;
            }
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
        return sample.containsKey(vv.SPECIE) && !sample.get(vv.SPECIE).species.containsKey(specie);
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
