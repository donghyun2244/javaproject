package main.controller;

import java.util.ArrayList;
import main.repository.UserRepository;
import main.model.Person;
import main.model.Professor;
import main.model.Subject;
import main.repository.SubjectRepository;
import main.exception.NotFoundException;
import main.exception.PermissionException;
import main.exception.ValidationException;


public class ScoreController {

    private SubjectRepository getSubjectRepo() {
        return SystemController.getInstance().getSubjectRepository();
    }

    private void checkPermission(Subject sub) throws PermissionException {
        Person currentUser = SystemController.getInstance().getCurrentUser();
        
        if (currentUser instanceof Professor) {
            if (!sub.getProfessorId().equals(currentUser.getMyId())) {
                throw new PermissionException("본인의 과목 성적만 관리할 수 있습니다.");
            }
        }
    }

    public void inputScore(String subjectCode, String studentId, int score) throws NotFoundException, ValidationException, PermissionException {
        if (score < 0 || score > 100) throw new ValidationException("점수는 0~100 사이여야 합니다.");
        
        SubjectRepository repo = getSubjectRepo();
        Subject sub = repo.findByIdNum(subjectCode);
        
        if (sub == null) throw new NotFoundException("과목을 찾을 수 없습니다.");
        
        checkPermission(sub);

        if (!sub.getStudentsId().contains(studentId)) {
            throw new NotFoundException("해당 과목의 수강생이 아닙니다.");
        }

        sub.getScores().put(studentId, score); 
        repo.update(sub);
    }

    public void updateScore(String subjectCode, String studentId, int newScore) throws NotFoundException, ValidationException, PermissionException {
        inputScore(subjectCode, studentId, newScore); 
    }

    public double getSubjectAverage(String subjectCode) throws NotFoundException {
        Subject sub = null;
        try {
            sub = getSubjectRepo().findByIdNum(subjectCode);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        if (sub == null) throw new NotFoundException("과목을 찾을 수 없습니다.");
        
        if (sub.getScores().isEmpty()) return 0.0;
        
        int sum = 0;
        for (int s : sub.getScores().values()) {
            sum += s;
        }
        return (double) sum / sub.getScores().size();
    }

    public boolean checkScholarshipEligibility(String studentId) throws NotFoundException {

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

    private UserRepository getUserRepo() {
        return SystemController.getInstance().getUserRepository();
    }


    public ArrayList<String> getEnrolledStudentsInfo(String subjectCode) 
        throws NotFoundException, PermissionException, ValidationException {
        
        Subject sub = getSubjectRepo().findByIdNum(subjectCode);
        if (sub == null) throw new NotFoundException("과목을 찾을 수 없습니다.");
        
 
        checkPermission(sub);

        ArrayList<String> infoList = new ArrayList<>();
        UserRepository userRepo = getUserRepo();
        
        for (String stId : sub.getStudentsId()) {
            StringBuilder info = new StringBuilder();
            
            try {
                Person p = userRepo.getById(stId);
                info.append(p.getName()).append(" (").append(stId).append(")");
            } catch (Exception e) {
                info.append("정보 없음 (").append(stId).append(")");
            }

            try {
 
                Integer score = sub.getScore(stId); 
                info.append(" - 현재 점수: ").append(score).append("점");
            } catch (Exception e) {
 
                info.append(" - 현재 점수: 미등록");
            }
            
            infoList.add(info.toString());
        }
        return infoList;
    }
}