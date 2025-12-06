package main.view;

import java.util.Scanner;
import main.controller.AuthController;
import main.model.Person;
import main.exception.ValidationException;
import main.exception.NotFoundException;
import main.exception.AuthenticationException;

public class AuthView {

    private AuthController authController;
    private Scanner scanner;

    public AuthView(AuthController authController) {
        this.authController = authController;
        this.scanner = new Scanner(System.in);
    }

    // 1. 로그인 View 설계 및 개발
    public void loginView() {
        System.out.println("\n=== [로그인] ===");

        // 이미 로그인 되어있는지 체크 (선택 사항, 사용자 편의)
        if (authController.getCurUser() != null) {
            System.out.println("이미 로그인 상태입니다: " + authController.getCurUser().getName());
            return;
        }

        System.out.print("아이디 입력: ");
        String id = scanner.nextLine();

        System.out.print("비밀번호 입력: ");
        String pw = scanner.nextLine();

        try {
            // AuthController.login 호출 (예외 발생 가능)
            authController.login(id, pw);
            System.out.println(">> 로그인 성공! 환영합니다, " + authController.getCurUser().getName() + "님.");
            
        } catch (ValidationException e) {
            // ID/PW 형식 오류 (빈 문자열, 4자리 미만 등)
            System.out.println("[입력 오류] " + e.getMessage());
        } catch (NotFoundException e) {
            // ID가 존재하지 않음
            System.out.println("[로그인 실패] 존재하지 않는 아이디입니다.");
        } catch (AuthenticationException e) {
            // 비밀번호 불일치 또는 이미 로그인된 상태
            System.out.println("[로그인 실패] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[시스템 오류] 알 수 없는 오류가 발생했습니다.");
        }
    }

    // 2. 로그아웃 View 설계 및 개발
    public void logoutView() {
        System.out.println("\n=== [로그아웃] ===");
        try {
            authController.logout();
            System.out.println(">> 로그아웃 되었습니다.");
        } catch (AuthenticationException e) {
            // 로그인 상태가 아닐 때 로그아웃 시도 시
            System.out.println("[오류] " + e.getMessage());
        }
    }

    // 3. 세션 관리 View 설계 및 개발 (현재 접속자 정보)
    public void sessionView() {
        System.out.println("\n=== [현재 접속 정보] ===");
        Person currentUser = authController.getCurUser();

        if (currentUser != null) {
            System.out.println("이름: " + currentUser.getName());
            System.out.println("아이디: " + currentUser.getMyId());
            // 필요하다면 역할(학생/교수 등)도 출력 가능 (instanceof 활용)
        } else {
            System.out.println("현재 로그인된 사용자가 없습니다.");
        }
    }
}