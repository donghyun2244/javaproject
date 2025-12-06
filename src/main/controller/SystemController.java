package main.controller;

import java.io.IOException;
import main.model.Person;
import main.repository.UserRepository;
import main.repository.SubjectRepository;
import main.repository.RepoFactory;
import main.repository.RepoMode;

public class SystemController {

    private static SystemController instance;
    private Person currentUser;
    
    // 데이터 저장소
    private UserRepository userRepo;
    private SubjectRepository subjectRepo;

    // 하위 컨트롤러
    private AuthController authController;
    private PersonController personController;
    private SubjectController subjectController;
    private ScoreController scoreController;

    private SystemController() {
        this.currentUser = null;
        // 컨트롤러 초기화
        this.authController = new AuthController();
        this.personController = new PersonController();
        this.subjectController = new SubjectController();
        this.scoreController = new ScoreController();
    }

    public static synchronized SystemController getInstance() {
        if (instance == null) {
            instance = new SystemController();
        }
        return instance;
    }

    public void initSystem() {
        try {
            // [설정] 파일 모드로 저장소 생성
            RepoMode mode = RepoMode.FILE;
            this.userRepo = RepoFactory.getUserRepository(mode);
            this.subjectRepo = RepoFactory.getSubjectRepository(mode);

            System.out.println("[시스템] 데이터 로드 중...");
            this.userRepo.loadData();
            this.subjectRepo.loadData();
            System.out.println("[시스템] 초기화 완료.");

        } catch (Exception e) {
            System.err.println("[오류] 시스템 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveAll() {
        try {
            System.out.println("[시스템] 데이터 저장 중...");
            this.userRepo.saveData();
            this.subjectRepo.saveData();
            System.out.println("[시스템] 저장 완료.");
        } catch (IOException e) {
            System.err.println("[오류] 데이터 저장 실패: " + e.getMessage());
        }
    }

    // [중요] 하위 컨트롤러들이 저장소에 접근할 수 있게 Getter 추가
    public UserRepository getUserRepository() { return userRepo; }
    public SubjectRepository getSubjectRepository() { return subjectRepo; }

    public Person getCurrentUser() { return currentUser; }
    public void setCurrentUser(Person currentUser) { this.currentUser = currentUser; }

    public AuthController getAuthController() { return authController; }
    public PersonController getPersonController() { return personController; }
    public SubjectController getSubjectController() { return subjectController; }
    public ScoreController getScoreController() { return scoreController; }
}