package defence.in.depth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/no-authorization")
public class NoAuthorizationController {

    @GetMapping("/hello")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("This endpoint is accessible if you have a valid token");
    }

}
