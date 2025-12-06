package main.controller;

import main.model.Person;
import main.repository.UserRepository;
import main.exception.ValidationException;
import main.exception.NotFoundException;
import main.exception.AuthenticationException;

public class AuthController {

    // 현재 로그인된 사용자 반환 (SystemController 위임)
    public Person getCurUser() {
        return SystemController.getInstance().getCurrentUser();
    }

    public void login(String id, String pw) throws ValidationException, NotFoundException, AuthenticationException {
        // 1. 입력값 유효성 검사
        if (id == null || id.isBlank() || pw == null || pw.isBlank()) {
            throw new ValidationException("아이디와 비밀번호를 모두 입력해주세요.");
        }

        // 2. 저장소 가져오기
        UserRepository userRepo = SystemController.getInstance().getUserRepository();
        
        // 3. 사용자 조회 (수정: findByMyId -> getById)
        // UserRepository 인터페이스 정의에 따라 getById 사용
        Person person = userRepo.getById(id);
        
        // getById가 null을 반환할 수도 있고 예외를 던질 수도 있음.
        // 인터페이스 상 NotFoundException을 던지게 되어 있으나, 혹시 null이 올 경우를 대비
        if (person == null) {
             throw new NotFoundException("존재하지 않는 아이디입니다.");
        }

        // 4. 비밀번호 확인 (수정: getMyPassWd().equals -> compPassWd)
        // Person 클래스는 비밀번호 Getter를 제공하지 않고 비교 메서드만 제공함
        if (!person.compPassWd(pw)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        // 5. 로그인 성공 처리 (세션 등록)
        SystemController.getInstance().setCurrentUser(person);
    }

    public void logout() throws AuthenticationException {
        if (getCurUser() == null) {
            throw new AuthenticationException("로그인 상태가 아닙니다.");
        }
        SystemController.getInstance().setCurrentUser(null);
    }
}