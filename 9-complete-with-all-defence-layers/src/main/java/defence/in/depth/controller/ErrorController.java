package defence.in.depth.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/error")
public class ErrorController {

    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity throwException() {
        throw new NotImplementedException("Not implemented on purpose");
    }

}
