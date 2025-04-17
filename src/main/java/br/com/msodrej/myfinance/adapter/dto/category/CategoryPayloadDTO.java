package br.com.msodrej.myfinance.adapter.dto.category;

public record CategoryPayloadDTO(
    Long id,
    String name,
    String description,
    String color
) {

}
