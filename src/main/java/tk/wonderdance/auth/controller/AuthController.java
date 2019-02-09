package tk.wonderdance.auth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tk.wonderdance.auth.helper.JwtTokenProvider;
import tk.wonderdance.auth.model.User;
import tk.wonderdance.auth.payload.auth.authenticate.AuthenticateFailResponse;
import tk.wonderdance.auth.payload.auth.authenticate.AuthenticateSuccessResponse;
import tk.wonderdance.auth.payload.auth.verify_token.VerifyTokenFailResponse;
import tk.wonderdance.auth.payload.auth.verify_token.VerifyTokenSuccessResponse;
import tk.wonderdance.auth.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestParam("email") String email,
                                              @RequestParam("password") String password){

        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();
            boolean checkPassword = BCrypt.checkpw(password, user.getPassword());

            if (checkPassword) {
                if(user.isActivate()) {
                    String token = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail());
                    AuthenticateSuccessResponse authenticateSuccessResponse = new AuthenticateSuccessResponse(true, token);
                    return ResponseEntity.ok(authenticateSuccessResponse);
                }

                else {
                    int error_code = 3;
                    String message = "User is not activated";
                    AuthenticateFailResponse authenticateFailResponse = new AuthenticateFailResponse(false, error_code, message);
                    return ResponseEntity.ok(authenticateFailResponse);
                }

            } else {
                int error_code = 2;
                String message = "Email and password do not match";
                AuthenticateFailResponse authenticateFailResponse = new AuthenticateFailResponse(false, error_code, message);
                return ResponseEntity.ok(authenticateFailResponse);
            }
        }
        catch (NoSuchElementException e){
            int error_code = 1;
            String message = "Email does not exist";
            AuthenticateFailResponse authenticateFailResponse = new AuthenticateFailResponse(false, error_code, message);
            return ResponseEntity.ok(authenticateFailResponse);
        }
    }


    @RequestMapping(value = "token/verify", method = RequestMethod.POST)
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token){
        try {
            Claims payload = jwtTokenProvider.verifyAccessToken(token);
            boolean success = true;
            VerifyTokenSuccessResponse verifyTokenSuccessResponse = new VerifyTokenSuccessResponse(success, payload);
            return ResponseEntity.ok(verifyTokenSuccessResponse);
        }
        catch (ExpiredJwtException e){
            boolean success = false;
            int error_code = 1;
            String message = "Token expired";
            VerifyTokenFailResponse verifyTokenFailResponse = new VerifyTokenFailResponse(success, error_code, message);
            return ResponseEntity.ok(verifyTokenFailResponse);
        }
        catch (Exception e){
            boolean success = false;
            int error_code = 2;
            String message = "Cannot parse token";
            VerifyTokenFailResponse verifyTokenFailResponse = new VerifyTokenFailResponse(success, error_code, message);
            return ResponseEntity.ok(verifyTokenFailResponse);
        }
    }

}
