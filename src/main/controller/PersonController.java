package main.controller;

import java.util.ArrayList;
import main.model.Person;
import main.model.Student;
import main.model.Professor;
import main.repository.UserRepository;
import main.repository.RepoFactory;

import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;

public class PersonController {

    private UserRepository userRepo;

    public PersonController() {
        // 팩토리 패턴으로 리포지토리 주입
        this.userRepo = RepoFactory.getUserRepository(RepoMode.FILE);
    }

    // 학생 등록
    public void registerStudent(String name, String id, String pw) throws DuplicateException {
        if (userRepo.findByMyId(id) != null) {
            throw new DuplicateException("이미 존재하는 ID입니다: " + id);
        }

        // Student 객체 생성 및 저장
        Student newStudent = new Student(name, id, pw);
        userRepo.add(newStudent);
        System.out.println("[알림] 학생 등록 완료: " + name);
    }

    // 교수 등록
    public void registerProfessor(String name, String id, String pw) throws DuplicateException {
        if (userRepo.findByMyId(id) != null) {
            throw new DuplicateException("이미 존재하는 ID입니다: " + id);
        }

        Professor newProf = new Professor(name, id, pw);
        userRepo.add(newProf);
        System.out.println("[알림] 교수 등록 완료: " + name);
    }

    // 사용자 정보 수정 (이름, 비밀번호)
    public void updatePerson(String id, String newName, String newPw) throws NotFoundException, ValidationException {
        Person person = userRepo.findByMyId(id);
        
        if (person == null) {
            throw new NotFoundException("수정할 사용자를 찾을 수 없습니다.");
        }

        // 비밀번호 유효성 검사 (AuthController와 동일 기준)
        if (newPw == null || newPw.length() < 4 || !newPw.matches("^[a-zA-Z0-9!@#$%]*$")) {
            throw new ValidationException("새 비밀번호 형식이 올바르지 않습니다.");
        }

        // 정보 수정 및 저장소 업데이트 호출
        person.setName(newName);
        person.setMyPassWd(newPw);
        
        userRepo.update(person); 
        System.out.println("[알림] 정보 수정 완료.");
    }

    // 사용자 삭제
    public void deletePerson(String id) throws NotFoundException {
        Person person = userRepo.findByMyId(id);
        
        if (person == null) {
            throw new NotFoundException("삭제할 사용자를 찾을 수 없습니다.");
        }

        userRepo.remove(id);
        System.out.println("[알림] 사용자 삭제 완료: " + id);
    }

    // ID로 사용자 찾기 (단건 조회)
    public Person findPersonById(String id) throws NotFoundException {
        Person person = userRepo.findByMyId(id);
        
        if (person == null) {
            throw new NotFoundException("해당 ID의 사용자가 없습니다: " + id);
        }
        
        return person;
    }

    // 전체 사용자 목록 조회
    public ArrayList<Person> findAllUsers() {
        return userRepo.getAll();
    }
}