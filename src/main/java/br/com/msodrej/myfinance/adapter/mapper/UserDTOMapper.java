package br.com.msodrej.myfinance.adapter.mapper;

import br.com.msodrej.myfinance.adapter.dto.user.NewUserDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserFilterDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserResponseDTO;
import br.com.msodrej.myfinance.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

  User toModel(NewUserDTO dto);

  User toModel(UserPayloadDTO dto);

  User toModel(UserFilterDTO dto);

  UserResponseDTO toDTO(User user);

}
