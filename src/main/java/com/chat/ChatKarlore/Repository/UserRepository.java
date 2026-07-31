package com.chat.ChatKarlore.Repository;

import com.chat.ChatKarlore.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);           // login time email se user khojna
    boolean existsByEmail(String email);                //register time check karega email exist ha ya nahi
}
