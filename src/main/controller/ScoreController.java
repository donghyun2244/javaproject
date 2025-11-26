package main.controller;

import java.util.ArrayList;
import main.model.Subject;
import main.repository.SubjectRepository;
import main.repository.RepoFactory;
import main.repository.RepoMode;
import main.exception.NotFoundException;
import main.exception.ValidationException;

public class ScoreController {

    // 성적은 과목(Subject) 객체 내부에 종속되므로 SubjectRepository만 있어도 충분함
    private SubjectRepository subjectRepo;

    public ScoreController() {
        this.subjectRepo = RepoFactory.getSubjectRepository(RepoMode.FILE);
    }

    // 성적 입력 및 수정 (HashMap의 특성상 put 시 덮어쓰기 됨)
    public void updateScore(String subjectCode, String studentId, int newScore) throws NotFoundException, ValidationException {
        Subject subject = subjectRepo.findByCode(subjectCode);
        
        if (subject == null) {
            throw new NotFoundException("성적을 입력할 과목을 찾을 수 없습니다.");
        }

        // 해당 학생이 수강생 명단에 있는지 확인 (수강 안 한 학생에게 점수 부여 불가)
        if (!subject.getStudentsId().contains(studentId)) {
            throw new NotFoundException("해당 과목의 수강생이 아닙니다: " + studentId);
        }

        // 점수 범위 유효성 검사
        if (newScore < 0 || newScore > 100) {
            throw new ValidationException("점수는 0점에서 100점 사이여야 합니다.");
        }

        // 모델의 setScore 호출 (내부 HashMap에 저장)
        subject.setScore(studentId, newScore);
        
        // 변경 사항 저장
        subjectRepo.update(subject);
        System.out.println("[알림] 성적 반영 완료.");
    }

    // 개인 점수 조회
    public int getScore(String subjectCode, String studentId) throws NotFoundException {
        Subject subject = subjectRepo.findByCode(subjectCode);

        if (subject == null) {
            throw new NotFoundException("과목을 찾을 수 없습니다.");
        }

        // 점수가 등록되어 있는지 확인 (null 체크)
        Integer score = subject.getScores().get(studentId);
        if (score == null) {
            throw new NotFoundException("아직 성적이 등록되지 않았습니다.");
        }

        return score;
    }

    // 과목 평균 점수 계산
    public double getSubjectAverage(String subjectCode) throws NotFoundException {
        Subject subject = subjectRepo.findByCode(subjectCode);

        if (subject == null) {
            throw new NotFoundException("과목을 찾을 수 없습니다.");
        }

        // HashMap의 values()를 이용하여 평균 계산
        int sum = 0;
        int count = subject.getScores().size();

        if (count == 0) {
            return 0.0; // 수강생(또는 점수 등록된 학생)이 0명이면 0점 반환
        }

        for (int score : subject.getScores().values()) {
            sum += score;
        }

        return (double) sum / count;
    }

    // 장학금 대상 여부 판별 (전체 과목 평점 90점 이상)
    public boolean checkScholarshipEligibility(String studentId) throws NotFoundException {
        ArrayList<Subject> allSubjects = subjectRepo.getAll();
        int totalScore = 0;
        int subjectCount = 0;

        // 모든 과목을 순회하며 내 점수 찾기
        for (Subject sub : allSubjects) {
            Integer score = sub.getScores().get(studentId);
            if (score != null) {
                totalScore += score;
                subjectCount++;
            }
        }

        if (subjectCount == 0) {
            throw new NotFoundException("수강 내역이나 성적 정보가 없습니다.");
        }

        double gpa = (double) totalScore / subjectCount;
        
        // 기준: 평균 90점 이상
        return gpa >= 90.0;
    }
}