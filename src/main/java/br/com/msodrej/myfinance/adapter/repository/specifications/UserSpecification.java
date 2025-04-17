package br.com.msodrej.myfinance.adapter.repository.specifications;

import br.com.msodrej.myfinance.adapter.repository.entity.UserEntity;
import br.com.msodrej.myfinance.domain.model.User;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {

  private UserSpecification() {}

  public static Specification<UserEntity> filter(User filter) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getName() != null) {
        predicates.add(cb.like(cb.lower(root.get("name")),
            "%" + filter.getName().toLowerCase() + "%"));
      }

      if (filter.getEmail() != null) {
        predicates.add(cb.like(cb.lower(root.get("email")),
            "%" + filter.getEmail().toLowerCase() + "%"));
      }

      if (filter.getPhone() != null) {
        predicates.add(cb.like(root.get("phone"),
            "%" + filter.getPhone() + "%"));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}