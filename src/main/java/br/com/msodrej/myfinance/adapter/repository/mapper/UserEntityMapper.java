package br.com.msodrej.myfinance.adapter.repository.mapper;

import br.com.msodrej.myfinance.adapter.repository.entity.UserEntity;
import br.com.msodrej.myfinance.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  User toModel(UserEntity userEntity);

  UserEntity toEntity(User user);

}
