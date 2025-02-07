package br.com.msodrej.myfinance.adapter.controller;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import br.com.msodrej.myfinance.adapter.dto.user.UserPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.user.NewUserDTO;
import br.com.msodrej.myfinance.adapter.mapper.UserDTOMapper;
import br.com.msodrej.myfinance.domain.usecase.UserUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserUseCase useCase;
  private final UserDTOMapper mapper;

  @PostMapping
  @ResponseStatus(CREATED)
  public UserResponseDTO save(@RequestBody NewUserDTO user) {
    return mapper.toDTO(useCase.save(mapper.toModel(user)));
  }

  @PutMapping
  @ResponseStatus(OK)
  public UserResponseDTO update(@RequestBody UserPayloadDTO user) {
    return mapper.toDTO(useCase.update(mapper.toModel(user)));
  }

  @DeleteMapping
  @ResponseStatus(NO_CONTENT)
  public void deleteById(UUID id) {
    useCase.deleteById(id);
  }

  @PatchMapping("/{id}/activate")
  @ResponseStatus(NO_CONTENT)
  public void activate(@PathVariable UUID id) {
    useCase.activate(id);
  }

  @GetMapping("/{id}")
  @ResponseStatus(OK)
  public UserResponseDTO findById(@PathVariable UUID id) {
    return mapper.toDTO(useCase.findById(id));
  }

  @GetMapping
  @ResponseStatus(OK)
  public Page<UserResponseDTO> findAll(Pageable pageable) {
    return useCase.findAll(pageable).map(mapper::toDTO);
  }

}
