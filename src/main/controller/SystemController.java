package main.controller;

import main.model.Person;
import main.repository.UserRepository;
import main.repository.SubjectRepository;
import main.repository.RepoFactory;
import main.repository.RepoMode;

public class SystemController {

    private static SystemController instance;
    private Person currentUser;

    private UserRepository userRepo;
    private SubjectRepository subjectRepo;

    private AuthController authController;
    private PersonController personController;
    private SubjectController subjectController;
    private ScoreController scoreController;

    private SystemController() {
        this.currentUser = null;
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

    // [수정] loadData() 호출 삭제
    public void initSystem() {
        try {
            RepoMode mode = RepoMode.FILE;
            this.userRepo = RepoFactory.getUserRepository(mode);
            this.subjectRepo = RepoFactory.getSubjectRepository(mode);
            
            System.out.println("[시스템] 초기화 완료 (File Mode).");

        } catch (Exception e) {
            System.err.println("[오류] 시스템 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // [수정] saveData() 호출 삭제 (파일 모드는 즉시 저장되므로 불필요)
    public void saveAll() {
        System.out.println("[시스템] 종료 중... (데이터는 자동 저장되었습니다)");
    }

    public UserRepository getUserRepository() { return userRepo; }
    public SubjectRepository getSubjectRepository() { return subjectRepo; }

    public Person getCurrentUser() { return currentUser; }
    public void setCurrentUser(Person currentUser) { this.currentUser = currentUser; }

    public AuthController getAuthController() { return authController; }
    public PersonController getPersonController() { return personController; }
    public SubjectController getSubjectController() { return subjectController; }
    public ScoreController getScoreController() { return scoreController; }
}