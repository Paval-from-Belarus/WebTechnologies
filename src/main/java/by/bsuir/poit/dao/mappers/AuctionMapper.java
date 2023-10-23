package by.bsuir.poit.dao.mappers;

import by.bsuir.poit.dao.entities.Auction;
import by.bsuir.poit.dao.entities.BlindAuction;
import by.bsuir.poit.dao.entities.BlitzAuction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Paval Shlyk
 * @since 23/10/2023
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface AuctionMapper {
AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);

BlindAuction updateBlindWithParent(@MappingTarget BlindAuction auction, Auction parent);

BlitzAuction updateBlitzWithParent(@MappingTarget BlitzAuction auction, Auction parent);

default Auction fromResultSet(ResultSet set) throws SQLException {
      return Auction.builder()
		 .id(set.getLong("auction_id"))
		 .eventDate(set.getDate("event_date"))
		 .lastRegisterDate(set.getDate("last_register_date"))
		 .priceStep(set.getDouble("price_step"))
		 .auctionTypeId(set.getLong("type_id"))
		 .duration(set.getTimestamp("duration"))
		 .membersLimit(set.getInt("members_limit"))
		 .build();
}

default BlindAuction blindFromResultSet(ResultSet set) throws SQLException {
      Auction auction = fromResultSet(set);
      BlindAuction blindAuction = BlindAuction.builder()
				      .timeout(set.getTimestamp("timeout"))
				      .betLimit(set.getInt("bet_limit"))
				      .build();
      return updateBlindWithParent(blindAuction, auction);
}

default BlitzAuction blitzFromResultSet(ResultSet set) throws SQLException {
      Auction auction = fromResultSet(set);
      BlitzAuction blitzAuction = BlitzAuction.builder()
				      .iterationLimit(set.getTimestamp("iteration_time"))
				      .memberExcludeLimit(set.getInt("members_exclude_limit"))
				      .build();
      return updateBlitzWithParent(blitzAuction, auction);
}
}
