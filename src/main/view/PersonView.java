package view;

import controller.PersonController;
import exception.DuplicateException;
import exception.NotFoundException;
import exception.ValidationException;
import model.Person;

import java.util.Scanner;

public class PersonView {

    private final PersonController personController;
    private final Scanner sc = new Scanner(System.in);

    public PersonView(PersonController personController) {
        this.personController = personController;
    }

    public void showMenu() {
        while (true) {
            System.out.println("==================================");
            System.out.println("          사용자 관리");
            System.out.println("==================================");
            System.out.println("1. 학생 등록");
            System.out.println("2. 교수 등록");
            System.out.println("3. 사용자 정보 수정");
            System.out.println("4. 사용자 정보 삭제");
            System.out.println("5. 사용자 정보 조회(ID)");
            System.out.println("0. 뒤로가기");
            System.out.print("메뉴 선택 >> ");

            String input = sc.nextLine();

            switch (input) {
                case "1": registerStudentView(); break;
                case "2": registerProfessorView(); break;
                case "3": updatePersonView(); break;
                case "4": deletePersonView(); break;
                case "5": searchPersonView(); break;
                case "0": return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // 1. 학생 등록
    private void registerStudentView() {
        System.out.println("------ 학생 등록 ------");
        System.out.print("이름 : ");
        String name = sc.nextLine();

        System.out.print("학번(ID) : ");
        String id = sc.nextLine();

        System.out.print("비밀번호 : ");
        String pw = sc.nextLine();

        try {
            personController.registerStudent(name, id, pw);
            System.out.println("학생 등록이 완료되었습니다.");
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
        }
    }

    // 2. 교수 등록
    private void registerProfessorView() {
        System.out.println("------ 교수 등록 ------");
        System.out.print("이름 : ");
        String name = sc.nextLine();

        System.out.print("교번(ID) : ");
        String id = sc.nextLine();

        System.out.print("비밀번호 : ");
        String pw = sc.nextLine();

        try {
            personController.registerProfessor(name, id, pw);
            System.out.println("교수 등록이 완료되었습니다.");
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
        }
    }

    // 3. 사용자 정보 수정
    private void updatePersonView() {
        System.out.println("------ 사용자 정보 수정 ------");
        System.out.print("수정할 사용자 ID : ");
        String id = sc.nextLine();

        System.out.print("새 이름 : ");
        String newName = sc.nextLine();

        System.out.print("새 비밀번호 : ");
        String newPw = sc.nextLine();

        try {
            personController.updatePerson(id, newName, newPw);
            System.out.println("사용자 정보가 수정되었습니다.");
        } catch (NotFoundException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 4. 사용자 삭제
    private void deletePersonView() {
        System.out.println("------ 사용자 정보 삭제 ------");
        System.out.print("삭제할 사용자 ID : ");
        String id = sc.nextLine();

        try {
            personController.deletePerson(id);
            System.out.println("사용자 정보가 삭제되었습니다.");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // 5. 사용자 조회 (ID로 조회)
    private void searchPersonView() {
        System.out.println("------ 사용자 정보 조회 (ID) ------");
        System.out.print("조회할 ID : ");
        String id = sc.nextLine();

        try {
            Person p = personController.findPersonById(id);

            System.out.println("\n[조회 결과]");
            System.out.println("이름 : " + p.getName());
            System.out.println("아이디 : " + p.getMyId());
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
