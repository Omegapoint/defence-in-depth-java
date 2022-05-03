package defence.in.depth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/api/no-authentication")
public class NoAuthenticationController {

    @GetMapping("/hello")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("This endpoint is accessible even if you are not logged in");
    }

}
