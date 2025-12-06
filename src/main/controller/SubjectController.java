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

    // 1. 과목 생성
    public void createSubject(String subName, String profId) 
        throws ValidationException, DuplicateException, NotFoundException {
        
        if (subName == null || subName.isBlank()) {
            throw new ValidationException("과목명을 입력해주세요.");
        }
        
        // [수정된 로직 시작]
        
        // 1. 유일한 4자리 과목 ID 생성
        SubjectRepository subRepo = getSubjectRepo();
        // 현재 등록된 과목의 개수를 기반으로 다음 ID를 생성합니다. (e.g., 0001, 0002...)
        int currentSize = subRepo.getAll().size();
        String newId = String.format("%04d", currentSize + 1); 

        // 2. 담당 교수가 존재하는지 확인
        if (profId != null && !profId.isBlank()) {
            if (!getUserRepo().existsById(profId)) { 
                throw new NotFoundException("해당 ID의 교수를 찾을 수 없습니다.");
            }
        }
        
        // 3. [수정] 3개의 인자를 가진 생성자를 호출 (ID, Name, ProfessorID)
        Subject sub = new Subject(newId, subName, profId); 
        
        // [수정된 로직 끝]
        
        subRepo.add(sub);
    }

    // 2. 과목 수정
    public void updateSubject(String code, String newName, String newProfId) 
        throws NotFoundException, ValidationException {
        
        SubjectRepository subRepo = getSubjectRepo();
        
        Subject sub = subRepo.findByIdNum(code);

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

    // 3. 과목 삭제
    public void deleteSubject(String code) 
        throws NotFoundException, ValidationException, ReferentialIntegrityException {
        
        SubjectRepository repo = getSubjectRepo();
        Subject sub = repo.findByIdNum(code);
        
        // Subject 모델의 getStudentsId()를 복사본으로 사용하여 무결성 검사
        if (sub.getStudentsId() != null && !sub.getStudentsId().isEmpty()) {
            throw new ReferentialIntegrityException("수강생이 있는 과목은 삭제할 수 없습니다.");
        }
        
        repo.remove(code);
    }

    // 4. 수강 신청
    public void applySubject(String studentId, String subjectCode) 
        throws NotFoundException, ValidationException, DuplicateException {
        
        SubjectRepository subRepo = getSubjectRepo();
        
        if (!getUserRepo().existsById(studentId)) {
            throw new NotFoundException("해당 ID의 학생을 찾을 수 없습니다.");
        }

        Subject sub = subRepo.findByIdNum(subjectCode);
        
        // Subject 모델의 addStudent 메서드를 사용하여 중복 및 유효성 검사 위임
        // Subject 클래스 내에서 중복 체크를 하기 때문에 코드가 간결해집니다.
        sub.addStudent(studentId); 
        
        subRepo.update(sub);
    }

    // 5. 전체 과목 조회
    public ArrayList<Subject> getAllSubjects() {
        return (ArrayList<Subject>) getSubjectRepo().getAll();
    }
}