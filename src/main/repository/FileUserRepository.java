package main.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ReferentialIntegrityException;
import main.exception.ValidationException;
import model.Chancellor;
import model.Person;
import model.Professor;
import model.Student;

public class FileUserRepository implements UserRepository {
    private static final String DATA_ROOT = "Data";
    private static final String PERSON_ROOT = "Person";
    private static final String STUDENT_DIR = "Student";
    private static final String PROFESSOR_DIR = "Professor";
    private static final String CHANCELLOR_DIR = "Chancellor";

    private static UserRepository instance;

    private FileUserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new FileUserRepository();
        }
        return instance;
    }

    @Override
    public void add(Person p) throws DuplicateException, ValidationException {
        if (p == null) {
            throw new ValidationException("Person cannot be null");
        }
        String myId = p.getMyId();
        validateMyId(myId);
        if (checkExistByMyId(myId)) {
            throw new DuplicateException("Duplicate ID: " + myId);
        }
        Path dir = buildPersonDir(p);
        try {
            Files.createDirectories(dir);
            writeString(dir.resolve("name.txt"), p.getName());
            writeString(dir.resolve("myId.txt"), p.getMyId());
            writeString(dir.resolve("myPassWd.txt"), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkExistByMyId(String myId) throws ValidationException {
        validateMyId(myId);
        Path dir = findPersonDirById(myId);
        return dir != null && Files.exists(dir);
    }

    @Override
    public Person findByMyId(String myId) throws NotFoundException, ValidationException {
        validateMyId(myId);
        Path dir = findPersonDirById(myId);
        if (dir == null || !Files.exists(dir)) {
            throw new NotFoundException("User not found: " + myId);
        }
        try {
            String name = readString(dir.resolve("name.txt"));
            String id = readString(dir.resolve("myId.txt"));
            String pass = readString(dir.resolve("myPassWd.txt"));
            if (dir.toString().contains(STUDENT_DIR)) {
                return new Student(name, id, pass);
            }
            if (dir.toString().contains(PROFESSOR_DIR)) {
                return new Professor(name, id, pass);
            }
            if (dir.toString().contains(CHANCELLOR_DIR)) {
                return new Chancellor(name, id, pass);
            }
            throw new NotFoundException("Unknown person type for: " + myId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Person> getAll() {
        ArrayList<Person> result = new ArrayList<>();
        Path base = Paths.get(DATA_ROOT, PERSON_ROOT);
        addAllFromTypeDir(result, base.resolve(STUDENT_DIR), STUDENT_DIR);
        addAllFromTypeDir(result, base.resolve(PROFESSOR_DIR), PROFESSOR_DIR);
        addAllFromTypeDir(result, base.resolve(CHANCELLOR_DIR), CHANCELLOR_DIR);
        return result;
    }

    @Override
    public void update(Person p) throws NotFoundException, ValidationException {
        if (p == null) {
            throw new ValidationException("Person cannot be null");
        }
        String myId = p.getMyId();
        validateMyId(myId);
        Path dir = findPersonDirById(myId);
        if (dir == null || !Files.exists(dir)) {
            throw new NotFoundException("User not found: " + myId);
        }
        try {
            writeString(dir.resolve("name.txt"), p.getName());
            writeString(dir.resolve("myId.txt"), p.getMyId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String myId) throws NotFoundException, ReferentialIntegrityException {
        validateMyId(myId);
        Path dir = findPersonDirById(myId);
        if (dir == null || !Files.exists(dir)) {
            throw new NotFoundException("User not found: " + myId);
        }
        try {
            Files.deleteIfExists(dir.resolve("name.txt"));
            Files.deleteIfExists(dir.resolve("myId.txt"));
            Files.deleteIfExists(dir.resolve("myPassWd.txt"));
            Files.deleteIfExists(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path buildPersonDir(Person p) {
        if (p instanceof Student) {
            return Paths.get(DATA_ROOT, PERSON_ROOT, STUDENT_DIR, p.getMyId());
        }
        if (p instanceof Professor) {
            return Paths.get(DATA_ROOT, PERSON_ROOT, PROFESSOR_DIR, p.getMyId());
        }
        if (p instanceof Chancellor) {
            return Paths.get(DATA_ROOT, PERSON_ROOT, CHANCELLOR_DIR, p.getMyId());
        }
        return Paths.get(DATA_ROOT, PERSON_ROOT, "Unknown", p.getMyId());
    }

    private Path findPersonDirById(String myId) {
        Path base = Paths.get(DATA_ROOT, PERSON_ROOT);
        Path studentDir = base.resolve(STUDENT_DIR).resolve(myId);
        if (Files.exists(studentDir)) {
            return studentDir;
        }
        Path professorDir = base.resolve(PROFESSOR_DIR).resolve(myId);
        if (Files.exists(professorDir)) {
            return professorDir;
        }
        Path chancellorDir = base.resolve(CHANCELLOR_DIR);
        if (Files.exists(chancellorDir)) {
            return chancellorDir;
        }
        return null;
    }

    private void addAllFromTypeDir(ArrayList<Person> list, Path typeDir, String type) {
        if (!Files.exists(typeDir)) {
            return;
        }
        try {
            Files.list(typeDir).filter(Files::isDirectory).forEach(dir -> {
                try {
                    String name = readString(dir.resolve("name.txt"));
                    String id = readString(dir.resolve("myId.txt"));
                    String pass = readString(dir.resolve("myPassWd.txt"));
                    Person p;
                    if (STUDENT_DIR.equals(type)) {
                        p = new Student(name, id, pass);
                    } else if (PROFESSOR_DIR.equals(type)) {
                        p = new Professor(name, id, pass);
                    } else if (CHANCELLOR_DIR.equals(type)) {
                        p = new Chancellor(name, id, pass);
                    } else {
                        p = null;
                    }
                    if (p != null) {
                        list.add(p);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeString(Path path, String value) throws IOException {
        Files.write(path, value == null ? new byte[0] : value.getBytes(StandardCharsets.UTF_8));
    }

    private String readString(Path path) throws IOException {
        if (!Files.exists(path)) {
            return "";
        }
        return Files.readString(path, StandardCharsets.UTF_8).trim();
    }

    private void validateMyId(String myId) throws ValidationException {
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
    }
}

