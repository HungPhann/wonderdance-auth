package tk.wonderdance.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tk.wonderdance.auth.exception.exception.ForbiddenException;
import tk.wonderdance.auth.exception.exception.ResourceConflictException;
import tk.wonderdance.auth.exception.exception.UnauthorizedException;
import tk.wonderdance.auth.exception.exception.UserNotFoundException;
import tk.wonderdance.auth.helper.StringGenerator;
import tk.wonderdance.auth.model.User;
import tk.wonderdance.auth.payload.user.activate.ActivateUserRequest;
import tk.wonderdance.auth.payload.user.activate.ActivateUserResponse;
import tk.wonderdance.auth.payload.user.activation_code.GetActivationCodeResponse;
import tk.wonderdance.auth.payload.user.change_password.ChangePasswordRequest;
import tk.wonderdance.auth.payload.user.change_password.ChangePasswordResponse;
import tk.wonderdance.auth.payload.user.create.CreateUserRequest;
import tk.wonderdance.auth.payload.user.create.CreateUserResponse;
import tk.wonderdance.auth.payload.user.reset_password.ResetPasswordResponse;
import tk.wonderdance.auth.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StringGenerator stringGenerator;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest requestBody) throws MethodArgumentTypeMismatchException, ResourceConflictException {

        boolean userExisted = userRepository.existsByEmail(requestBody.getEmail());
        if (userExisted){
            throw new ResourceConflictException("User existed with email=" + requestBody.getEmail());
        }
        else {
            String hashPassword = BCrypt.hashpw(requestBody.getPassword(), BCrypt.gensalt());
            String activateCode = stringGenerator.generateString(16);
            User user = new User(requestBody.getEmail(), hashPassword, activateCode);
            userRepository.save(user);
            long userID = user.getId();
            CreateUserResponse createUserResponse = new CreateUserResponse(userID, activateCode);
            return ResponseEntity.ok(createUserResponse);
        }
    }


    @RequestMapping(value = "{email}/activate", method = RequestMethod.POST)
    public ResponseEntity<?> activateUser(@PathVariable("email") String email,
                                          @Valid @RequestBody ActivateUserRequest requestBody) throws MethodArgumentTypeMismatchException, UserNotFoundException, ForbiddenException{

        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();

            if (requestBody.getActivate_code().equals(user.getActivate_code())) {
                user.setActivate(true);
                userRepository.save(user);

                return new ResponseEntity<>(HttpStatus.OK);

            } else {
                throw new ForbiddenException("Fail to activate User with email=" + email + " and activate_code=" + requestBody.getActivate_code());
            }
        }
        catch (NoSuchElementException e){
            throw new UserNotFoundException("Cannot find User with email=" + email);
        }
    }


    @RequestMapping(value = "{email}/activate", method = RequestMethod.GET)
    public ResponseEntity<?> getActivationCode(@PathVariable("email") String email) throws MethodArgumentTypeMismatchException, UserNotFoundException{
        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();
            String activationCode = user.getActivate_code();
            GetActivationCodeResponse getActivationCodeResponse = new GetActivationCodeResponse(activationCode);
            return ResponseEntity.ok(getActivationCodeResponse);
        }
        catch (NoSuchElementException e){
            throw new UserNotFoundException("Cannot find User with email=" + email);
        }
    }


    @RequestMapping(value = "{user_id}/password", method = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(@PathVariable("user_id") long userID,
                                            @Valid @RequestBody ChangePasswordRequest requestBody) throws MethodArgumentTypeMismatchException, UserNotFoundException, UnauthorizedException{
        Optional<User> userQuery = userRepository.findUserById(userID);

        try {
            User user= userQuery.get();

            if(BCrypt.checkpw(requestBody.getOld_password(), user.getPassword())){
                String hashNewPassword = BCrypt.hashpw(requestBody.getNew_password(), BCrypt.gensalt());
                user.setPassword(hashNewPassword);
                userRepository.save(user);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                throw new UnauthorizedException("Fail to change password for User with user_id=" + userID);
            }

        }
        catch (NoSuchElementException e){
            throw new UserNotFoundException("Cannot find User with user_id=" + userID);

        }
    }


    @RequestMapping(value = "{email}/password/reset", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@PathVariable("email") String email) throws MethodArgumentTypeMismatchException, UserNotFoundException{
        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();
            String newPassword = stringGenerator.generateString(16);
            String hashNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashNewPassword);
            userRepository.save(user);

            boolean success = true;
            ResetPasswordResponse resetPasswordResponse = new ResetPasswordResponse(success, newPassword);
            return ResponseEntity.ok(resetPasswordResponse);
        }
        catch (NoSuchElementException e){
            throw new UserNotFoundException("Cannot find User with email=" + email);
        }
    }

}
