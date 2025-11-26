package main.controller;

import java.io.IOException;
import main.model.Person;
import main.repository.UserRepository;
import main.repository.SubjectRepository;
import main.repository.RepoFactory;
import main.repository.RepoMode;

public class SystemController {

    // [필드] 싱글톤 인스턴스
    private static SystemController instance;

    // [필드] 현재 로그인한 사용자 (로그인 전에는 null)
    private Person currentUser;

    // [필드] 데이터 저장소
    private UserRepository userRepo;
    private SubjectRepository subjectRepo;

    // [필드] 하위 컨트롤러들
    private AuthController authController;
    private PersonController personController;
    private SubjectController subjectController;
    private ScoreController scoreController;

    // [생성자] private으로 막아서 외부 생성 차단
    private SystemController() {
        this.currentUser = null;

        // 하위 컨트롤러 초기화
        this.authController = new AuthController();
        this.personController = new PersonController();
        this.subjectController = new SubjectController();
        this.scoreController = new ScoreController();
    }

    // [메서드] 싱글톤 인스턴스 반환
    public static synchronized SystemController getInstance() {
        if (instance == null) {
            instance = new SystemController();
        }
        return instance;
    }

    // [메서드] 시스템 초기화 (데이터 로드)
    public void initSystem() {
        try {
            // 1. 저장소 모드 설정 (FILE 모드) 및 리포지토리 주입
            RepoMode mode = RepoMode.FILE;
            this.userRepo = RepoFactory.getUserRepository(mode);
            this.subjectRepo = RepoFactory.getSubjectRepository(mode);

            // 2. 파일에서 데이터 로드
            System.out.println("[시스템] 데이터 로드 중...");
            this.userRepo.loadData();
            this.subjectRepo.loadData();
            
            System.out.println("[시스템] 초기화 완료.");

        } catch (Exception e) {
            System.err.println("[오류] 시스템 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // [메서드] 시스템 종료 시 데이터 저장
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

    // [Getter/Setter] 현재 로그인 사용자 관리
    public Person getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }

    // [Getter] 하위 컨트롤러 접근용
    public AuthController getAuthController() { return authController; }
    public PersonController getPersonController() { return personController; }
    public SubjectController getSubjectController() { return subjectController; }
    public ScoreController getScoreController() { return scoreController; }
}