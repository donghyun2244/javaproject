package main.model;

import main.exception.ValidationException;

public class Chancellor extends Person {

    public Chancellor(String name, String myId, String myPassWd) throws ValidationException {
        super(name, myId, myPassWd);
    }

    @Override
    public boolean login(String myId, String myPassWd) {
        if (myId == null || myPassWd == null) {
            return false;
        }
        if (!this.getMyId().equals(myId)) {
            return false;
        }
        return this.compPassWd(myPassWd);
    }
}

