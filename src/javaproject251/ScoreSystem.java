package javaproject251;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ScoreSystem {

	public static void main(String[] args) {
		testCsvAccess();
		
		loginMenu();
		

	}
	
	public static void testCsvAccess() {
        String csvFile = "listdata.csv"; // 프로젝트 루트 경로에 있는 파일 이름
        String line = "";
                
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            
            boolean hasContent = false;
            // 파일 내용을 줄 단위로 읽습니다.
            while ((line = br.readLine()) != null) {// 해당 파일이 비어져 있으면 실행이 안됨
                System.out.println("[CSV Data] " + line);
                hasContent = true;
            }
            
            if (!hasContent) {  // 파일이 비어져 있으면 실행.
                System.out.println("listdata.csv 파일은 현재 비어있습니다.");
                System.out.println("   파일 내용을 채우면 여기에 데이터가 출력됩니다.");
            }
            
        } catch (IOException e) {
            // 파일을 찾지 못하거나 읽을 수 없을 때 발생하는 오류 처리
            System.err.println("   " + csvFile + " 파일을 찾을 수 없거나 읽을 수 없습니다.");
            System.err.println("   오류 메시지: " + e.getMessage());
        }

    }
	
	public static void loginMenu() {
		System.out.println("----------------------------------------------");
		System.out.println("로그인");
		System.out.println("학생이면 1 교수면 2를 입력");
		
		Scanner scanner = new Scanner(System.in); 
		int a=scanner.nextInt();
		if(a==1) {
			//학생 기능 실행
		}else if(a==2) {
			//교수기능 실행
		}else {
			//잘못된 숫자를 입력하셨습니다. 다시 입력해주세요.
		}
		
	}
	
	public static void loginStudent() {
		
	}
	
	public static void loginpr() {
		
	}
}





