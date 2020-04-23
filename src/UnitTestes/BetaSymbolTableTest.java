package UnitTestes;

import org.junit.jupiter.api.Test;
import simpleAdder.interpret.BetaSymbolTable;

import static org.junit.jupiter.api.Assertions.*;

class BetaSymbolTableTest {

    BetaSymbolTable BST = new BetaSymbolTable();
    TestGetMethods get = new TestGetMethods();

    @Test
    void stackToString() {
        String ExpectedResult = "1+2/9*8/2*2**2-(2+2)+2**2+2**(3-1)+(-1)";
        String ActualResult = BST.StackToString(get.InitializedStack());

        assertEquals(ExpectedResult, ActualResult);
    }
}