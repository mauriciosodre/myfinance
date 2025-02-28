package br.com.msodrej.myfinance.adapter.mapper;

import br.com.msodrej.myfinance.adapter.dto.role.NewRoleDTO;
import br.com.msodrej.myfinance.adapter.dto.role.RolePayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.role.RoleResponseDTO;
import br.com.msodrej.myfinance.domain.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleDTOMapper {

  @Mapping(target = "id", ignore = true)
  Role toModel(NewRoleDTO dto);

  Role toModel(RolePayloadDTO dto);

  RoleResponseDTO toDTO(Role role);

}
