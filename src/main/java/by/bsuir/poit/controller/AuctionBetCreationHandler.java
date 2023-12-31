package by.bsuir.poit.controller;

import by.bsuir.poit.dto.AuctionBetDto;
import by.bsuir.poit.dto.Create;
import by.bsuir.poit.services.AuctionService;
import by.bsuir.poit.services.exception.resources.ResourceNotFoundException;
import by.bsuir.poit.utils.ControllerUtils;
import by.bsuir.poit.utils.PageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Paval Shlyk
 * @since 12/11/2023
 */
@Controller
@RequestMapping("/api/auction/bet/new")
@RequiredArgsConstructor
public class AuctionBetCreationHandler {
private static final Logger LOGGER = LogManager.getLogger(AuctionBetCreationHandler.class);
private final AuctionService auctionService;

@PostMapping
public void accept(
    @RequestPart @Validated(Create.class) AuctionBetDto auctionBet,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception {
//      AuctionBet auctionBet = ParserUtils.parseBet(request);
      auctionBet.setTime(new Timestamp(new Date().getTime()));
      try {
	    auctionService.saveBet(request.getUserPrincipal(), auctionBet);
      } catch (ResourceNotFoundException e) {
	    Principal principal = request.getUserPrincipal();
	    final String msg = String.format("Failed to save bet for user=%s with size=%f", principal.getName(), auctionBet.getBet());
	    LOGGER.error(msg);
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      PageUtils.redirectTo(response, ControllerUtils.AUCTION_ENDPOINT + "?auction_id=" + auctionBet.getAuctionId());
}
}
