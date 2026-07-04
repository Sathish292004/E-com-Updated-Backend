package Sathish292004.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminProfileResponse {

    private String name;
    private String email;
    private String role;
}