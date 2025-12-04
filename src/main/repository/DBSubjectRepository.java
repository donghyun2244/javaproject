package main.repository;

import java.util.ArrayList;

import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import model.Subject;

public class DBSubjectRepository implements SubjectRepository {
    private static SubjectRepository instance;

    private DBSubjectRepository() {
    }

    public static SubjectRepository getInstance() {
        throw new UnsupportedOperationException("DBSubjectRepository is not implemented");
    }

    @Override
    public void add(Subject s) throws DuplicateException, ValidationException {
        throw new UnsupportedOperationException("DBSubjectRepository.add is not implemented");
    }

    @Override
    public boolean checkExistByIdNum(String idNum) throws ValidationException {
        throw new UnsupportedOperationException("DBSubjectRepository.checkExistByIdNum is not implemented");
    }

    @Override
    public Subject findByIdNum(String idNum) throws NotFoundException, ValidationException {
        throw new UnsupportedOperationException("DBSubjectRepository.findByIdNum is not implemented");
    }

    @Override
    public ArrayList<Subject> getAll() {
        throw new UnsupportedOperationException("DBSubjectRepository.getAll is not implemented");
    }

    @Override
    public void update(Subject s) throws NotFoundException, ValidationException {
        throw new UnsupportedOperationException("DBSubjectRepository.update is not implemented");
    }

    @Override
    public void remove(String idNum) throws NotFoundException {
        throw new UnsupportedOperationException("DBSubjectRepository.remove is not implemented");
    }
}

