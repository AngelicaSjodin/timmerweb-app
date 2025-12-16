package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import com.example.demo.UserRepository;
import com.example.demo.User;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:8100")

public class UserController {
  private final UserRepository repo;
  private final PasswordEncoder encoder;

  public UserController(UserRepository repo){ 
    this.repo = repo; 
    this.encoder = encoder;
    }

  @GetMapping
  public List<User> all(){ 
    return repo.findAll(); }

  @PostMapping("/register")
  public User create(@RequestBody User user){ 
    //hashar
    user.setPassword(encoder.encode(user.getPassword()));

    return repo.save(user); }
  //kollar så allt matchar
  @PostMapping("/login")//<?> om det returnener olika för olika siruationer?
  public ResponseEntity<?> Login(@RequestBody User loginUser){

    User userDb = repo.findByName(loginUser.getName());

    if (userDb == null){
      return ResponseEntity.status(401).body("hittar inte användarnamnet")
    }

    if (!encoder.matches(loginUser.getPassword(),userDb.getPassword())){
      return ResponseEntity.status(401).body("fel lösen");
    }
    return ResponseEntity.ok(userDb);
  }
}
