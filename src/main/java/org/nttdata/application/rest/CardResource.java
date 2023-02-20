package org.nttdata.application.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.nttdata.btask.interfaces.CardService;
import org.nttdata.domain.models.CardDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/cards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CardResource {
  private final CardService cardService;

  public CardResource(CardService cardService) {
    this.cardService = cardService;
  }


  @GET
  public Multi<CardDto> findAll() {
    return cardService.list();
  }

  @POST
  public Uni<CardDto> add(CardDto cardDto) {
    return cardService.addCard(cardDto);
  }

  @PUT
  public Uni<CardDto> updateCustomer(CardDto cardDto) {
    return cardService.updateCard(cardDto);
  }

  @POST
  @Path("/search")
  public Uni<CardDto> findByNroDocument(CardDto cardDto) {
    return cardService.findByNroDocument(cardDto);
  }

  @DELETE
  public Uni<CardDto> deleteCustomer(CardDto cardDto) {
    return cardService.deleteCard(cardDto);
  }

}
