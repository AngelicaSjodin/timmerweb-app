package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.demo.UserRepository;
import com.example.demo.User;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserRepository repo;
  public UserController(UserRepository repo){ this.repo = repo; }

  @GetMapping
  public List<User> all(){ return repo.findAll(); }

  @PostMapping
  public User create(@RequestBody User u){ return repo.save(u); }
}
