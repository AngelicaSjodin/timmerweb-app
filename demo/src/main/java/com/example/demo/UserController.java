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
    String name = user.getName() == null ? "" : user.getName().trim();
    user.setName(name);

    if (name.isEmpty()) {
      return ResponseEntity.status(400).body(Map.of("message","användarenman saknas"));
    }

    if(repo.findByName(name)!= null){
      return ResponseEntity.status(400).body(Map.of("message", "användare finns redan"));
    }

      // Hasha lösenord
    user.setPassword(encoder.encode(user.getPassword()));
    repo.save(user);

    return ResponseEntity.ok(Map.of("message","Konto skapat"));

    }

  //kollar så allt matchar
  @PostMapping("/login")//<?> om det returnener olika för olika siruationer?
  public ResponseEntity<?> login(@RequestBody User loginUser){
    String name = loginUser.getName() == null ? "" : loginUser.getName().trim();
    String password = loginUser.getPassword() == null ? "" : loginUser.getPassword();
    
    User userDb = repo.findByName(name);
    

    if (userDb == null){
      return ResponseEntity.status(401).body(Map.of("message","hittar inte användarnamnet"));
    }

    if (!encoder.matches(password, userDb.getPassword())){
      return ResponseEntity.status(401).body(Map.of("message","fel lösen"));
    }
    return ResponseEntity.ok(Map.of("message","inloggning ok"));
  }
}
