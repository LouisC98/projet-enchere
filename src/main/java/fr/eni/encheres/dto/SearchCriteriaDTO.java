package fr.eni.encheres.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaDTO {
    private Long noCategorie;
    private String searchName;
    private String mode;
    private List<String> achats;
    private List<String> ventes;
}