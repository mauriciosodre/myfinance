package br.com.msodrej.myfinance.adapter.repository.mapper;

import br.com.msodrej.myfinance.adapter.repository.entity.RoleEntity;
import br.com.msodrej.myfinance.domain.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {

  Role toModel(RoleEntity roleEntity);

  RoleEntity toEntity(Role role);

}
