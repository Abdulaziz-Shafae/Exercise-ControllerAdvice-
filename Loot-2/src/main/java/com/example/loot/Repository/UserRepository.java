package com.example.loot.Repository;

import com.example.loot.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    User findUserById(Integer id);
    User findUserByEmail(String email);
    User findUserByEmailAndPassword(String email , String password);


}
