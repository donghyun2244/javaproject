package main.view; // 패키지 선언 추가

import java.util.Scanner;
import main.controller.ScoreController;
// 문서에 명시된 예외 클래스 import
import main.exception.ValidationException;
import main.exception.NotFoundException;

public class ScoreView {

    private ScoreController scoreController;
    private Scanner scanner;

    public ScoreView(ScoreController scoreController) {
        this.scoreController = scoreController;
        this.scanner = new Scanner(System.in);
    }

    public void registerScoreView() {
        System.out.println("\n[성적 등록]");
        System.out.print("과목 코드 입력: ");
        String subjectCode = scanner.nextLine();
        System.out.print("학생 ID 입력: ");
        String studentId = scanner.nextLine();
        System.out.print("점수 입력(0~100): ");
        
        try {
            int score = Integer.parseInt(scanner.nextLine());
            scoreController.inputScore(subjectCode, studentId, score);
            System.out.println(">> 성적이 등록되었습니다.");
        } catch (NumberFormatException e) {
            System.out.println(">> 오류: 점수는 숫자로 입력해주세요.");
        } catch (NotFoundException e) {
            System.out.println(">> 등록 실패: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println(">> 입력 오류: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }

    public void updateScoreView() {
        System.out.println("\n[성적 수정]");
        System.out.print("과목 코드 입력: ");
        String subjectCode = scanner.nextLine();
        System.out.print("학생 ID 입력: ");
        String studentId = scanner.nextLine();
        System.out.print("수정할 점수 입력(0~100): ");

        try {
            int newScore = Integer.parseInt(scanner.nextLine());
            scoreController.updateScore(subjectCode, studentId, newScore);
            System.out.println(">> 성적이 수정되었습니다.");
        } catch (NumberFormatException e) {
            System.out.println(">> 오류: 점수는 숫자로 입력해주세요.");
        } catch (NotFoundException e) {
            System.out.println(">> 수정 실패: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println(">> 입력 오류: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }

    public void printSubjectStatisticsView() {
        System.out.println("\n[과목별 성적 평균 조회]");
        System.out.print("과목 코드 입력: ");
        String subjectCode = scanner.nextLine();

        try {
            double avg = scoreController.getSubjectAverage(subjectCode);
            System.out.printf(">> 해당 과목의 평균 점수: %.2f점\n", avg);
        } catch (NotFoundException e) {
            System.out.println(">> 조회 실패: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println(">> 알림: 수강생이 없어 평균을 계산할 수 없습니다.");
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }

    public void checkScholarshipView() {
        System.out.println("\n[장학금 수혜 대상 확인]");
        System.out.print("확인할 학생 ID 입력: ");
        String studentId = scanner.nextLine();

        try {
            boolean isEligible = scoreController.checkScholarshipEligibility(studentId);
            if (isEligible) {
                System.out.println(">> 축하합니다! 해당 학생은 장학금 수혜 대상입니다.");
            } else {
                System.out.println(">> 아쉽지만 장학금 수혜 대상이 아닙니다.");
            }
        } catch (NotFoundException e) {
            System.out.println(">> 확인 실패: 수강 내역이 존재하지 않습니다.");
        } catch (Exception e) {
            System.out.println(">> 오류 발생: " + e.getMessage());
        }
    }
}