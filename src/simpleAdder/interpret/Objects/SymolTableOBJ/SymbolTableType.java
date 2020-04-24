package simpleAdder.interpret.Objects.SymolTableOBJ;

import java.util.HashMap;
import java.util.List;

public class SymbolTableType {

    public String id;
    public String type;
    public String value;
    public HashMap<String,SymbolTableType> scope;
    public List<reaction> crn = null;
    public function func = null;
    public List<titration> titrations = null;
    public HashMap<String, String> species = null;

    public SymbolTableType(String id, String type, String value)
    {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public SymbolTableType(String id){
        this.id = id;
        this.species = new HashMap<>();
    }

    public SymbolTableType(String id, String type ,HashMap<String,SymbolTableType> scope)
    {
        this.id = id;
        this.type = type;
        this.scope = scope;
    }

    public SymbolTableType(String id, String type ,List<reaction> crn)
    {
        this.id = id;
        this.type = type;
        this.crn = crn;
    }

    public SymbolTableType(String id, String type,function func)
    {
        this.id  = id;
        this.type = type;
        this.func = func;
    }

    public  SymbolTableType(String id, List<titration> titrations ,String type)
    {
        this.id = id;
        this.type = type;
        this.titrations = titrations;
    }
}
