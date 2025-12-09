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
  public UserController(UserRepository repo){ this.repo = repo; }

  @GetMapping
  public List<User> all(){ return repo.findAll(); }

  @PostMapping
  public User create(@RequestBody User u){ return repo.save(u); }
}
