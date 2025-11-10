package javaproject251;

import java.util.Scanner;

public class ScoreSystem {

	public static void main(String[] args) {
		loginMenu();
		

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





