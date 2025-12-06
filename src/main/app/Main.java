package main.app;

import java.util.Scanner;
import main.controller.SystemController;
import main.model.Person;
import main.model.Student;
import main.model.Professor;
import main.model.Chancellor;
import main.view.AuthView;
import main.view.PersonView;
import main.view.ScoreView;
import main.view.SubjectView;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static SystemController system = SystemController.getInstance();

    // View 객체들 (컨트롤러 주입)
    private static AuthView authView;
    private static PersonView personView;
    private static SubjectView subjectView;
    private static ScoreView scoreView;

    public static void main(String[] args) {
        // 1. 시스템 초기화 (데이터 로드)
        system.initSystem();

        // 2. View 인스턴스 생성 (SystemController에서 컨트롤러 가져와서 주입)
        initViews();

        // 3. 메인 루프 실행
        runMainLoop();

        // 4. 시스템 종료 시 저장
        system.saveAll();
        scanner.close();
    }

    private static void initViews() {
        // SystemController가 싱글톤이므로 이미 생성된 하위 컨트롤러들을 가져옴
        authView = new AuthView(system.getAuthController());
        personView = new PersonView(system.getPersonController());
        subjectView = new SubjectView(system.getSubjectController());
        scoreView = new ScoreView(system.getScoreController());
    }

    private static void runMainLoop() {
        while (true) {
            Person currentUser = system.getCurrentUser();

            // [상태 1] 로그인 전
            if (currentUser == null) {
                System.out.println("\n===== [학사 관리 시스템] =====");
                System.out.println("1. 로그인");
                System.out.println("2. 회원가입");
                System.out.println("0. 종료");
                System.out.print("선택 >> ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        // AuthView에 로그인 처리 메서드가 있다고 가정 (예: loginMenu)
                        // 만약 없다면 AuthController를 직접 호출하는 로직이 필요할 수 있음
                         AuthView.loginMenu(); 
                        break;
                    case "2":
                         AuthView.registerMenu();
                        break;
                    case "0":
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }

            } 
            // [상태 2] 로그인 후
            else {
                System.out.println("\n===== [" + currentUser.getName() + "님 환영합니다] =====");
                System.out.println("1. 내 정보 관리");
                System.out.println("2. 과목 관리 (수강신청/개설)");
                System.out.println("3. 성적 관리");
                System.out.println("9. 로그아웃");
                System.out.println("0. 종료");
                System.out.print("선택 >> ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        // 내 정보 보기/수정
                        // personView.showMyInfo(currentUser); 
                        break;
                    case "2":
                        runSubjectMenu(currentUser);
                        break;
                    case "3":
                        runScoreMenu(currentUser);
                        break;
                    case "9":
                        system.setCurrentUser(null);
                        System.out.println("로그아웃 되었습니다.");
                        break;
                    case "0":
                        System.out.println("프로그램을 종료합니다.");
                        return; // 메인 루프 종료 -> saveAll() 호출됨
                    default:
                        System.out.println("잘못된 입력입니다.");
                }
            }
        }
    }

    // === 서브 메뉴: 과목 관리 ===
    private static void runSubjectMenu(Person user) {
        while (true) {
            System.out.println("\n--- [과목 관리 메뉴] ---");
            // 역할별로 보여줄 메뉴를 다르게 구성
            if (user instanceof Professor || user instanceof Chancellor) {
                System.out.println("1. 과목 개설 (교수/담당자)");
                System.out.println("2. 과목 정보 수정");
                System.out.println("3. 과목 폐강");
            }
            if (user instanceof Student) {
                System.out.println("4. 수강 신청");
            }
            System.out.println("5. 전체 과목 조회");
            System.out.println("0. 뒤로 가기");
            System.out.print("선택 >> ");

            String choice = scanner.nextLine();
            if (choice.equals("0")) break;

            switch (choice) {
                case "1":
                    subjectView.createSubjectView();
                    break;
                case "2":
                    subjectView.updateSubjectView();
                    break;
                case "3":
                    subjectView.deleteSubjectView();
                    break;
                case "4":
                    subjectView.applySubjectView();
                    break;
                case "5":
                    subjectView.printAllSubjects();
                    break;
                default:
                    System.out.println("잘못된 입력이거나 권한이 없습니다.");
            }
        }
    }

    // === 서브 메뉴: 성적 관리 ===
    private static void runScoreMenu(Person user) {
        while (true) {
            System.out.println("\n--- [성적 관리 메뉴] ---");
            if (user instanceof Professor) {
                System.out.println("1. 성적 등록");
                System.out.println("2. 성적 수정");
            }
            System.out.println("3. 과목별 평균 조회");
            System.out.println("4. 장학금 대상 확인");
            System.out.println("0. 뒤로 가기");
            System.out.print("선택 >> ");

            String choice = scanner.nextLine();
            if (choice.equals("0")) break;

            switch (choice) {
                case "1":
                    scoreView.registerScoreView();
                    break;
                case "2":
                    scoreView.updateScoreView();
                    break;
                case "3":
                    scoreView.printSubjectStatisticsView();
                    break;
                case "4":
                    scoreView.checkScholarshipView();
                    break;
                default:
                    System.out.println("잘못된 입력이거나 권한이 없습니다.");
            }
        }
    }
}