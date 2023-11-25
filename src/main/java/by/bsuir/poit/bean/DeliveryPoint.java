package by.bsuir.poit.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Paval Shlyk
 * @since 23/10/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPoint {
private long id;
private String cityCode;
private String streetName;
private String houseNumber;
private String name;
}
