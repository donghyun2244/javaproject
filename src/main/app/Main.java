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
        system.initSystem(); // 초기화
        initViews();         // 뷰 생성
        runMainLoop();       // 실행
        system.saveAll();    // 종료 시 저장
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

            //로그인 하기 전
            if (currentUser == null) {
                System.out.println("\n===== [학사 관리 시스템] =====");
                System.out.println("1. 로그인");
                System.out.println("2. 회원가입");
                System.out.println("0. 종료");
                System.out.print("선택 >> ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        authView.loginView(); 
                        break;
                    case "2":
                        runRegisterMenu();
                        break;
                    case "0":
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }

            } 
            //로그인 후
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
                        
                         personView.findPersonByIdView(); 
                        break;
                    case "2":
                        runSubjectMenu(currentUser);
                        break;
                    case "3":
                        runScoreMenu(currentUser);
                        break;
                    case "9":
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