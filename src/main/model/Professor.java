package model;

import main.exception.ValidationException;

public class Professor extends Person {
    public Professor(String name, String myId, String myPassWd) throws ValidationException {
        super(name, myId, myPassWd);
    }

    @Override
    public boolean login(String myId, String myPassWd) {
        if (getMyId() == null || myId == null) {
            return false;
        }
        if (!getMyId().equals(myId)) {
            return false;
        }
        return compPassWd(myPassWd);
    }
}

