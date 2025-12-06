package main.controller;

import main.model.Subject;
import main.repository.SubjectRepository;
import main.exception.NotFoundException;
import main.exception.ValidationException;

public class ScoreController {

    private SubjectRepository getSubjectRepo() {
        return SystemController.getInstance().getSubjectRepository();
    }

    public void inputScore(String subjectCode, String studentId, int score) throws NotFoundException, ValidationException {
        if (score < 0 || score > 100) throw new ValidationException("점수는 0~100 사이여야 합니다.");
        
        SubjectRepository repo = getSubjectRepo();
        Subject sub = repo.findByIdNum(subjectCode);
        
        if (sub == null) throw new NotFoundException("과목을 찾을 수 없습니다.");
        if (!sub.getStudentsId().contains(studentId)) {
            throw new NotFoundException("해당 과목의 수강생이 아닙니다.");
        }

        // Subject 클래스에 setScore(studentId, score) 메서드가 있다고 가정
        // 혹은 sub.getScores().put(studentId, score); 와 같이 직접 접근
        sub.getScores().put(studentId, score); 
        repo.update(sub);
    }

    public void updateScore(String subjectCode, String studentId, int newScore) throws NotFoundException, ValidationException {
        inputScore(subjectCode, studentId, newScore); // 등록 로직과 동일
    }

    public double getSubjectAverage(String subjectCode) throws NotFoundException {
        Subject sub = null;
        try {
            sub = getSubjectRepo().findByIdNum(subjectCode);
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ValidationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (sub == null) throw new NotFoundException("과목을 찾을 수 없습니다.");
        
        // Subject 클래스 내 점수 저장 구조가 HashMap<String, Integer> scores 라고 가정
        if (sub.getScores().isEmpty()) return 0.0;
        
        int sum = 0;
        for (int s : sub.getScores().values()) {
            sum += s;
        }
        return (double) sum / sub.getScores().size();
    }

    public boolean checkScholarshipEligibility(String studentId) throws NotFoundException {
        // 전체 과목을 돌며 해당 학생의 평점 계산 로직 필요
        // (간단한 예시: 등록된 모든 과목 평균이 90 이상이면 장학금)
        int totalScore = 0;
        int count = 0;
        
        for (Subject sub : getSubjectRepo().getAll()) {
            if (sub.getScores().containsKey(studentId)) {
                totalScore += sub.getScores().get(studentId);
                count++;
            }
        }
        
        if (count == 0) throw new NotFoundException("수강 내역이 없습니다.");
        
        return (totalScore / (double) count) >= 90.0;
    }
}