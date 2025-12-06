package main.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;

public class Subject {
    private final String idNum;
    private String subjectName;
    private String professorId;
    private ArrayList<String> studentsId;
    private HashMap<String, Integer> scores;

    private String validateId(String idNum) throws ValidationException {
        if (idNum == null) {
            throw new ValidationException("ID cannot be null");
        }
        String trimmed = idNum.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("ID cannot be empty");
        }
        if (trimmed.length() != 4) {
            throw new ValidationException("ID must be exactly 4 characters");
        }
        if (!trimmed.matches("[0-9]{4}")) {
            throw new ValidationException("ID must contain only digits");
        }
        return trimmed;
    }

    public Subject(String idNum, String subjectName, String professorId, ArrayList<String> studentsId,
            HashMap<String, Integer> scores) throws ValidationException {
        this.idNum = validateId(idNum);
        this.studentsId = new ArrayList<String>();
        this.scores = new HashMap<String, Integer>();
        this.setSubjectName(subjectName);
        this.setProfessorId(professorId);
        if (studentsId != null) {
            for (String s : studentsId) {
                try {
                    this.addStudent(s);
                } catch (DuplicateException e) {
                }
            }
        }
        if (scores != null) {
            for (Map.Entry<String, Integer> e : scores.entrySet()) {
                String sid = e.getKey();
                Integer val = e.getValue();
                try {
                    if (sid != null) {
                        this.setScore(sid, val == null ? 0 : val);
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    public Subject(String idNum, String subjectName, String professorId) throws ValidationException {
        this.idNum = validateId(idNum);
        this.studentsId = new ArrayList<String>();
        this.scores = new HashMap<String, Integer>();
        this.setSubjectName(subjectName);
        this.setProfessorId(professorId);
    }

    public String getIdNum() {
        return this.idNum;
    }

    public void setSubjectName(String subjectName) throws ValidationException {
        if (subjectName == null) {
            throw new ValidationException("Subject name cannot be null");
        }
        String trimmed = subjectName.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Subject name cannot be empty");
        }
        boolean hasKorean = trimmed.matches(".*[\uAC00-\uD7A3].*");
        boolean hasEnglish = trimmed.matches(".*[A-Za-z].*");
        if (hasKorean && hasEnglish) {
            throw new ValidationException("Subject name cannot mix Korean and English");
        }
        if (hasKorean) {
            if (!trimmed.matches("[\uAC00-\uD7A3 ]+")) {
                throw new ValidationException("Subject name contains invalid characters for Korean name");
            }
        } else if (hasEnglish) {
            if (!trimmed.matches("[A-Za-z ]+")) {
                throw new ValidationException("Subject name contains invalid characters for English name");
            }
        } else {
            throw new ValidationException("Subject name must contain Korean or English letters");
        }
        this.subjectName = trimmed;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public void setProfessorId(String professorId) throws ValidationException {
        if (professorId == null) {
            throw new ValidationException("Professor ID cannot be null");
        }
        String trimmed = professorId.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Professor ID cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("Professor ID must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("Professor ID must contain only English letters and digits");
        }
        this.professorId = trimmed;
    }

    public String getProfessorId() {
        return this.professorId;
    }

    public void addStudent(String studentId) throws ValidationException, DuplicateException {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        String trimmed = studentId.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Student ID cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("Student ID must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("Student ID must contain only English letters and digits");
        }
        if (this.studentsId.contains(trimmed)) {
            throw new DuplicateException("Student already enrolled");
        }
        this.studentsId.add(trimmed);
    }

    public void removeStudent(String studentId) throws ValidationException, NotFoundException {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        String trimmed = studentId.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Student ID cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("Student ID must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("Student ID must contain only English letters and digits");
        }
        if (!this.studentsId.contains(trimmed)) {
            throw new NotFoundException("Student not enrolled");
        }
        this.studentsId.remove(trimmed);
        if (this.scores.containsKey(trimmed)) {
            this.scores.remove(trimmed);
        }
    }

    public void setScore(String studentId, int score) throws ValidationException, NotFoundException {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        String trimmed = studentId.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Student ID cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("Student ID must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("Student ID must contain only English letters and digits");
        }
        if (score < 0 || score > 100) {
            throw new ValidationException("Score must be between 0 and 100");
        }
        if (!this.studentsId.contains(trimmed)) {
            throw new NotFoundException("Student not enrolled");
        }
        this.scores.put(trimmed, Integer.valueOf(score));
    }

    public Integer getScore(String studentId) throws ValidationException, NotFoundException {
        return findScoreByStudentId(studentId);
    }

    public boolean checkExistByStudentId(String studentId) throws ValidationException {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        String trimmed = studentId.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Student ID cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("Student ID must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("Student ID must contain only English letters and digits");
        }
        return this.studentsId.contains(trimmed);
    }

    public Integer findScoreByStudentId(String studentId) throws ValidationException, NotFoundException {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        String trimmed = studentId.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException("Student ID cannot be empty");
        }
        if (trimmed.length() < 4) {
            throw new ValidationException("Student ID must be at least 4 characters");
        }
        if (!trimmed.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("Student ID must contain only English letters and digits");
        }
        if (!this.scores.containsKey(trimmed)) {
            throw new NotFoundException("Score not found for student");
        }
        return this.scores.get(trimmed);
    }

    public ArrayList<String> getStudentsId() {
        ArrayList<String> copy = new ArrayList<String>();
        for (String s : this.studentsId) {
            copy.add(s);
        }
        return copy;
    }

    public HashMap<String, Integer> getScores() {
        HashMap<String, Integer> copy = new HashMap<String, Integer>();
        for (Map.Entry<String, Integer> e : this.scores.entrySet()) {
            copy.put(e.getKey(), e.getValue());
        }
        return copy;
    }
}

