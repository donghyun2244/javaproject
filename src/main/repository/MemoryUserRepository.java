package main.repository;

import java.util.ArrayList;

import main.exception.AuthenticationException;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ReferentialIntegrityException;
import main.exception.ValidationException;
import main.model.Chancellor;
import main.model.Person;
import main.model.Student;
import main.model.Professor;

public class MemoryUserRepository implements UserRepository {
    private static UserRepository instance;
    private final ArrayList<Person> users;

    private MemoryUserRepository() {
        this.users = new ArrayList<>();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new MemoryUserRepository();
        }
        return instance;
    }

    @Override
    public void add(Person p) throws DuplicateException, ValidationException {
        if (p == null) {
            throw new ValidationException("Person cannot be null");
        }
        String myId = p.getMyId();
        if (checkExistByMyId(myId)) {
            throw new DuplicateException("Duplicate ID: " + myId);
        }
        this.users.add(p);
    }

    public boolean checkExistByMyId(String myId) throws ValidationException {
        validateMyId(myId);
        for (Person user : users) {
            if (user.getMyId().equals(myId.trim())) {
                return true;
            }
        }
        return false;
    }

    public Person findByMyId(String myId) throws NotFoundException, ValidationException {
        validateMyId(myId);
        String trimmed = myId.trim();
        for (Person user : users) {
            if (user.getMyId().equals(trimmed)) {
                return clonePerson(user);
            }
        }
        throw new NotFoundException("User not found: " + trimmed);
    }

    @Override
    public ArrayList<Person> getAll() {
        ArrayList<Person> copy = new ArrayList<>();
        for (Person user : users) {
            copy.add(clonePerson(user));
        }
        return copy;
    }

    @Override
    public void update(Person p) throws NotFoundException, ValidationException {
        if (p == null) {
            throw new ValidationException("Person cannot be null");
        }
        String myId = p.getMyId();
        validateMyId(myId);
        String trimmed = myId.trim();
        for (int i = 0; i < users.size(); i++) {
            Person current = users.get(i);
            if (current.getMyId().equals(trimmed)) {
                users.set(i, p);
                return;
            }
        }
        throw new NotFoundException("User not found: " + trimmed);
    }

    @Override
    public void remove(String myId) throws NotFoundException, ReferentialIntegrityException {
        String trimmed = myId == null ? null : myId.trim();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new NotFoundException("User not found: " + myId);
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getMyId().equals(trimmed)) {
                users.remove(i);
                return;
            }
        }
        throw new NotFoundException("User not found: " + trimmed);
    }

    private void validateMyId(String myId) throws ValidationException {
        if (myId == null) {
            throw new ValidationException("ID cannot be null");
        }
        String trimmed = myId.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("ID cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("ID must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("ID must contain only English letters and digits");
        }
    }

    private Person clonePerson(Person original) {
        try {
            if (original instanceof Student) {
                return new Student(original.getName(), original.getMyId(), original.getMyId() + "1234"); 
            }
            if (original instanceof Professor) {
                return new Professor(original.getName(), original.getMyId(), original.getMyId() + "1234");
            }
            if (original instanceof Chancellor) {
                return new Chancellor(original.getName(), original.getMyId(), original.getMyId() + "1234");
            }
            return original; 
            
        } catch (ValidationException e) {
            throw new RuntimeException("객체 복사 중 오류 발생", e);
        }
    }

    @Override
    public Person getById(String userId) throws ValidationException, NotFoundException {
        return findByMyId(userId);
    }

    @Override
    public boolean existsById(String userId) throws ValidationException {
        return checkExistByMyId(userId);
    }

    @Override
    public void changePassword(String userId, String newPassword) throws ValidationException, NotFoundException {
        validateMyId(userId);
        String trimmed = userId.trim();
        for (Person user : users) {
            if (user.getMyId().equals(trimmed)) {
                user.setMyPassWd(newPassword);
                return;
            }
        }
        throw new NotFoundException("User not found: " + trimmed);
    }

    @Override
    public Person authenticate(String userId, String password) throws ValidationException, AuthenticationException {
        try {
            Person p = findByMyId(userId);
            if (!p.compPassWd(password)) {
                throw new AuthenticationException("Invalid credentials");
            }
            return p;
        } catch (NotFoundException e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}

