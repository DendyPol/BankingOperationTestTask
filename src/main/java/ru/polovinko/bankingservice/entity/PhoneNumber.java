package ru.polovinko.bankingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone_number")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneNumber {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;
}
