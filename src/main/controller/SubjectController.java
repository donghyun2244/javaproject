package main.controller;

import java.util.ArrayList;
import main.model.Subject;
import main.repository.SubjectRepository;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import main.exception.ReferentialIntegrityException;

public class SubjectController {

    private SubjectRepository getSubjectRepo() {
        return SystemController.getInstance().getSubjectRepository();
    }

    public void createSubject(String subName, String profId) throws ValidationException {
        // 교수 ID 유효성 검사 등 필요하다면 UserRepository 조회 로직 추가 가능
        if (subName == null || subName.isBlank()) throw new ValidationException("과목명을 입력해주세요.");
        
        Subject sub = new Subject(subName, profId);
        getSubjectRepo().add(sub);
    }

    public void updateSubject(String code, String newName, String newProfId) throws NotFoundException {
        SubjectRepository repo = getSubjectRepo();
        Subject sub = repo.findByIdNum(code); // 코드로 찾기
        if (sub == null) throw new NotFoundException("해당 과목을 찾을 수 없습니다.");
        
        sub.setSubjectName(newName);
        sub.setProfessorId(newProfId);
        repo.update(sub);
    }

    public void deleteSubject(String code) throws NotFoundException, ReferentialIntegrityException {
        SubjectRepository repo = getSubjectRepo();
        Subject sub = repo.findByIdNum(code);
        
        if (sub == null) throw new NotFoundException("해당 과목을 찾을 수 없습니다.");
        // 수강생이 있는지 확인 (getStudentsId() 메서드가 있다고 가정)
        if (!sub.getStudentsId().isEmpty()) {
            throw new ReferentialIntegrityException("수강생이 있는 과목은 삭제할 수 없습니다.");
        }
        repo.remove(code);
    }

    public void applySubject(String studentId, String subjectCode) throws NotFoundException, DuplicateException {
        SubjectRepository repo = getSubjectRepo();
        Subject sub = repo.findByIdNum(subjectCode);
        
        if (sub == null) throw new NotFoundException("과목을 찾을 수 없습니다.");
        
        // 이미 수강 중인지 확인
        if (sub.getStudentsId().contains(studentId)) {
            throw new DuplicateException("이미 수강 신청한 과목입니다.");
        }
        
        sub.getStudentsId().add(studentId);
        repo.update(sub);
    }

    public ArrayList<Subject> getAllSubjects() {
        return getSubjectRepo().getAll();
    }
}