package main.controller;

import java.util.ArrayList;
import java.util.List;

import main.model.Person;
import main.model.Student;
import main.model.Professor;
import main.repository.UserRepository;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import main.exception.ReferentialIntegrityException; // import 추가

public class PersonController {

    private UserRepository getUserRepo() {
        return SystemController.getInstance().getUserRepository();
    }
    
    public void registerStudent(String name, String id, String pw) throws DuplicateException, ValidationException {
        Student newStudent = new Student(name, id, pw);
        getUserRepo().add(newStudent);
    }

    public void registerProfessor(String name, String id, String pw) throws DuplicateException, ValidationException {
        Professor newProfessor = new Professor(name, id, pw);
        getUserRepo().add(newProfessor);
    }
    
    public void updatePerson(String id, String newName, String newPw) throws NotFoundException, ValidationException {
        UserRepository repo = getUserRepo();
        
        if (!repo.existsById(id)) {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        }
        
        Person p = repo.getById(id);
        
        if (newName != null && !newName.isBlank()) {
            p.setName(newName);
        }
        
        if (newPw != null && !newPw.isBlank()) {
            p.setMyPassWd(newPw);
        }
        
        repo.update(p);
    }

    public void deletePerson(String id) throws NotFoundException, ValidationException, ReferentialIntegrityException {
        UserRepository repo = getUserRepo();
        repo.remove(id);
    }

    public String getPersonInfoById(String id) throws NotFoundException, ValidationException {
        Person p = getUserRepo().getById(id);
        if (p == null) {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        }
        return "이름: " + p.getName() + ", ID: " + p.getMyId() + ", 유형: " + p.getClass().getSimpleName();
    }

    public ArrayList<String> getAllUsersInfo() {
        ArrayList<String> infos = new ArrayList<>();
        List<Person> people = getUserRepo().getAll();
        
        if (people != null) {
            for (Person p : people) {
                String info = "이름: " + p.getName() + ", ID: " + p.getMyId() + " (" + p.getClass().getSimpleName() + ")";
                infos.add(info);
            }
        }
        return infos;
    }
}