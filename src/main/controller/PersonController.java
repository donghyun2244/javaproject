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
import main.exception.ReferentialIntegrityException;

public class PersonController {

    // Helper: 저장소 가져오기
    private UserRepository getUserRepo() {
        return SystemController.getInstance().getUserRepository();
    }

    // 1. 학생 등록
    public void registerStudent(String name, String id, String pw) throws DuplicateException, ValidationException {
        // 객체 생성 (생성자에서 유효성 검사 수행됨)
        Student newStudent = new Student(name, id, pw);
        
        // 저장소에 추가 (이미 존재하면 Repo에서 DuplicateException 발생)
        getUserRepo().add(newStudent);
    }

    // 2. 교수 등록
    public void registerProfessor(String name, String id, String pw) throws DuplicateException, ValidationException {
        Professor newProfessor = new Professor(name, id, pw);
        getUserRepo().add(newProfessor);
    }
    
    // 3. 사용자 정보 수정
    public void updatePerson(String id, String newName, String newPw) throws NotFoundException, ValidationException {
        UserRepository repo = getUserRepo();
        
        // 존재 여부 확인
        if (!repo.existsById(id)) {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        }
        
        // 비밀번호 변경 (Repo 기능 사용)
        if (newPw != null && !newPw.isBlank()) {
            repo.changePassword(id, newPw);
        }

        // 이름 변경
        // 주의: 현재 UserRepository 인터페이스에는 이름만 변경하여 저장하는 기능(update)이 없습니다.
        // 따라서 이름 변경은 메모리 상의 객체에만 반영되거나, DB/파일에 즉시 반영되지 않을 수 있습니다.
        Person p = repo.getById(id);
        if (newName != null && !newName.isBlank()) {
            p.setName(newName);
        }
    }

    // 4. 사용자 삭제
    public void deletePerson(String id) throws NotFoundException, ReferentialIntegrityException {
        UserRepository repo = getUserRepo();
        repo.remove(id);
    }

    // 5. 사용자 정보 조회 (ID로)
    public String getPersonInfoById(String id) throws NotFoundException, ValidationException {
        Person p = getUserRepo().getById(id);
        // Person.toString()이 구현되어 있다면 p.toString() 사용
        // 여기서는 안전하게 직접 구성
        return "이름: " + p.getName() + ", ID: " + p.getMyId() + ", 유형: " + p.getClass().getSimpleName();
    }

    // 6. 전체 사용자 목록 조회
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