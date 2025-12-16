package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import com.example.demo.UserRepository;
import com.example.demo.User;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:8100")

public class UserController {
  private final UserRepository repo;
  private final PasswordEncoder encoder;

  public UserController(UserRepository repo, PasswordEncoder encoder){ //auto injection från security config
    this.repo = repo; 
    this.encoder = encoder;
    }

  @GetMapping
  public List<User> all(){ 
    return repo.findAll(); }

  @PostMapping("/register")
  public ResponseEntity<?>register(@RequestBody User user){ 
    if (repo.findByName(user.getName()) != null) {
            return ResponseEntity.status(400).body(Map.of("message","användare finns resan"));
    }
      // Hasha lösenord
    user.setPassword(encoder.encode(user.getPassword()));

    repo.save(user);

    return ResponseEntity.ok(Map.of("message","Konto skapat"));

    }

  //kollar så allt matchar
  @PostMapping("/login")//<?> om det returnener olika för olika siruationer?
  public ResponseEntity<?> login(@RequestBody User loginUser){

    User userDb = repo.findByName(loginUser.getName());

    if (userDb == null){
      return ResponseEntity.status(401).body("hittar inte användarnamnet");
    }

    if (!encoder.matches(loginUser.getPassword(),userDb.getPassword())){
      return ResponseEntity.status(401).body("fel lösen");
    }
    return ResponseEntity.ok("inloggning ok");
  }
}
