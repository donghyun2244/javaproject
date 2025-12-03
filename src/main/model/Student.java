package model;

import main.exception.ValidationException;

public class Student extends Person {
    public Student(String name, String myId, String myPassWd) throws ValidationException {
        super(name, myId, myPassWd);
    }

    @Override
    public boolean login(String myId, String myPassWd) {
        if (!this.getMyId().equals(myId)) {
            return false;
        }
        return this.compPassWd(myPassWd);
    }
}

