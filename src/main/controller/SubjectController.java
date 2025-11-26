package main.controller;

import java.util.ArrayList;
import main.model.Subject;
import main.model.Person;
import main.repository.SubjectRepository;
import main.repository.UserRepository;
import main.repository.RepoFactory;
import main.repository.RepoMode;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ReferentialIntegrityException;
import main.exception.ValidationException;

public class SubjectController {

    private SubjectRepository subjectRepo;
    private UserRepository userRepo; // 교수/학생 존재 여부 확인용

    public SubjectController() {
        // 팩토리 패턴으로 리포지토리 초기화
        RepoMode mode = RepoMode.FILE;
        this.subjectRepo = RepoFactory.getSubjectRepository(mode);
        this.userRepo = RepoFactory.getUserRepository(mode);
    }

    // 과목 개설
    public void createSubject(String subName, String profId) throws ValidationException, NotFoundException {
        if (subName == null || subName.trim().isEmpty()) {
            throw new ValidationException("과목명이 비어있습니다.");
        }
        
        // 담당 교수가 실제로 존재하는지 확인
        if (userRepo.findByMyId(profId) == null) {
            throw new NotFoundException("해당 ID의 교수가 존재하지 않습니다: " + profId);
        }

        // 과목 생성 및 저장 (과목 코드는 모델 내부 혹은 리포지토리에서 처리한다고 가정)
        Subject newSubject = new Subject(subName, profId);
        subjectRepo.addSubject(newSubject);
        System.out.println("[알림] 과목 개설 완료: " + subName);
    }

    // 과목 정보 수정
    public void updateSubject(String subjectCode, String newName, String newProfId) throws NotFoundException, ValidationException {
        Subject subject = subjectRepo.findByCode(subjectCode);
        
        if (subject == null) {
            throw new NotFoundException("수정할 과목을 찾을 수 없습니다.");
        }
        
        // 교체할 교수가 존재하는지 확인
        if (userRepo.findByMyId(newProfId) == null) {
            throw new NotFoundException("변경할 교수 ID가 존재하지 않습니다.");
        }

        subject.setSubjectName(newName);
        subject.setProfessorId(newProfId);
        
        subjectRepo.update(subject);
        System.out.println("[알림] 과목 정보 수정 완료.");
    }

    // 과목 폐강 (삭제) - 참조 무결성 검사 포함
    public void deleteSubject(String subjectCode) throws NotFoundException, ReferentialIntegrityException {
        Subject subject = subjectRepo.findByCode(subjectCode);
        
        if (subject == null) {
            throw new NotFoundException("삭제할 과목이 없습니다.");
        }

        // [중요] 수강생이 한 명이라도 있으면 삭제 불가
        if (!subject.getStudentsId().isEmpty()) {
            throw new ReferentialIntegrityException("수강생이 등록된 과목은 삭제할 수 없습니다.");
        }

        subjectRepo.remove(subjectCode);
        System.out.println("[알림] 과목 폐강 완료: " + subjectCode);
    }

    // 수강 신청
    public void applySubject(String studentId, String subjectCode) throws NotFoundException, DuplicateException {
        // 1. 학생 존재 확인
        if (userRepo.findByMyId(studentId) == null) {
            throw new NotFoundException("학생 정보를 찾을 수 없습니다.");
        }

        // 2. 과목 존재 확인
        Subject subject = subjectRepo.findByCode(subjectCode);
        if (subject == null) {
            throw new NotFoundException("신청할 과목이 존재하지 않습니다.");
        }

        // 3. 중복 수강 확인
        if (subject.getStudentsId().contains(studentId)) {
            throw new DuplicateException("이미 수강 신청한 과목입니다.");
        }

        // 4. 수강생 추가
        subject.addStudent(studentId);
        subjectRepo.update(subject); // 변경 사항 저장
        System.out.println("[알림] 수강 신청 완료.");
    }

    // 수강 취소
    public void cancelSubject(String studentId, String subjectCode) throws NotFoundException {
        Subject subject = subjectRepo.findByCode(subjectCode);
        
        if (subject == null) {
            throw new NotFoundException("과목을 찾을 수 없습니다.");
        }

        if (!subject.getStudentsId().contains(studentId)) {
            throw new NotFoundException("해당 과목을 수강하고 있지 않습니다.");
        }

        subject.removeStudent(studentId);
        subjectRepo.update(subject);
        System.out.println("[알림] 수강 취소 완료.");
    }

    // 전체 과목 목록 조회
    public ArrayList<Subject> getAllSubjects() {
        return subjectRepo.getAll();
    }
}