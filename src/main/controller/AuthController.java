package main.controller;

import main.model.Person;
import main.repository.UserRepository;
import main.exception.ValidationException;
import main.exception.NotFoundException;
import main.exception.AuthenticationException;

public class AuthController {

    // 현재 로그인된 사용자 반환
    public Person getCurUser() {
        return SystemController.getInstance().getCurrentUser();
    }

    public void login(String id, String pw) throws ValidationException, NotFoundException, AuthenticationException {
        if (id == null || id.isBlank() || pw == null || pw.isBlank()) {
            throw new ValidationException("아이디와 비밀번호를 모두 입력해주세요.");
        }

        // SystemController에서 UserRepo 가져오기
        UserRepository userRepo = SystemController.getInstance().getUserRepository();
        
        // 아이디로 사용자 찾기 (없으면 NotFoundException 발생 가정)
        Person person = userRepo.findByMyId(id);
        
        if (person == null) {
             throw new NotFoundException("존재하지 않는 아이디입니다.");
        }

        // 비밀번호 확인 (Person 클래스에 validatePassword 등의 메서드가 있거나 직접 비교)
        // 여기서는 getMyPassWd()와 비교한다고 가정
        if (!person.getMyPassWd().equals(pw)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 처리
        SystemController.getInstance().setCurrentUser(person);
    }

    public void logout() throws AuthenticationException {
        if (getCurUser() == null) {
            throw new AuthenticationException("로그인 상태가 아닙니다.");
        }
        SystemController.getInstance().setCurrentUser(null);
    }
}