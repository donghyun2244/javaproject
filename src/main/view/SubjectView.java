package main.view; 

import java.util.Scanner;
import java.util.ArrayList;
import main.controller.SubjectController;
import main.model.Subject;
import main.exception.ValidationException;
import main.exception.NotFoundException;
import main.exception.DuplicateException;
import main.exception.ReferentialIntegrityException;

public class SubjectView {

    private SubjectController subjectController;
    private Scanner scanner;

    public SubjectView(SubjectController subjectController) {
        this.subjectController = subjectController;
        this.scanner = new Scanner(System.in);
    }

    public void createSubjectView() {
        System.out.println("\n[과목 생성]");
        System.out.print("과목명 입력: ");
        String subName = scanner.nextLine();
        System.out.print("담당 교수 ID 입력: ");
        String profId = scanner.nextLine();

        try {
            subjectController.createSubject(subName, profId);
            System.out.println(">> 과목이 성공적으로 개설되었습니다.");
        } catch (ValidationException e) {
            System.out.println(">> 입력 오류: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }

    public void updateSubjectView() {
        System.out.println("\n[과목 정보 수정]");
        System.out.print("수정할 과목 코드 입력: ");
        String subjectCode = scanner.nextLine();
        System.out.print("새로운 과목명 입력: ");
        String newName = scanner.nextLine();
        System.out.print("새로운 담당 교수 ID 입력: ");
        String newProfId = scanner.nextLine();

        try {
            subjectController.updateSubject(subjectCode, newName, newProfId);
            System.out.println(">> 과목 정보가 수정되었습니다.");
        } catch (NotFoundException e) {
            System.out.println(">> 대상 없음: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }

    public void deleteSubjectView() {
        System.out.println("\n[과목 폐강(삭제)]");
        System.out.print("삭제할 과목 코드 입력: ");
        String subjectCode = scanner.nextLine();

        try {
            subjectController.deleteSubject(subjectCode);
            System.out.println(">> 과목이 삭제되었습니다.");
        } catch (NotFoundException e) {
            System.out.println(">> 대상 없음: " + e.getMessage());
        } catch (ReferentialIntegrityException e) {
            System.out.println(">> 삭제 불가: 수강생이 있는 과목은 삭제할 수 없습니다.");
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }

    public void applySubjectView() {
        System.out.println("\n[수강 신청]");
        System.out.print("학생 ID 입력: ");
        String studentId = scanner.nextLine();
        System.out.print("신청할 과목 코드 입력: ");
        String subjectCode = scanner.nextLine();

        try {
            subjectController.applySubject(studentId, subjectCode);
            System.out.println(">> 수강 신청이 완료되었습니다.");
        } catch (NotFoundException e) {
            System.out.println(">> 신청 실패: 학생 또는 과목 정보가 없습니다.");
        } catch (DuplicateException e) {
            System.out.println(">> 신청 실패: 이미 수강 중인 과목입니다.");
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }
    
    public void printAllSubjects() {
        System.out.println("\n[전체 과목 목록]");
        ArrayList<Subject> list = subjectController.getAllSubjects();
        if(list.isEmpty()) {
            System.out.println(">> 등록된 과목이 없습니다.");
            return;
        }
        for(Subject s : list) {
            System.out.println(s); 
        }
    }
}