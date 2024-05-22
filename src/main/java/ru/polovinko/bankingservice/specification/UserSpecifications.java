package ru.polovinko.bankingservice.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.bankingservice.entity.User;

import java.time.LocalDate;

public class UserSpecifications {
  public static Specification<User> dateOfBirthAfter(LocalDate dateOfBirth) {
    return (root, query, criteriaBuilder) -> {
      if (dateOfBirth == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth);
    };
  }

  public static Specification<User> hasPhoneNumber(String phoneNumber) {
    return (root, query, criteriaBuilder) -> {
      if (phoneNumber == null || phoneNumber.isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.join("phoneNumbers").get("phoneNumber"), phoneNumber);
    };
  }

  public static Specification<User> hasFullNameLike(String fullName) {
    return (root, query, criteriaBuilder) -> {
      if (fullName == null || fullName.isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(root.get("username"), fullName + "%");
    };
  }

  public static Specification<User> hasEmail(String email) {
    return (root, query, criteriaBuilder) -> {
      if (email == null || email.isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.join("emails").get("email"), email);
    };
  }
}
