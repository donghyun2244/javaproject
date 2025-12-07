package main.view; 

import java.util.ArrayList;
import java.util.Scanner;

import main.controller.PersonController;
import main.exception.DuplicateException;
import main.exception.NotFoundException;
import main.exception.ValidationException;

public class PersonView {

    private PersonController personController;
    private Scanner scanner;

    public PersonView(PersonController personController) {
        this.personController = personController;
        this.scanner = new Scanner(System.in);
    }

    public void registerStudentView() {
        System.out.println("\n=== [학생 등록] ===");

        System.out.print("이름: ");
        String name = scanner.nextLine();

        System.out.print("아이디: ");
        String id = scanner.nextLine();

        System.out.print("비밀번호: ");
        String pw = scanner.nextLine();

        try {
            personController.registerStudent(name, id, pw);
            System.out.println(">> 학생 등록 성공!");
        } catch (DuplicateException e) {
            System.out.println("[등록 실패] " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("[입력 오류] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[시스템 오류] 알 수 없는 오류가 발생했습니다.");
        }
    }

    public void registerProfessorView() {
        System.out.println("\n=== [교수 등록] ===");

        System.out.print("이름: ");
        String name = scanner.nextLine();

        System.out.print("아이디: ");
        String id = scanner.nextLine();

        System.out.print("비밀번호: ");
        String pw = scanner.nextLine();

        try {
            personController.registerProfessor(name, id, pw);
            System.out.println(">> 교수 등록 성공!");
        } catch (DuplicateException e) {
            System.out.println("[등록 실패] " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("[입력 오류] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[시스템 오류] 알 수 없는 오류가 발생했습니다.");
        }
    }

    public void updatePersonView() {
        System.out.println("\n=== [사용자 정보 수정] ===");

        System.out.print("수정할 사용자 ID: ");
        String id = scanner.nextLine();

        System.out.print("새 이름: ");
        String newName = scanner.nextLine();

        System.out.print("새 비밀번호: ");
        String newPw = scanner.nextLine();

        try {
            personController.updatePerson(id, newName, newPw);
            System.out.println(">> 정보 수정 성공!");
        } catch (NotFoundException e) {
            System.out.println("[수정 실패] " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("[입력 오류] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[시스템 오류] 알 수 없는 오류가 발생했습니다.");
        }
    }

    public void deletePersonView() {
        System.out.println("\n=== [사용자 삭제] ===");

        System.out.print("삭제할 사용자 ID: ");
        String id = scanner.nextLine();

        try {
            personController.deletePerson(id);
            System.out.println(">> 사용자 삭제 성공!");
        } catch (NotFoundException e) {
            System.out.println("[삭제 실패] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[시스템 오류] 알 수 없는 오류가 발생했습니다.");
        }
    }

    public void findPersonByIdView() {
        System.out.println("\n=== [ID로 사용자 조회] ===");

        System.out.print("조회할 사용자 ID: ");
        String id = scanner.nextLine();

        try {
            String info = personController.getPersonInfoById(id); 
            System.out.println(info);
        } catch (NotFoundException e) {
            System.out.println("[조회 실패] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[시스템 오류] 알 수 없는 오류가 발생했습니다.");
        }
    }

    public void showMyInfoView(String myId) {
        System.out.println("\n=== [내 정보] ===");
        try {

            String info = personController.getPersonInfoById(myId);
            System.out.println(info);
        } catch (Exception e) {
            System.out.println("정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void findAllUsersView() {
        System.out.println("\n=== [전체 사용자 목록] ===");

        try {
            ArrayList<String> infos = personController.getAllUsersInfo();

            if (infos == null || infos.isEmpty()) {
                System.out.println("등록된 사용자가 없습니다.");
                return;
            }

            for (String info : infos) {
                System.out.println(info);
            }
        } catch (Exception e) {
            System.out.println("[시스템 오류] 알 수 없는 오류가 발생했습니다.");
        }
    }
}
