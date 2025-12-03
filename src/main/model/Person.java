package model;

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
        boolean hasKorean = trimmed.matches(".*[\uAC00-\uD7A3].*");
        boolean hasEnglish = trimmed.matches(".*[A-Za-z].*");
        if (hasKorean && hasEnglish) {
            throw new ValidationException("Name cannot mix Korean and English");
        }
        if (hasKorean) {
            if (!trimmed.matches("[\uAC00-\uD7A3]+")) {
                throw new ValidationException("Name contains invalid characters for Korean name");
            }
        } else if (hasEnglish) {
            if (!trimmed.matches("[A-Za-z]+")) {
                throw new ValidationException("Name contains invalid characters for English name");
            }
        } else {
            throw new ValidationException("Name must contain only Korean or English letters");
        }
        this.name = trimmed;
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
    public abstract boolean login(String myId, String myPassWd);
}

