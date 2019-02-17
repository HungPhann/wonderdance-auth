package tk.wonderdance.auth.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tk.wonderdance.auth.exception.exception.AccountNotActivatedException;
import tk.wonderdance.auth.exception.exception.UnauthorizedException;
import tk.wonderdance.auth.exception.exception.UserNotFoundException;
import tk.wonderdance.auth.helper.JwtTokenProvider;
import tk.wonderdance.auth.model.User;
import tk.wonderdance.auth.payload.auth.authenticate.AuthenticateResponse;
import tk.wonderdance.auth.payload.auth.verify_token.VerifyTokenResponse;
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
                                              @RequestParam("password") String password) throws MethodArgumentTypeMismatchException, UserNotFoundException, AccountNotActivatedException, UnauthorizedException {

        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();
            boolean checkPassword = BCrypt.checkpw(password, user.getPassword());

            if (checkPassword) {
                if(user.isActivate()) {
                    String token = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail());
                    AuthenticateResponse authenticateResponse = new AuthenticateResponse(true, token);
                    return ResponseEntity.ok(authenticateResponse);
                }

                else {
                    throw new AccountNotActivatedException("User with email=" + email + " is not activated");
                }

            } else {
                throw new UnauthorizedException("Cannot authenticate User with email=" + email);
            }
        }
        catch (NoSuchElementException e){
            throw new UserNotFoundException("Cannot find User with email=" + email);
        }
    }


    @RequestMapping(value = "token/verify", method = RequestMethod.POST)
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) throws UnauthorizedException {
        try {
            Claims payload = jwtTokenProvider.verifyAccessToken(token);
            boolean success = true;
            VerifyTokenResponse verifyTokenResponse = new VerifyTokenResponse(success, payload);
            return ResponseEntity.ok(verifyTokenResponse);
        }
        catch (Exception e){
            throw new UnauthorizedException("");
        }
    }

}
