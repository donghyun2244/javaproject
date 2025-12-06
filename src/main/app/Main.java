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

    // View 객체들
    private static AuthView authView;
    private static PersonView personView;
    private static SubjectView subjectView;
    private static ScoreView scoreView;

    public static void main(String[] args) {
        system.initSystem(); // 1. 초기화
        initViews();         // 2. 뷰 생성
        runMainLoop();       // 3. 실행
        system.saveAll();    // 4. 종료 시 저장
        scanner.close();
    }

    private static void initViews() {
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
                        // [수정] 객체 변수명(authView) 사용 및 메서드명(loginView) 수정
                        authView.loginView(); 
                        break;
                    case "2":
                        // [수정] 회원가입 메뉴 직접 구현 (AuthView에 registerMenu가 없으므로)
                        runRegisterMenu();
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
                System.out.println("2. 과목 관리");
                System.out.println("3. 성적 관리");
                System.out.println("9. 로그아웃");
                System.out.println("0. 종료");
                System.out.print("선택 >> ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        // PersonView에 내 정보 보기 기능이 있다면 호출 (현재 코드엔 없어서 주석)
                         personView.findPersonByIdView(); // 임시로 조회 기능 연결
                        break;
                    case "2":
                        runSubjectMenu(currentUser);
                        break;
                    case "3":
                        runScoreMenu(currentUser);
                        break;
                    case "9":
                        // [수정] 로그아웃 처리
                        authView.logoutView();
                        break;
                    case "0":
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }
            }
        }
    }

    // [추가] 회원가입 서브 메뉴
    private static void runRegisterMenu() {
        System.out.println("\n--- [회원가입] ---");
        System.out.println("1. 학생으로 가입");
        System.out.println("2. 교수로 가입");
        System.out.print("선택 >> ");
        String choice = scanner.nextLine();

        try {
            if (choice.equals("1")) {
                if (personView != null) {
                    personView.registerStudentView();
                } else {
                    System.out.println("회원가입 기능을 사용할 수 없습니다. 관리자에게 문의하세요.");
                }
            } else if (choice.equals("2")) {
                if (personView != null) {
                    personView.registerProfessorView();
                } else {
                    System.out.println("회원가입 기능을 사용할 수 없습니다. 관리자에게 문의하세요.");
                }
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        } catch (Exception e) {
            // 상세 오류 메시지 출력: "알 수 없는 오류" 대신 예외 메시지와 스택트레이스로 원인 파악 가능
            System.out.println("회원가입 중 오류가 발생했습니다: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
            e.printStackTrace();
        }
    }

    private static void runSubjectMenu(Person user) {
        while (true) {
            System.out.println("\n--- [과목 관리 메뉴] ---");
            if (user instanceof Professor || user instanceof Chancellor) {
                System.out.println("1. 과목 개설");
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

            // 권한 체크 없이 메뉴 호출 시 컨트롤러/뷰 내부에서 예외처리 되거나, 여기서 if문으로 막아야 함
            switch (choice) {
                case "1": subjectView.createSubjectView(); break;
                case "2": subjectView.updateSubjectView(); break;
                case "3": subjectView.deleteSubjectView(); break;
                case "4": subjectView.applySubjectView(); break;
                case "5": subjectView.printAllSubjects(); break;
                default: System.out.println("잘못된 입력입니다.");
            }
        }
    }

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
                case "1": scoreView.registerScoreView(); break;
                case "2": scoreView.updateScoreView(); break;
                case "3": scoreView.printSubjectStatisticsView(); break;
                case "4": scoreView.checkScholarshipView(); break;
                default: System.out.println("잘못된 입력입니다.");
            }
        }
    }
}