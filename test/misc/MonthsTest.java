package misc;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonthsTest {

    Months month;

    @Before
    public void setUp() throws Exception {
        month = Months.JANUARY;
    }
    @Test
    public void getCodeTest(){
        assertEquals(1, month.getCode());
    }
}