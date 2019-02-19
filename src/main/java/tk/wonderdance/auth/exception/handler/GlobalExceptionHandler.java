package tk.wonderdance.auth.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import tk.wonderdance.auth.exception.exception.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="User Not Found")  // 404
    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFoundException(HttpServletRequest request, UserNotFoundException e){
        logger.info("[UserNotFoundException] " + e.getMessage() +  " at: " + request.getRequestURL());
    }

    @ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Permission Denied")  // 403
    @ExceptionHandler(ForbiddenException.class)
    public void handleForbiddenException(HttpServletRequest request, ForbiddenException e){
        logger.info("[ForbiddenException] " + e.getMessage() + " at: " + request.getRequestURL());
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User is not activated") // 403
    @ExceptionHandler(AccountNotActivatedException.class)
    public void handleAccountNotActivatedException(HttpServletRequest request, AccountNotActivatedException e){
        logger.info("[AccountNotActivatedException] " + e.getMessage() + " at: " + request.getRequestURL());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Authentication fail")    // 401
    @ExceptionHandler(UnauthorizedException.class)
    public void handleUnauthorizedException(HttpServletRequest request, UnauthorizedException e){
        logger.info("[UnauthorizedException] " + e.getMessage() + " at: " + request.getRequestURL());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource existed")   // 409
    @ExceptionHandler(ResourceConflictException.class)
    public void handleResourceConflictException(HttpServletRequest request, ResourceConflictException e){
        logger.info("[ResourceConflictException] " + e.getMessage() + " at: " + request.getRequestURL());
    }
}
