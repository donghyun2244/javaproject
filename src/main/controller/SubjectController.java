package main.controller;

import java.util.ArrayList;
import main.model.Subject;
import main.repository.SubjectRepository;
import main.repository.UserRepository; 
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;
import main.exception.ReferentialIntegrityException;

public class SubjectController {

    private SubjectRepository getSubjectRepo() {
        return SystemController.getInstance().getSubjectRepository();
    }
    
    private UserRepository getUserRepo() {
        return SystemController.getInstance().getUserRepository();
    }

    // 과목 생성
    public void createSubject(String subName, String profId) 
        throws ValidationException, DuplicateException, NotFoundException {
        
        if (subName == null || subName.isBlank()) {
            throw new ValidationException("과목명을 입력해주세요.");
        }

        SubjectRepository subRepo = getSubjectRepo();
        int currentSize = subRepo.getAll().size();
        String newId = String.format("%04d", currentSize + 1); // 0001, 0002...

        if (profId != null && !profId.isBlank()) {
            if (!getUserRepo().existsById(profId)) { 
                throw new NotFoundException("해당 ID의 교수를 찾을 수 없습니다.");
            }
        }
        
        Subject sub = new Subject(newId, subName, profId); 
        
        subRepo.add(sub);
    }

    // 과목 수정
    public void updateSubject(String code, String newName, String newProfId) 
        throws NotFoundException, ValidationException {
        
        SubjectRepository subRepo = getSubjectRepo();
        Subject sub = subRepo.findByIdNum(code);

        if (sub == null) throw new NotFoundException("해당 과목을 찾을 수 없습니다.");

        if (newProfId != null && !newProfId.isBlank()) {
            if (!getUserRepo().existsById(newProfId)) {
                throw new NotFoundException("해당 ID의 교수를 찾을 수 없습니다.");
            }
            sub.setProfessorId(newProfId);
        }
        
        if (newName != null && !newName.isBlank()) {
            sub.setSubjectName(newName);
        }
        
        subRepo.update(sub);
    }

    // 과목 삭제
    public void deleteSubject(String code) 
        throws NotFoundException, ValidationException, ReferentialIntegrityException {
        
        SubjectRepository repo = getSubjectRepo();
        Subject sub = repo.findByIdNum(code);
        
        if (sub == null) throw new NotFoundException("해당 과목을 찾을 수 없습니다.");

        // 수강생이 있으면 삭제 불가
        if (sub.getStudentsId() != null && !sub.getStudentsId().isEmpty()) {
            throw new ReferentialIntegrityException("수강생이 있는 과목은 삭제할 수 없습니다.");
        }
        
        repo.remove(code);
    }

    // 수강 신청
    public void applySubject(String studentId, String subjectCode) 
        throws NotFoundException, ValidationException, DuplicateException {
        
        SubjectRepository subRepo = getSubjectRepo();
        
        if (!getUserRepo().existsById(studentId)) {
            throw new NotFoundException("해당 ID의 학생을 찾을 수 없습니다.");
        }

        Subject sub = subRepo.findByIdNum(subjectCode);
        if (sub == null) throw new NotFoundException("과목을 찾을 수 없습니다.");
        
        sub.addStudent(studentId); 
        
        subRepo.update(sub);
    }

    // 전체 과목 조회
    public ArrayList<Subject> getAllSubjects() {

        return (ArrayList<Subject>) getSubjectRepo().getAll();
    }
}