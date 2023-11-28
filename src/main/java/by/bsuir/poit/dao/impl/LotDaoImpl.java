package by.bsuir.poit.dao.impl;

import by.bsuir.poit.context.Repository;
import by.bsuir.poit.dao.LotDao;
import by.bsuir.poit.dao.connections.ConnectionPool;
import by.bsuir.poit.dto.EnglishLot;
import by.bsuir.poit.dto.Lot;
import by.bsuir.poit.dto.mappers.LotMapper;
import by.bsuir.poit.dao.exception.DataAccessException;
import by.bsuir.poit.dao.exception.DataModifyingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Paval Shlyk
 * @since 23/10/2023
 */
@RequiredArgsConstructor
@Repository
public class LotDaoImpl extends AbstractDao implements LotDao {
private static final Logger LOGGER = LogManager.getLogger(LotDaoImpl.class);
private final ConnectionPool pool;
private final LotMapper mapper;

@Override
public Optional<EnglishLot> findEnglishLotById(long id) {
      Optional<EnglishLot> englishLot;
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from AUCTION_TYPE_ENGLISH_LOT where LOT_ID = ?")) {
	    statement.setLong(1, id);
	    englishLot = fetchEntityAndClose(statement, mapper::fromResultSetEnglish);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
      return englishLot;
}

@Override
public Optional<Lot> findByIdAndStatus(long id, short status) {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from LOT where  LOT_ID = ? and STATUS = ?")) {
	    statement.setLong(1, id);
	    statement.setShort(2, status);
	    return fetchEntityAndClose(statement, mapper);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }


}

@Override
public Optional<Lot> findById(long id) {
      Optional<Lot> lot;
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from LOT where LOT_ID = ?")) {
	    statement.setLong(1, id);
	    lot = fetchEntityAndClose(statement, mapper);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
      return lot;
}

@Override
public List<Lot> findAllByAuctionId(long auctionId) {
      List<Lot> lots;
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from LOT where AUCTION_ID = ?")) {
	    statement.setLong(1, auctionId);
	    lots = fetchListAndClose(statement, mapper);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
      return lots;
}

@Override
public List<Lot> findAllBySellerId(long sellerId) {
      List<Lot> lots;
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from LOT where CLIENT_SELLER_ID = ?")) {
	    statement.setLong(1, sellerId);
	    lots = fetchListAndClose(statement, mapper);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
      return lots;
}

@Override
public List<Lot> findAllByStatus(short status) {
      List<Lot> lots;
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from LOT where status = ?")) {
	    statement.setShort(1, status);
	    lots = fetchListAndClose(statement, mapper);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
      return lots;
}

@Override
public List<Lot> findAllByStatusOrderByStartingPriceDesc(short status) {
      List<Lot> lots;
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from LOT where STATUS = ?" +
									 "ORDER BY START_PRICE DESC ")) {
	    statement.setShort(1, status);
	    lots = fetchListAndClose(statement, mapper);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
      return lots;

}

@Override
public List<Lot> findAllByCustomerId(long customerId) {
      List<Lot> lots;
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("select * from LOT where CLIENT_CUSTOMER_ID = ?")) {
	    statement.setLong(1, customerId);
	    lots = fetchListAndClose(statement, mapper);
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
      return lots;
}

@Override
public Lot save(Lot lot) throws DataModifyingException {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("insert into LOT " +
									 "(TITLE, START_PRICE, ACTUAL_PRICE, AUCTION_TYPE_ID, STATUS, CLIENT_SELLER_ID, CLIENT_CUSTOMER_ID, DELIVERY_POINT_ID, AUCTION_ID) " +
									 "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
	    statement.setString(1, lot.getTitle());
	    statement.setDouble(2, lot.getStartPrice());
	    if (lot.getActualPrice() == null) {
		  statement.setNull(3, Types.DECIMAL);
	    } else {
		  statement.setDouble(3, lot.getActualPrice());
	    }
	    statement.setLong(4, lot.getAuctionTypeId());
	    statement.setShort(5, lot.getStatus());
	    statement.setLong(6, lot.getSellerId());
	    if (lot.getCustomerId() == null) {
		  statement.setNull(7, Types.BIGINT);
	    } else {
		  statement.setLong(7, lot.getCustomerId());
	    }
	    if (lot.getDeliveryPointId() == null) {
		  statement.setNull(8, Types.BIGINT);
	    } else {
		  statement.setLong(8, lot.getDeliveryPointId());
	    }
	    if (lot.getAuctionId() == null) {
		  statement.setNull(9, Types.BIGINT);
	    } else {
		  statement.setLong(9, lot.getAuctionId());
	    }
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to insert lot entity into table: %s", lot);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);
	    }
	    Long lotId = fetchLongKeyAndClose(statement).orElseThrow(() -> {
		  final String msg = String.format("Failed to fetch generated keys from lot table: %s", lot);
		  LOGGER.error(msg);
		  return new DataModifyingException(msg);
	    });
	    lot.setId(lotId);
      } catch (SQLException e) {
	    LOGGER.error("Failed to insert new lot message {} and stack-trace {}", e.toString(), Arrays.toString(e.getStackTrace()));
	    throw new DataAccessException(e);
      }
      return lot;

}

/**
 * This method assign provided status and set auctionId to one.
 * The current auctionId should be null.
 * Otherwise, method falls
 *
 * @param lotId     the id of an existing lot
 * @param status    new status of a lot to be assigned
 * @param auctionId auctionId for which lot will be assigned
 */
@Override
public void assignLotWithStatusToAuction(long lotId, short status, long auctionId) {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("update LOT SET STATUS = ?, AUCTION_ID = ? where LOT_ID = ? and AUCTION_ID IS NULL")) {
	    statement.setShort(1, status);
	    statement.setLong(2, auctionId);
	    statement.setLong(3, lotId);
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to update lot with id=%d for given auction_id=%d", lotId, auctionId);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);
	    }
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
}

@Override
public void delete(long lotId) throws DataAccessException, DataModifyingException {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("delete from LOT where LOT_ID = ? and AUCTION_ID IS NULL")) {
	    statement.setLong(1, lotId);
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to delete lot by id=%d", lotId);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);
	    }
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
}

@Override
public void setAuctionId(long lotId, long auctionId) throws DataAccessException {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("update LOT set AUCTION_ID = ? where LOT_ID = ?")) {
	    statement.setLong(1, auctionId);
	    statement.setLong(2, lotId);
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to update lot with id=%d for given auction_id=%d", lotId, auctionId);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);
	    }
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
}

@Override
public void setLotStatus(long lotId, short status) throws DataAccessException {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("update LOT set STATUS = ? where LOT_ID = ?")) {
	    statement.setShort(1, status);
	    statement.setLong(2, lotId);
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to update lot with id=%d for given status =%d", lotId, status);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);
	    }
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
}

@Override
public void setCustomerId(long lotId, long customerId) throws DataAccessException {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("update LOT set CLIENT_CUSTOMER_ID = ? where LOT_ID = ?")) {
	    statement.setLong(1, customerId);
	    statement.setLong(2, lotId);
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to update lot with id=%d for given customer_id=%d", lotId, customerId);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);

	    }
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
}

@Override
public void setActualPrice(long lotId, double auctionPrice) throws DataAccessException {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("update LOT set ACTUAL_PRICE = ? where LOT_ID = ?")) {
	    statement.setDouble(1, auctionPrice);
	    statement.setLong(2, lotId);
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to update lot with id=%d to auctionPrice=%f", lotId, auctionPrice);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);
	    }
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
}

@Override
public void setDeliveryPointId(long lotId, long deliveryPointId) throws DataAccessException {
      try (Connection connection = pool.getConnection();
	   PreparedStatement statement = connection.prepareStatement("update LOT SET DELIVERY_POINT_ID = ? where LOT_ID = ?")) {
	    statement.setLong(1, deliveryPointId);
	    statement.setLong(2, lotId);
	    if (statement.executeUpdate() != 1) {
		  final String msg = String.format("Failed to update lot with id=%d for given delivery_point_id=%d", lotId, deliveryPointId);
		  LOGGER.error(msg);
		  throw new DataModifyingException(msg);
	    }
      } catch (SQLException e) {
	    LOGGER.error(e);
	    throw new DataAccessException(e);
      }
}


}
