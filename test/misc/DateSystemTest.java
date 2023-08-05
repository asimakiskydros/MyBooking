package misc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateSystemTest {

    DateSystem dateManager;

    @Before
    public void setUp() throws Exception {
        dateManager = new DateSystem();
    }
    @Test
    public void getDateCodeTest(){
        assertEquals(20210101, dateManager.getDateCode(1,1,2021));
    }

}