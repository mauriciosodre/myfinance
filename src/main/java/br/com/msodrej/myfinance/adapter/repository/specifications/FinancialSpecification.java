package br.com.msodrej.myfinance.adapter.repository.specifications;

import br.com.msodrej.myfinance.adapter.repository.entity.FinancialEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class FinancialSpecification {

  private FinancialSpecification() {}

  public static Specification<FinancialEntity> hasName(String name) {
    return (root, query, criteriaBuilder) -> 
        name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }

  public static Specification<FinancialEntity> hasOwnerId(UUID ownerId) {
    return (root, query, criteriaBuilder) -> 
        ownerId == null ? null : criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
  }

  public static Specification<FinancialEntity> isSharedWith(UUID userId) {
    return (root, query, criteriaBuilder) -> {
      if (userId == null) {
        return null;
      }
      Join<Object, Object> sharedWith = root.join("sharedWith");
      return criteriaBuilder.equal(sharedWith.get("id"), userId);
    };
  }
}