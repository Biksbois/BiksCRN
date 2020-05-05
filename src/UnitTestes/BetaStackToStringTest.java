package UnitTestes;

import org.junit.jupiter.api.Test;
import simpleAdder.interpret.TypeCheckers.BetaStackToString;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class BetaStackToStringTest {

    BetaStackToString BSTS = new BetaStackToString();
    TestGetMethods get = new TestGetMethods();

    @Test
    void stackToString() {
        String ExpectedResult = "1+2/9*8/2*2**2-(2+2)+2**2+2**(3-1)+(-1)";
        String ActualResult = BSTS.StackToString(get.InitializedStack());

        assertEquals(ExpectedResult, ActualResult);
    }
}