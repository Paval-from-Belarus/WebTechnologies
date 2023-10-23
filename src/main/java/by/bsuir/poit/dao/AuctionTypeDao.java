package by.bsuir.poit.dao;

import by.bsuir.poit.connections.ConnectionPool;
import by.bsuir.poit.dao.entities.AuctionType;
import by.bsuir.poit.dao.mappers.AuctionTypeMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * @author Paval Shlyk
 * @since 23/10/2023
 */
@RequiredArgsConstructor
public class AuctionTypeDao {
private final ConnectionPool pool;
private final AuctionTypeMapper mapper;
Optional<AuctionType> findById(long typeId) {
 	return Optional.empty();
}
List<AuctionType> findAll() {
      return List.of();
}
}
