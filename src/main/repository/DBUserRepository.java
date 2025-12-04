package main.repository;

import java.util.ArrayList;

import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ReferentialIntegrityException;
import main.exception.ValidationException;
import model.Person;

public class DBUserRepository implements UserRepository {
    private static UserRepository instance;

    private DBUserRepository() {
    }

    public static UserRepository getInstance() {
        throw new UnsupportedOperationException("DBUserRepository is not implemented");
    }

    @Override
    public void add(Person p) throws DuplicateException, ValidationException {
        throw new UnsupportedOperationException("DBUserRepository.add is not implemented");
    }

    @Override
    public boolean checkExistByMyId(String myId) throws ValidationException {
        throw new UnsupportedOperationException("DBUserRepository.checkExistByMyId is not implemented");
    }

    @Override
    public Person findByMyId(String myId) throws NotFoundException, ValidationException {
        throw new UnsupportedOperationException("DBUserRepository.findByMyId is not implemented");
    }

    @Override
    public ArrayList<Person> getAll() {
        throw new UnsupportedOperationException("DBUserRepository.getAll is not implemented");
    }

    @Override
    public void update(Person p) throws NotFoundException, ValidationException {
        throw new UnsupportedOperationException("DBUserRepository.update is not implemented");
    }

    @Override
    public void remove(String myId) throws NotFoundException, ReferentialIntegrityException {
        throw new UnsupportedOperationException("DBUserRepository.remove is not implemented");
    }
}

