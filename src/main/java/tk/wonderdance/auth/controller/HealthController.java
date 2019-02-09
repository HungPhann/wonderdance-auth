package tk.wonderdance.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HealthController {
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public ResponseEntity<?> auth() {
        return ResponseEntity.ok("OK");
    }
}