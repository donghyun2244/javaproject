package main.repository;

import java.util.List;

import main.exception.AuthenticationException;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import main.model.Person;

public interface UserRepository {
    Person add(Person user) throws ValidationException, DuplicateException;
    Person getById(String userId) throws ValidationException, NotFoundException;
    List<Person> getAll();
    boolean existsById(String userId) throws ValidationException;
    void changePassword(String userId, String newPassword)
            throws ValidationException, NotFoundException;
    void remove(String userId) throws ValidationException, NotFoundException;
    Person authenticate(String userId, String password)
            throws ValidationException, AuthenticationException;
}

