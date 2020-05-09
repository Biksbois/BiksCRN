package UnitTestes;

import org.junit.jupiter.api.Test;
import simpleAdder.interpret.TypeCheckers.OptimizedStackToString;

import static org.junit.jupiter.api.Assertions.*;

class OptimizedStackToStringTest {

    TestGetMethods TGM = new TestGetMethods();
    OptimizedStackToString OSS = new OptimizedStackToString();

    @Test
    void PlusMinusExpr(){
        String result = OSS.Calculate(TGM.GetPlusMinusStack(false));
        String expected = "-4.00";

        assertEquals(expected, result);
    }

    @Test
    void PlusMinusCycle(){
        String result = OSS.Calculate(TGM.GetPlusMinusStack(true));
        String expected = "-1.0+(i)*(-1)";

        assertEquals(expected, result);
    }

    @Test
    void MultDivideCycle(){
        String result = OSS.Calculate(TGM.GetMultDivide(true));
        String expected = "-4.5625+i**(i)";

        assertEquals(expected, result);
    }

    @Test
    void MultDivideExpr(){
        String result = OSS.Calculate(TGM.GetMultDivide(false));
        String expected = "22.43750";

        assertEquals(expected, result);
    }

    @Test
    void PowerCycle(){
        String result = OSS.Calculate(TGM.GetPower(true));
        String expected = "1.0+(i**(9.00))*(-1)";

        assertEquals(expected, result);
    }

    @Test
    void PowerExpr(){
        String result = OSS.Calculate(TGM.GetPower(false));
        String expected = "-19682.00";

        assertEquals(expected, result);
    }

    @Test
    void OutWeighCycle(){
        String result = OSS.Calculate(TGM.GetOutWeigh());
        String expected = "42.0";

        assertEquals(expected, result);
    }
}