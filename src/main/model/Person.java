package main.model;

import main.exception.ValidationException;

public abstract class Person implements Loginable {
    private String name;
    private final String myId;
    private String myPassWd;

    public Person(String name, String myId, String myPassWd) throws ValidationException {
        this.setName(name);
        this.myId = validateId(myId);
        this.setMyPassWd(myPassWd);
    }

    public void setName(String name) throws ValidationException {
        if (name == null) {
            throw new ValidationException("Name cannot be null");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Name cannot be empty");
        }

        String normalized = trimmed.replaceAll("\\s+", ""); 
        normalized = normalized.replace("·", ""); 

        if (normalized.isEmpty()) {
            throw new ValidationException("Name cannot be empty after normalization");
        }

        if (normalized.matches("[가-힣]+")) {
            this.name = trimmed;
            return;
        }
        if (normalized.matches("[A-Za-z]+")) {
            this.name = trimmed;
            return;
        }

        throw new ValidationException("Name must contain only Korean or English letters");
    }

    public String getName() {
        return this.name;
    }

    private String validateId(String myId) throws ValidationException {
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
        return trimmed;
    }

    public String getMyId() {
        return this.myId;
    }

    public String getMyPassWd() {
        return this.myPassWd;
    }

    public void setMyPassWd(String myPassWd) throws ValidationException {
        if (myPassWd == null) {
            throw new ValidationException("Password cannot be null");
        }
        String trimmed = myPassWd.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("Password must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9!@#$%]+")) {
            throw new ValidationException("Password contains invalid characters");
        }
        this.myPassWd = trimmed;
    }

    public boolean compPassWd(String myPassWd) {
        if (this.myPassWd == null) {
            return false;
        }
        if (myPassWd == null) {
            return false;
        }
        return this.myPassWd.equals(myPassWd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return myId.equals(person.myId);
    }

    @Override
    public int hashCode() {
        return myId.hashCode();
    }

    @Override
    public abstract boolean login(String myId, String myPassWd);
}

