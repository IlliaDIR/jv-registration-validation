package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final int VALID_AGE = 18;
    private static final String VALID_LOGIN = "UserLogin1";
    private static final String VALID_PASSWORD = "Passwd";
    private RegistrationService registrationService;
    private StorageDao storageDao;
    private User user;

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        storageDao = new StorageDaoImpl();
        registrationService = new RegistrationServiceImpl(storageDao);
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
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);
        user.setAge(12);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        user.setAge(17);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        user.setAge(0);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_ageOver18_Ok() {
        user.setPassword(VALID_PASSWORD);
        user.setLogin(VALID_LOGIN);
        user.setAge(19);
        User saved = registrationService.register(user);
        assertNotNull(storageDao.get(user.getLogin()));
        assertEquals(user, saved);
        User user1 = new User();
        user1.setPassword(VALID_PASSWORD);
        user1.setLogin("anotherUser");
        user1.setAge(27);
        User saved1 = registrationService.register(user1);
        assertNotNull(storageDao.get(user1.getLogin()));
        assertEquals(user1, saved1);
    }

    @Test
    void register_ageEquals18_Ok() {
        user.setPassword(VALID_PASSWORD);
        user.setLogin(VALID_LOGIN);
        user.setAge(VALID_AGE);
        User saved = registrationService.register(user);
        assertEquals(user, saved);
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
        user.setAge(VALID_AGE);
        user.setLogin(VALID_LOGIN);
        user.setPassword("testPass");
        User saved = registrationService.register(user);
        assertEquals(user, saved);
        User user1 = new User();
        user1.setAge(25);
        user1.setLogin("qweasfg");
        user1.setPassword("edge12");
        User saved1 = registrationService.register(user1);
        assertEquals(user1, saved1);
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
        storageDao.add(user);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }
}
