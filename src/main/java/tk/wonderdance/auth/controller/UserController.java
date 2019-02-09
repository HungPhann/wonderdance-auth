package tk.wonderdance.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tk.wonderdance.auth.helper.StringGenerator;
import tk.wonderdance.auth.model.User;
import tk.wonderdance.auth.payload.user.activate.ActivateUserFailResponse;
import tk.wonderdance.auth.payload.user.activate.ActivateUserSuccessResponse;
import tk.wonderdance.auth.payload.user.activation_code.GetActivationCodeFailResponse;
import tk.wonderdance.auth.payload.user.activation_code.GetActivationCodeSuccessResponse;
import tk.wonderdance.auth.payload.user.change_password.ChangePasswordFailResponse;
import tk.wonderdance.auth.payload.user.change_password.ChangePasswordSuccessResponse;
import tk.wonderdance.auth.payload.user.create.CreateUserFailResponse;
import tk.wonderdance.auth.payload.user.create.CreateUserSuccessResponse;
import tk.wonderdance.auth.payload.user.reset_password.ResetPasswordFailResponse;
import tk.wonderdance.auth.payload.user.reset_password.ResetPasswordSuccessResponse;
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

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestParam("email") String email,
                                        @RequestParam("password") String password){

        boolean userExisted = userRepository.existsByEmail(email);
        if (userExisted){
            boolean success = false;
            String message = "Email exists";
            CreateUserFailResponse createUserFailResponse = new CreateUserFailResponse(success, message);
            return ResponseEntity.ok(createUserFailResponse);
        }
        else {
            String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String activateCode = stringGenerator.generateString(16);
            User user = new User(email, hashPassword, activateCode);
            userRepository.save(user);
            boolean success = true;
            long userID = user.getId();
            CreateUserSuccessResponse createUserSuccessResponse = new CreateUserSuccessResponse(success, userID, activateCode);
            return ResponseEntity.ok(createUserSuccessResponse);
        }
    }


    @RequestMapping(value = "activate", method = RequestMethod.POST)
    public ResponseEntity<?> activateUser(@RequestParam("email") String email,
                                          @RequestParam("activate_code") String activate_code){

        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();

            if (activate_code.equals(user.getActivate_code())) {
                user.setActivate(true);
                userRepository.save(user);

                boolean success = true;
                ActivateUserSuccessResponse activateUserSuccessResponse = new ActivateUserSuccessResponse(success);
                return ResponseEntity.ok(activateUserSuccessResponse);

            } else {
                boolean success = false;
                int error_code = 2;
                String message = "Email and activation code do not match";
                ActivateUserFailResponse activateUserFailResponse = new ActivateUserFailResponse(success,error_code,message);
                return ResponseEntity.ok(activateUserFailResponse);
            }
        }
        catch (NoSuchElementException e){
            boolean success = false;
            int error_code = 1;
            String message = "Email does not exist";
            ActivateUserFailResponse activateUserFailResponse = new ActivateUserFailResponse(success,error_code,message);
            return ResponseEntity.ok(activateUserFailResponse);
        }
    }


    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public ResponseEntity<?> getActivationCode(@RequestParam("email") String email){
        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();
            boolean success = true;
            String activationCode = user.getActivate_code();
            GetActivationCodeSuccessResponse getActivationCodeSuccessResponse = new GetActivationCodeSuccessResponse(success, activationCode);
            return ResponseEntity.ok(getActivationCodeSuccessResponse);
        }
        catch (NoSuchElementException e){
            boolean success = false;
            int error_code = 1;
            String message = "Email does not exist";
            GetActivationCodeFailResponse getActivationCodeFailResponse = new GetActivationCodeFailResponse(success, error_code, message);
            return ResponseEntity.ok(getActivationCodeFailResponse);
        }
    }


    @RequestMapping(value = "password/change", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestParam("user_id") long userID,
                                            @RequestParam("old_password") String oldPassword,
                                            @RequestParam("new_password") String newPassword){
        Optional<User> userQuery = userRepository.findUserById(userID);

        try {
            User user= userQuery.get();

            if(BCrypt.checkpw(oldPassword, user.getPassword())){
                String hashNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashNewPassword);
                userRepository.save(user);

                boolean success = true;
                ChangePasswordSuccessResponse changePasswordSuccessResponse = new ChangePasswordSuccessResponse(success);
                return ResponseEntity.ok(changePasswordSuccessResponse);
            }
            else {
                boolean success = false;
                int error_code = 2;
                String message = "Old password is incorrect";
                ChangePasswordFailResponse changePasswordFailResponse = new ChangePasswordFailResponse(success, error_code, message);
                return ResponseEntity.ok(changePasswordFailResponse);
            }

        }
        catch (NoSuchElementException e){
            boolean success = false;
            int error_code = 1;
            String message = "User ID does not exist";
            ChangePasswordFailResponse changePasswordFailResponse = new ChangePasswordFailResponse(success, error_code, message);
            return ResponseEntity.ok(changePasswordFailResponse);
        }
    }


    @RequestMapping(value = "password/reset", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email){
        Optional<User> userQuery = userRepository.findUserByEmail(email);

        try {
            User user= userQuery.get();
            String newPassword = stringGenerator.generateString(16);
            String hashNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashNewPassword);
            userRepository.save(user);

            boolean success = true;
            ResetPasswordSuccessResponse resetPasswordSuccessResponse = new ResetPasswordSuccessResponse(success, newPassword);
            return ResponseEntity.ok(resetPasswordSuccessResponse);
        }
        catch (NoSuchElementException e){
            boolean success = false;
            int error_code = 1;
            String message = "Email does not exist";
            ResetPasswordFailResponse resetPasswordFailResponse = new ResetPasswordFailResponse(success, error_code, message);
            return ResponseEntity.ok(resetPasswordFailResponse);
        }
    }

}
