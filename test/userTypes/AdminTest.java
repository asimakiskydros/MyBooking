package userTypes;

import accommodationTypes.Accommodation;
import data.Database;
import data.Initializer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class AdminTest {

    Admin admin;

    @Before
    public void setUp() throws Exception{
        admin = new Admin(
                "administrator",
                "123",
                "pentagon",
                "REDACTED",
                "6940404040"
        );
    }
    @Test
    public void sendMessageTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        User user = new Customer(
                "test",
                "123",
                "test",
                "test",
                "test",
                true
        );
        admin.sendMessage("Hello!", user, tempDatabase);
        assertTrue(user.getMessages().containsKey("Hello!"));
        assertTrue(user.getMessages().containsValue(admin.getName()));
    }
    @Test
    public void searchUsersTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        User result;
        result = Admin.searchUsers("PeterPumpkinEater1337", tempDatabase);
        assertNotNull(result);
        assertEquals("PeterPumpkinEater1337", result.getUsername());
    }
    @Test
    public void searchAccommodationsTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        Accommodation result;
        result = Admin.searchAccommodations("Ocean Retreat", tempDatabase);
        assertNotNull(result);
        assertEquals("Ocean Retreat", result.getName());
    }
}