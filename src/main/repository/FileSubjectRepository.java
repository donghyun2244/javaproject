package main.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import model.Subject;

public class FileSubjectRepository implements SubjectRepository {
    private static final String DATA_ROOT = "Data";
    private static final String SUBJECT_ROOT = "Subject";

    private static SubjectRepository instance;

    private FileSubjectRepository() {
    }

    public static SubjectRepository getInstance() {
        if (instance == null) {
            instance = new FileSubjectRepository();
        }
        return instance;
    }

    @Override
    public void add(Subject s) throws DuplicateException, ValidationException {
        if (s == null) {
            throw new ValidationException("Subject cannot be null");
        }
        String idNum = s.getIdNum();
        validateIdNum(idNum);
        String subjectName = s.getSubjectName();
        Path dir = buildSubjectDir(subjectName);
        if (Files.exists(dir)) {
            throw new DuplicateException("Duplicate subject folder: " + subjectName);
        }
        try {
            Files.createDirectories(dir);
            writeString(dir.resolve("idNum.txt"), s.getIdNum());
            writeString(dir.resolve("subjectName.txt"), s.getSubjectName());
            writeString(dir.resolve("professorId.txt"), s.getProfessorId());
            writeLines(dir.resolve("studentsId.txt"), s.getStudentsId());
            writeScores(dir.resolve("scores.txt"), s.getScores());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkExistByIdNum(String idNum) throws ValidationException {
        validateIdNum(idNum);
        Path base = Paths.get(DATA_ROOT, SUBJECT_ROOT);
        if (!Files.exists(base)) {
            return false;
        }
        try {
            return Files.list(base).anyMatch(dir -> {
                if (!Files.isDirectory(dir)) {
                    return false;
                }
                Path idFile = dir.resolve("idNum.txt");
                if (!Files.exists(idFile)) {
                    return false;
                }
                try {
                    String stored = readString(idFile);
                    return stored.equals(idNum.trim());
                } catch (IOException e) {
                    return false;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Subject findByIdNum(String idNum) throws NotFoundException, ValidationException {
        validateIdNum(idNum);
        Path dir = findSubjectDirByIdNum(idNum);
        if (dir == null) {
            throw new NotFoundException("Subject not found: " + idNum);
        }
        try {
            String storedId = readString(dir.resolve("idNum.txt"));
            String subjectName = readString(dir.resolve("subjectName.txt"));
            String professorId = readString(dir.resolve("professorId.txt"));
            ArrayList<String> students = readLines(dir.resolve("studentsId.txt"));
            HashMap<String, Integer> scores = readScores(dir.resolve("scores.txt"));
            return new Subject(storedId, subjectName, professorId, students, scores);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Subject> getAll() {
        ArrayList<Subject> result = new ArrayList<>();
        Path base = Paths.get(DATA_ROOT, SUBJECT_ROOT);
        if (!Files.exists(base)) {
            return result;
        }
        try {
            Files.list(base).filter(Files::isDirectory).forEach(dir -> {
                try {
                    String idNum = readString(dir.resolve("idNum.txt"));
                    String subjectName = readString(dir.resolve("subjectName.txt"));
                    String professorId = readString(dir.resolve("professorId.txt"));
                    ArrayList<String> students = readLines(dir.resolve("studentsId.txt"));
                    HashMap<String, Integer> scores = readScores(dir.resolve("scores.txt"));
                    Subject s = new Subject(idNum, subjectName, professorId, students, scores);
                    result.add(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void update(Subject s) throws NotFoundException, ValidationException {
        if (s == null) {
            throw new ValidationException("Subject cannot be null");
        }
        String idNum = s.getIdNum();
        validateIdNum(idNum);
        Path dir = findSubjectDirByIdNum(idNum);
        if (dir == null) {
            throw new NotFoundException("Subject not found: " + idNum);
        }
        try {
            writeString(dir.resolve("idNum.txt"), s.getIdNum());
            writeString(dir.resolve("subjectName.txt"), s.getSubjectName());
            writeString(dir.resolve("professorId.txt"), s.getProfessorId());
            writeLines(dir.resolve("studentsId.txt"), s.getStudentsId());
            writeScores(dir.resolve("scores.txt"), s.getScores());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String idNum) throws NotFoundException {
        validateIdNum(idNum);
        Path dir = findSubjectDirByIdNum(idNum);
        if (dir == null) {
            throw new NotFoundException("Subject not found: " + idNum);
        }
        try {
            Files.deleteIfExists(dir.resolve("idNum.txt"));
            Files.deleteIfExists(dir.resolve("subjectName.txt"));
            Files.deleteIfExists(dir.resolve("professorId.txt"));
            Files.deleteIfExists(dir.resolve("studentsId.txt"));
            Files.deleteIfExists(dir.resolve("scores.txt"));
            Files.deleteIfExists(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path buildSubjectDir(String subjectName) {
        return Paths.get(DATA_ROOT, SUBJECT_ROOT, subjectName);
    }

    private Path findSubjectDirByIdNum(String idNum) {
        Path base = Paths.get(DATA_ROOT, SUBJECT_ROOT);
        if (!Files.exists(base)) {
            return null;
        }
        try {
            return Files.list(base).filter(Files::isDirectory).filter(dir -> {
                Path idFile = dir.resolve("idNum.txt");
                if (!Files.exists(idFile)) {
                    return false;
                }
                try {
                    String stored = readString(idFile);
                    return stored.equals(idNum.trim());
                } catch (IOException e) {
                    return false;
                }
            }).findFirst().orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeString(Path path, String value) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, value == null ? new byte[0] : value.getBytes(StandardCharsets.UTF_8));
    }

    private String readString(Path path) throws IOException {
        if (!Files.exists(path)) {
            return "";
        }
        return Files.readString(path, StandardCharsets.UTF_8).trim();
    }

    private void writeLines(Path path, ArrayList<String> lines) throws IOException {
        Files.createDirectories(path.getParent());
        ArrayList<String> safe = new ArrayList<>();
        if (lines != null) {
            for (String s : lines) {
                safe.add(s == null ? "" : s);
            }
        }
        Files.write(path, safe, StandardCharsets.UTF_8);
    }

    private ArrayList<String> readLines(Path path) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        if (!Files.exists(path)) {
            return result;
        }
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private void writeScores(Path path, HashMap<String, Integer> scores) throws IOException {
        Files.createDirectories(path.getParent());
        ArrayList<String> lines = new ArrayList<>();
        if (scores != null) {
            for (java.util.Map.Entry<String, Integer> e : scores.entrySet()) {
                String key = e.getKey();
                Integer val = e.getValue();
                if (key != null && val != null) {
                    lines.add(key + ":" + val.intValue());
                }
            }
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private HashMap<String, Integer> readScores(Path path) throws IOException {
        HashMap<String, Integer> map = new HashMap<>();
        if (!Files.exists(path)) {
            return map;
        }
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] parts = trimmed.split(":", 2);
            if (parts.length == 2) {
                try {
                    String key = parts[0].trim();
                    int val = Integer.parseInt(parts[1].trim());
                    map.put(key, Integer.valueOf(val));
                } catch (NumberFormatException e) {
                }
            }
        }
        return map;
    }

    private void validateIdNum(String idNum) throws ValidationException {
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
    }
}

