package br.com.msodrej.myfinance.adapter.dto.category;

public record CategoryResponseDTO(
    Long id,
    String name,
    String description,
    String color,
    Boolean enabled
) {

}
