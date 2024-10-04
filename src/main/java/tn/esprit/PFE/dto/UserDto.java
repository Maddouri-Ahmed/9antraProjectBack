package tn.esprit.PFE.dto;

import lombok.*;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id ;
    private String username;
    private String email;
}