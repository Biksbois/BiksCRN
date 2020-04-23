package simpleAdder.interpret.ObjectGeneration;

import javafx.util.Pair;
import simpleAdder.interpret.Objects.STObjects.reaction;
import simpleAdder.interpret.SymbolTableType;
import simpleAdder.interpret.ViableVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dict{
    ViableVariable vv = new ViableVariable();
    PrinterMethods PM = new PrinterMethods();

    /***
     * This method generates the head of the sample, and the converts the species to variables from the global and local scope.
     * It finally returns sample dictonary.
     * @Param global
     * @Param local
     * @Param level
     * @return
     */
    public String GenerateDictionary(HashMap<String, SymbolTableType> global, HashMap<String, SymbolTableType> local, int level)
    {
        String PrettyResult = "";
        PrettyResult += PM.ApplyTap(level, GenerateDictInit());
        level++;
        PrettyResult += GenerateDictFromHashmap(local, global, level);

        PrettyResult = PrettyResult.substring(0, PrettyResult.lastIndexOf(",")) + "" + PrettyResult.substring(PrettyResult.lastIndexOf(",") + 1);

        level--;


        return PrettyResult;
    }

    /***
     * This method generates the head of the sample, and the converts the species to variables from the global scope.
     * It finally returns sample dictonary.
     * @Param global
     * @Param level
     * @return
     */
    public String GenerateDictionary(HashMap<String, SymbolTableType> global, int level)
    {
        String PrettyResult = "";
        PrettyResult += PM.ApplyTap(level, GenerateDictInit());
        level++;
        PrettyResult += GenerateDictFromHashmap(global, level);

        PrettyResult = PrettyResult.substring(0, PrettyResult.lastIndexOf(",")) + "" + PrettyResult.substring(PrettyResult.lastIndexOf(",") + 1);

        level--;


        return PrettyResult;
    }

    private String GenerateDictInit()
    {
        return "sample = {\n";
    }

    /***
     * Verifies the species, such that there are no duplicates and unused species in the dictionary. It does so
     * based on both the local and global scope. Finally the relevant species are returned.
     * @Param local
     * @Param global
     * @Param level
     * @return
     */
    public String GenerateDictFromHashmap(HashMap<String, SymbolTableType> local, HashMap<String, SymbolTableType> global, int level){
        String PrettyResult = "";
        HashMap<String, String> UsedSpecie = new HashMap<>();

        if(local.containsKey(vv.CRN)) {
            if (local.containsKey(vv.SPECIE)){
                UniqueSpecieToHash(local.get(vv.SPECIE).species, local.get(vv.CRN).crn, UsedSpecie);

            }
            if (global.containsKey(vv.SPECIE)){
                UniqueSpecieToHash(global.get(vv.SPECIE).species, local.get(vv.CRN).crn, UsedSpecie);
            }
        }

        if(UsedSpecie != null)
        {
            for (Map.Entry<String,String> p: UsedSpecie.entrySet()) {
                PrettyResult += PM.ApplyTap(level,GenerateDictField(p.getKey(), p.getValue()));
            }
        }
        return PrettyResult;
    }

    /***
     * Verifies the species, such that there are no duplicates and unused species in the dictionary. It does so
     * based on the global scope. Finally the relevant species are returned.
     * @Param global
     * @Param level
     * @return
     */
    public String GenerateDictFromHashmap(HashMap<String, SymbolTableType> global, int level){

        String PrettyResult = "";
        HashMap<String, String> UsedSpecie = new HashMap<>();
        if (global.containsKey(vv.SPECIE)){
            UniqueSpecieToHash(global.get(vv.SPECIE).species, UsedSpecie);
        }

        if(UsedSpecie != null)
        {
            for (Map.Entry<String,String> p: UsedSpecie.entrySet()) {
                PrettyResult += PM.ApplyTap(level,GenerateDictField(p.getKey(), p.getValue()));
            }
        }
        return PrettyResult + GenerateDictEnd();
    }

    /***
     * Takes the HashMap of species, and check each entryset to see if the specie already initiated
     * and is used in the CRN.
     * If the specie is not initiated, the specie is added to the UsedSpecies HashMap.
     * @Param species
     * @Param reactiones
     * @Param UsedSpeces
     * @return
     */
    private void UniqueSpecieToHash(HashMap<String, String> species, List<reaction> reactions, HashMap<String, String> UsedSpecies){
        for (Map.Entry<String, String> p : species.entrySet()) {
            if (!UsedSpecies.containsKey(p.getKey()) && UsedInCRN(p.getKey(), reactions)){
                UsedSpecies.put(p.getKey(), p.getValue());
            }
        }
    }

    /***
     * Takes the HashMap of species, and check each entryset to see if the specie already initiated.
     * If the specie is not initiated, the specie is added to the UsedSpecies HashMap.
     * @Param species
     * @Param UsedSpeces
     * @return
     */
    private void UniqueSpecieToHash(HashMap<String, String> species, HashMap<String, String> UsedSpecies){
        for (Map.Entry<String, String> p : species.entrySet()) {
            if (!UsedSpecies.containsKey(p.getKey())){
                UsedSpecies.put(p.getKey(), p.getValue());
            }
        }
    }

    /***
     * Takes a specie, and check if the specie is used in the CRN. A boolean is returned
     * depending on the outcome.
     * @Param specie
     * @Param reactiones
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
     * Takes a specie, and check if the specie is used in the CRN. A boolean is returned
     * depending on the outcome.
     * @Param specie
     * @Param reactiones
     * @return
     */
    private boolean CheckOnePairlist(List<Pair<String, String>> pairs, String specie){
        for (Pair<String, String> p: pairs) {
            if (specie.equals(p.getKey())){
                return true;
            }
        }
        return false;
    }

    private String GenerateDictEnd()
    {
        return "}\n";
    }

    private String GenerateDictField(String key, String value)
    {
        return "\""+key+"\":["+value+"],\n";
    }
}
