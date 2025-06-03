package securityPractice.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityPractice.domain.user.dto.request.SignUpRequest;
import securityPractice.domain.user.dto.response.SignUpResponse;
import securityPractice.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUp(request));
    }
}
