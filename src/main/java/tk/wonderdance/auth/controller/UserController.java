package tk.wonderdance.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tk.wonderdance.auth.exception.exception.ForbiddenException;
import tk.wonderdance.auth.exception.exception.ResourceConflictException;
import tk.wonderdance.auth.exception.exception.UnauthorizedException;
import tk.wonderdance.auth.exception.exception.UserNotFoundException;
import tk.wonderdance.auth.helper.StringGenerator;
import tk.wonderdance.auth.model.User;
import tk.wonderdance.auth.payload.user.activate.ActivateUserResponse;
import tk.wonderdance.auth.payload.user.activation_code.GetActivationCodeResponse;
import tk.wonderdance.auth.payload.user.change_password.ChangePasswordResponse;
import tk.wonderdance.auth.payload.user.create.CreateUserResponse;
import tk.wonderdance.auth.payload.user.reset_password.ResetPasswordResponse;
import tk.wonderdance.auth.repository.UserRepository;

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
    public ResponseEntity<?> createUser(@RequestParam("email") String email,
                                        @RequestParam("password") String password) throws MethodArgumentTypeMismatchException, ResourceConflictException {

        boolean userExisted = userRepository.existsByEmail(email);
        if (userExisted){
            throw new ResourceConflictException("User existed with email=" + email);
        }
        else {
            String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String activateCode = stringGenerator.generateString(16);
            User user = new User(email, hashPassword, activateCode);
            userRepository.save(user);
            boolean success = true;
            long userID = user.getId();
            CreateUserResponse createUserResponse = new CreateUserResponse(success, userID, activateCode);
            return ResponseEntity.ok(createUserResponse);
        }
    }


    @RequestMapping(value = "{email}/activate", method = RequestMethod.POST)
    public ResponseEntity<?> activateUser(@PathVariable("email") String email,
                                          @RequestParam("activate_code") String activate_code) throws MethodArgumentTypeMismatchException, UserNotFoundException, ForbiddenException{

        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();

            if (activate_code.equals(user.getActivate_code())) {
                user.setActivate(true);
                userRepository.save(user);

                boolean success = true;
                ActivateUserResponse activateUserResponse = new ActivateUserResponse(success);
                return ResponseEntity.ok(activateUserResponse);

            } else {
                throw new ForbiddenException("Fail to activate User with email=" + email + " and activate_code=" + activate_code);
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
            boolean success = true;
            String activationCode = user.getActivate_code();
            GetActivationCodeResponse getActivationCodeResponse = new GetActivationCodeResponse(success, activationCode);
            return ResponseEntity.ok(getActivationCodeResponse);
        }
        catch (NoSuchElementException e){
            throw new UserNotFoundException("Cannot find User with email=" + email);
        }
    }


    @RequestMapping(value = "{user_id}/password", method = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(@PathVariable("user_id") long userID,
                                            @RequestParam("old_password") String oldPassword,
                                            @RequestParam("new_password") String newPassword) throws MethodArgumentTypeMismatchException, UserNotFoundException, UnauthorizedException{
        Optional<User> userQuery = userRepository.findUserById(userID);

        try {
            User user= userQuery.get();

            if(BCrypt.checkpw(oldPassword, user.getPassword())){
                String hashNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashNewPassword);
                userRepository.save(user);

                boolean success = true;
                ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse(success);
                return ResponseEntity.ok(changePasswordResponse);
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
