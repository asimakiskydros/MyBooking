package userTypes;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class UserTest {
    User user;

    @Before
    public void setUp() throws Exception {
        user = new User(
                "testUsername",
                "testPassword",
                "testAddress",
                "testName",
                "testPhoneNumber",
                false
        );
    }
    @Test
    public void testSetGet(){
        assertEquals("testUsername", user.getUsername());
        assertEquals(false, user.checkUsername("aUsername"));
        assertEquals(true, user.checkPassword("testPassword"));
        assertEquals("testAddress", user.getAddress());
        assertEquals("testName", user.getName());
        assertEquals("testPhoneNumber", user.getPhoneNumber());
        assertFalse(user.getAdmitted());

        user.setName("MyName");
        user.setAdmitted(true);
        user.setAddress("11 Downing Street");
        user.setPhoneNumber("6989704212");

        assertEquals("11 Downing Street", user.getAddress());
        assertEquals("MyName", user.getName());
        assertEquals("6989704212", user.getPhoneNumber());
        assertTrue(user.getAdmitted());

        HashMap<String, String> tempMessages = new HashMap<>();
        tempMessages.put("Hello!", "Asimakis");
        user.setMessages(tempMessages);
        assertTrue(user.getMessages().containsValue("Asimakis"));
        for(String message : user.getMessages().keySet()){
            assertEquals("Hello!", message);
            assertEquals("Asimakis", user.getMessages().get(message));
        }
    }
}