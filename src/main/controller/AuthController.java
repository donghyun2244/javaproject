package main.controller;

// 다른 패키지에 있는 클래스들을 가져옵니다.
// (만약 빨간 줄이 뜨면 패키지명을 본인 프로젝트에 맞게 수정해야 합니다)
import main.model.Person;
import main.repository.UserRepository;
import main.repository.RepoFactory;
import main.repository.RepoMode;
import main.exception.ValidationException;
import main.exception.NotFoundException;
import main.exception.AuthenticationException;

public class AuthController {

    // [필드] 
    // 1. 현재 로그인 한 사람의 객체를 저장 (로그인 전까지는 null)
    private Person curUser;
    
    // 2. 사용자 정보 확인을 위한 리포지토리 (도구)
    private UserRepository userRepo;

    // [생성자]
    public AuthController() {
        // 초기에는 로그인한 사람이 없음
        this.curUser = null;
        
        // 팩토리 패턴을 통해 FILE 모드의 리포지토리를 가져옴
        this.userRepo = RepoFactory.getUserRepository(RepoMode.FILE);
    }

    // [Getter] 현재 로그인한 사용자 객체 반환 (SystemController 등에서 사용)
    public Person getCurUser() {
        return this.curUser;
    }

    // [메서드] 로그인 기능
    public void login(String myId, String myPassWd) throws ValidationException, NotFoundException, AuthenticationException {
        
        // 1. 유효성 검증 (ValidationException)
        // ID 입력값 검사: null, 빈문자열, 4자리 미만, 영문/숫자 외 문자 포함 시
        if (myId == null || myId.trim().isEmpty() || myId.length() < 4 || !myId.matches("^[a-zA-Z0-9]*$")) {
            throw new ValidationException("아이디는 영문/숫자 조합으로 4자리 이상이어야 합니다.");
        }
        
        // PW 입력값 검사: null, 빈문자열, 4자리 미만, 형식 불일치 시
        if (myPassWd == null || myPassWd.trim().isEmpty() || myPassWd.length() < 4 || 
            !myPassWd.matches("^[a-zA-Z0-9!@#$%]*$")) {
            throw new ValidationException("비밀번호는 4자리 이상이며 영문, 숫자, 특수문자(!@#$%)만 허용됩니다.");
        }

        // 2. 이미 로그인 상태인지 확인 (AuthenticationException)
        if (this.curUser != null) {
            throw new AuthenticationException("이미 로그인된 사용자가 있습니다. 먼저 로그아웃 해주세요.");
        }

        // 3. 사용자 탐색 (NotFoundException)
        // 리포지토리에서 ID로 사람을 찾음
        Person user = userRepo.findByMyId(myId);
        
        if (user == null) {
            throw new NotFoundException("존재하지 않는 아이디입니다.");
        }

        // 4. 비밀번호 일치 확인 (AuthenticationException)
        // 모델(Person)이 가진 compPassWd 메서드 활용
        if (!user.compPassWd(myPassWd)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        // 5. 로그인 성공 처리
        this.curUser = user;
        System.out.println("[알림] " + user.getName() + "님 환영합니다.");
    }

    // [메서드] 로그아웃 기능
    public void logout() throws AuthenticationException {
        // 예외: 로그인 상태가 아닐 때
        if (this.curUser == null) {
            throw new AuthenticationException("로그인된 상태가 아닙니다.");
        }

        // 로그아웃 처리 (필드를 다시 null로)
        System.out.println("[알림] " + this.curUser.getName() + "님이 로그아웃 하셨습니다.");
        this.curUser = null;
    }
}