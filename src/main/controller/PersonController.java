package main.controller;

import java.util.ArrayList;
import main.model.Person;
import main.model.Student;
import main.model.Professor;
import main.repository.UserRepository;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;

public class PersonController {

    private UserRepository getUserRepo() {
        return SystemController.getInstance().getUserRepository();
    }

    public void registerStudent(String name, String id, String pw) throws DuplicateException, ValidationException {
        UserRepository repo = getUserRepo();
        // 중복 체크
        if (repo.findByMyId(id) != null) {
            throw new DuplicateException("이미 존재하는 아이디입니다.");
        }
        // 학생 생성 및 추가
        Student newStudent = new Student(name, id, pw);
        repo.add(newStudent); // Repository의 add 메서드 사용
    }

    public void registerProfessor(String name, String id, String pw) throws DuplicateException, ValidationException {
        UserRepository repo = getUserRepo();
        if (repo.findByMyId(id) != null) {
            throw new DuplicateException("이미 존재하는 아이디입니다.");
        }
        // 교수 생성 및 추가
        Professor newProfessor = new Professor(name, id, pw);
        repo.add(newProfessor);
    }
    
    // 정보 수정, 삭제, 조회 등 추가 구현
    public void updatePerson(String id, String newName, String newPw) throws NotFoundException, ValidationException {
        UserRepository repo = getUserRepo();
        Person p = repo.findByMyId(id);
        if (p == null) throw new NotFoundException("사용자를 찾을 수 없습니다.");
        
        p.setName(newName);
        p.setMyPassWd(newPw);
        repo.update(p); // Repository에 update 메서드가 있다면 호출
    }

    public void deletePerson(String id) throws NotFoundException {
        UserRepository repo = getUserRepo();
        Person p = repo.findByMyId(id);
        if (p == null) throw new NotFoundException("사용자를 찾을 수 없습니다.");
        
        repo.remove(id); // Repository에 remove 메서드가 있다면 호출
    }

    public String getPersonInfoById(String id) throws NotFoundException {
        Person p = getUserRepo().findByMyId(id);
        if (p == null) throw new NotFoundException("사용자를 찾을 수 없습니다.");
        return p.toString();
    }

    public ArrayList<String> getAllUsersInfo() {
        ArrayList<String> infos = new ArrayList<>();
        // getAll() 메서드가 있다고 가정
        for (Person p : getUserRepo().getAll()) {
            infos.add(p.toString());
        }
        return infos;
    }
}