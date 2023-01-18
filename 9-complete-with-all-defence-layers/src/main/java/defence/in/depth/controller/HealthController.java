package defence.in.depth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Value("${app.version:unknown}")
    String version;

    @GetMapping("/live")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Object> live() {
        return ResponseEntity.ok(Map.of("version", version));
    }

    @GetMapping("/ready")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> ready() {
        return ResponseEntity.ok(Map.of("version", version, "runtime", Runtime.version().toString()));
    }

}
