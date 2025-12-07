package main.controller;

import main.model.Person;
import main.repository.UserRepository;
import main.exception.ValidationException;
import main.exception.NotFoundException;
import main.exception.AuthenticationException;

public class AuthController {

    // 현재 로그인된 사용자 반환하는 거
    public Person getCurUser() {
        return SystemController.getInstance().getCurrentUser();
    }

    public void login(String id, String pw) throws ValidationException, NotFoundException, AuthenticationException {
        // 입력값 검증
        if (id == null || id.isBlank() || pw == null || pw.isBlank()) {
            throw new ValidationException("아이디와 비밀번호를 모두 입력해주세요.");
        }

        //저장소 가져오기
        UserRepository userRepo = SystemController.getInstance().getUserRepository();
        
        //사용자 조회 
        Person person = userRepo.getById(id);

        if (person == null) {
             throw new NotFoundException("존재하지 않는 아이디입니다.");
        }
        //비밀번호 검증
        if (!person.compPassWd(pw)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        //로그인 성공 (세션 등록)
        SystemController.getInstance().setCurrentUser(person);
    }

    public void logout() throws AuthenticationException {
        if (getCurUser() == null) {
            throw new AuthenticationException("로그인 상태가 아닙니다.");
        }
        SystemController.getInstance().setCurrentUser(null);
    }
}