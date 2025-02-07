package br.com.msodrej.myfinance.adapter.repository.repositories;

import br.com.msodrej.myfinance.adapter.repository.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  @Modifying
  @Query("update UserEntity u set u.active = :active where u.id = :id")
  void activateById(UUID id, boolean active);

  Optional<UserEntity> findByUsername(String username);

}
