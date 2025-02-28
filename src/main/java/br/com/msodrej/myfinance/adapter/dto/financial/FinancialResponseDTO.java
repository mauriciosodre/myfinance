package br.com.msodrej.myfinance.adapter.dto.financial;

import br.com.msodrej.myfinance.adapter.dto.user.UserResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FinancialResponseDTO(Long id, String name, String description,
                                   UUID ownerId, List<UserResponseDTO> sharedWith,
                                   LocalDateTime createdAt) {

}
