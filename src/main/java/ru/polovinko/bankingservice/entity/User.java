package ru.polovinko.bankingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "app_client")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "username", unique = true, nullable = false)
  private String username;
  @Column(name = "password", nullable = false)
  private String password;
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private BankAccount bankAccount;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PhoneNumber> phoneNumbers = new HashSet<>();
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Email> emails = new HashSet<>();
  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;
  @ElementCollection(targetClass = RoleType.class, fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "roles", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Set<RoleType> roles = new HashSet<>();
}
