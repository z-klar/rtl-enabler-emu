package cz.zk.springmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbtButtonsDTO {
    private boolean AbtButtonHome;
    private boolean AbtButtonMenu;
    private boolean AbtButtonPower;
    private String AbtButtonsMessage1;
    private String MflButtonsMessage1;
    private String AbtMessage;
}
