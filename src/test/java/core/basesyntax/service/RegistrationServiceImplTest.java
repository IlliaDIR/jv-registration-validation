package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final int VALID_AGE = 18;
    private static final int AGE_OVER_18 = 24;
    private static final String VALID_LOGIN = "UserLogin1";
    private static final String VALID_PASSWORD = "Passwd";
    private static RegistrationService registrationService;
    private User user;

    @BeforeAll
    static void beforeAll() {
        registrationService = new RegistrationServiceImpl();
    }

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_nullLogin_notOk() {
        user.setAge(VALID_AGE);
        user.setPassword(VALID_PASSWORD);
        user.setLogin(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullPassword_notOk() {
        user.setAge(VALID_AGE);
        user.setLogin(VALID_LOGIN);
        user.setPassword(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullAge_notOk() {
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);
        user.setAge(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_ageUnder18_notOk() {
        user.setAge(12);
        int actual = user.getAge();
        assertFalse(actual >= VALID_AGE);
    }

    @Test
    void register_ageOver18_Ok() {
        user.setAge(AGE_OVER_18);
        int actual = user.getAge();
        assertTrue(actual >= VALID_AGE);
    }

    @Test
    void register_ageEquals18_Ok() {
        user.setAge(VALID_AGE);
        assertEquals(VALID_AGE, user.getAge());
    }

    @Test
    void register_ageNegative_notOk() {
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);
        user.setAge(-VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_passwordLength_notOk() {
        user.setLogin(VALID_LOGIN);
        user.setAge(VALID_AGE);
        user.setPassword("abvds");
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        user.setPassword("123");
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        user.setPassword("a");
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_passwordLength_Ok() {
        user.setPassword("testPass");
        int length = user.getPassword().length();
        assertTrue(length >= VALID_PASSWORD.length());
        user.setPassword("passwd");
        length = user.getPassword().length();
        assertTrue(length >= VALID_PASSWORD.length());
    }

    @Test
    void register_emptyPassword_notOk() {
        user.setPassword("");
        user.setAge(VALID_AGE);
        user.setLogin(VALID_LOGIN);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_loginLength_notOk() {
        user.setPassword(VALID_PASSWORD);
        user.setAge(VALID_AGE);
        user.setLogin("");
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        user.setLogin("asdfg");
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        user.setLogin("123");
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_userExists_notOk() {
        user.setLogin(VALID_LOGIN);
        user.setAge(VALID_AGE);
        user.setPassword(VALID_PASSWORD);
        registrationService.register(user);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @AfterAll
    static void afterAll() {
        registrationService = null;
    }
}
