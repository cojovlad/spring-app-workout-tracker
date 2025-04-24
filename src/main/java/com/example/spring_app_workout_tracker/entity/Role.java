package com.example.spring_app_workout_tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class Role implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @ManyToMany(mappedBy = "roles")
    List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
