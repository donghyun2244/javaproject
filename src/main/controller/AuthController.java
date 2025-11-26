package main.controller;

import main.model.Person;
import main.repository.UserRepository;
import main.repository.RepoFactory;
import main.repository.RepoMode;
import main.exception.ValidationException;
import main.exception.NotFoundException;
import main.exception.AuthenticationException;

public class AuthController {

    private Person curUser; // 현재 로그인한 사용자 객체 (로그인 전 null)
    private UserRepository userRepo;

    public AuthController() {
        this.curUser = null;
        // 팩토리를 통해 리포지토리 의존성 주입
        this.userRepo = RepoFactory.getUserRepository(RepoMode.FILE);
    }

    public Person getCurUser() {
        return this.curUser;
    }

    // 로그인 처리
    public void login(String myId, String myPassWd) throws ValidationException, NotFoundException, AuthenticationException {
        
        // 유효성 검증: ID(영문/숫자), PW(특수문자 포함) 형식 및 길이 체크
        if (myId == null || myId.trim().isEmpty() || myId.length() < 4 || !myId.matches("^[a-zA-Z0-9]*$")) {
            throw new ValidationException("아이디는 영문/숫자 조합으로 4자리 이상이어야 합니다.");
        }
        
        if (myPassWd == null || myPassWd.trim().isEmpty() || myPassWd.length() < 4 || 
            !myPassWd.matches("^[a-zA-Z0-9!@#$%]*$")) {
            throw new ValidationException("비밀번호는 4자리 이상이며 영문, 숫자, 특수문자(!@#$%)만 허용됩니다.");
        }

        // 중복 로그인 방지
        if (this.curUser != null) {
            throw new AuthenticationException("이미 로그인된 상태입니다.");
        }

        Person user = userRepo.findByMyId(myId);
        
        if (user == null) {
            throw new NotFoundException("존재하지 않는 아이디입니다.");
        }

        // 비밀번호 일치 확인 (Model 메서드 위임)
        if (!user.compPassWd(myPassWd)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        this.curUser = user;
        System.out.println("[알림] " + user.getName() + "님 환영합니다.");
    }

    // 로그아웃 처리
    public void logout() throws AuthenticationException {
        if (this.curUser == null) {
            throw new AuthenticationException("로그인된 상태가 아닙니다.");
        }

        System.out.println("[알림] " + this.curUser.getName() + "님이 로그아웃 하셨습니다.");
        this.curUser = null;
    }
}