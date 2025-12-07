package main.repository;

import java.util.ArrayList;
import java.util.HashMap;

import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import main.model.Subject;

public class MemorySubjectRepository implements SubjectRepository {
    private static SubjectRepository instance;
    private final ArrayList<Subject> subjects;

    private MemorySubjectRepository() {
        this.subjects = new ArrayList<>();
    }

    public static SubjectRepository getInstance() {
        if (instance == null) {
            instance = new MemorySubjectRepository();
        }
        return instance;
    }

    @Override
    public void add(Subject s) throws DuplicateException, ValidationException {
        if (s == null) {
            throw new ValidationException("과목은 비어있을 수 없습니다.");
        }
        String idNum = s.getIdNum();
        validateIdNum(idNum);
        if (checkExistByIdNum(idNum)) {
            throw new DuplicateException("Duplicate subject id: " + idNum);
        }
        subjects.add(s);
    }

    @Override
    public boolean checkExistByIdNum(String idNum) throws ValidationException {
        validateIdNum(idNum);
        String trimmed = idNum.trim();
        for (Subject subject : subjects) {
            if (subject.getIdNum().equals(trimmed)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Subject findByIdNum(String idNum) throws NotFoundException, ValidationException {
        validateIdNum(idNum);
        String trimmed = idNum.trim();
        for (Subject subject : subjects) {
            if (subject.getIdNum().equals(trimmed)) {
                return cloneSubject(subject);
            }
        }
        throw new NotFoundException("Subject not found: " + trimmed);
    }

    @Override
    public ArrayList<Subject> getAll() {
        ArrayList<Subject> copy = new ArrayList<>();
        for (Subject s : subjects) {
            copy.add(cloneSubject(s));
        }
        return copy;
    }

    @Override
    public void update(Subject s) throws NotFoundException, ValidationException {
        if (s == null) {
            throw new ValidationException("과목은 비어있을 수 없습니다.");
        }
        String idNum = s.getIdNum();
        validateIdNum(idNum);
        String trimmed = idNum.trim();
        for (int i = 0; i < subjects.size(); i++) {
            Subject current = subjects.get(i);
            if (current.getIdNum().equals(trimmed)) {
                subjects.set(i, s);
                return;
            }
        }
        throw new NotFoundException("Subject not found: " + trimmed);
    }

    @Override
    public void remove(String idNum) throws NotFoundException {
        try {
            validateIdNum(idNum);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        String trimmed = idNum.trim();
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getIdNum().equals(trimmed)) {
                subjects.remove(i);
                return;
            }
        }
        throw new NotFoundException("Subject not found: " + trimmed);
    }

    private void validateIdNum(String idNum) throws ValidationException {
        if (idNum == null) {
            throw new ValidationException("ID는 비어있을 수 없습니다.");
        }
        String trimmed = idNum.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("ID는 비어있을 수 없습니다.");
        }
        if (trimmed.length() != 4) {
            throw new ValidationException("ID는 정확히 4자여야 합니다.");
        }
        if (!trimmed.matches("[0-9]{4}")) {
            throw new ValidationException("ID는 숫자만 포함할 수 있습니다.");
        }
    }

    private Subject cloneSubject(Subject original) {
        try {
            ArrayList<String> studentsCopy = original.getStudentsId();
            HashMap<String, Integer> scoresCopy = original.getScores();
            return new Subject(original.getIdNum(), original.getSubjectName(), original.getProfessorId(), studentsCopy,
                    scoresCopy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

