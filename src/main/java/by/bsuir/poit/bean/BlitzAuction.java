package by.bsuir.poit.bean;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BlitzAuction extends Auction {
@NotNull
private Timestamp iterationLimit;
private Integer memberExcludeLimit;
}
