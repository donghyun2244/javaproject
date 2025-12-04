package main.repository;

import java.util.ArrayList;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import model.Subject;

public interface SubjectRepository {
    void add(Subject s) throws DuplicateException, ValidationException;
    boolean checkExistByIdNum(String idNum) throws ValidationException;
    Subject findByIdNum(String idNum) throws NotFoundException, ValidationException;
    ArrayList<Subject> getAll();
    void update(Subject s) throws NotFoundException, ValidationException;
    void remove(String idNum) throws NotFoundException;
}
