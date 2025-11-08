package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_LENGTH = 6;
    private static final int MIN_AGE = 18;
    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storage) {
        this.storageDao = storage;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User should not be null.");
        }
        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("User with login - \""
                    + user.getLogin() + "\" already exists.");
        }
        if (user.getLogin() == null) {
            throw new RegistrationException("Login should not be null.");
        }
        if (user.getPassword() == null) {
            throw new RegistrationException("Password should not be null.");
        }
        if (user.getAge() == null) {
            throw new RegistrationException("Age should not be null.");
        }
        if (user.getLogin().length() < MIN_LENGTH) {
            throw new RegistrationException("Login should contain at least 6 symbols.");
        }
        if (user.getPassword().length() < MIN_LENGTH) {
            throw new RegistrationException("Not valid password length: "
                    + user.getPassword().length()
                    + ". Min allowed password " + "length is: " + MIN_LENGTH);
        }
        if (user.getAge() < MIN_AGE) {
            throw new RegistrationException("Not valid age: "
                    + user.getAge() + ". Min allowed age is "
                    + MIN_AGE);
        }
        return storageDao.add(user);
    }
}
