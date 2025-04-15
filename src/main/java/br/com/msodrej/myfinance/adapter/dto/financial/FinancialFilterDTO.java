package br.com.msodrej.myfinance.adapter.dto.financial;

import java.util.List;
import java.util.UUID;

public record FinancialFilterDTO(
    String name,
    UUID ownerId,
    List<UUID> sharedWithId
) {

}
