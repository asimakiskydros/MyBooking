package misc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyPairTest {

    MyPair pair;

    @Before
    public void setUp() throws Exception {
        pair = new MyPair(100, 200);
    }
    @Test
    public void testGet(){
        assertEquals(100, (int) pair.getHead());
        assertEquals(200, (int) pair.getValue());
    }
}