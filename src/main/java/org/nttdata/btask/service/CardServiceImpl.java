package org.nttdata.btask.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.nttdata.btask.interfaces.CardService;
import org.nttdata.domain.contract.CardRepository;
import org.nttdata.domain.models.CardDto;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardServiceImpl implements CardService {
  private final CardRepository cardRepository;

  public CardServiceImpl(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Override
  public Multi<CardDto> list() {
    return cardRepository.list();
  }

  @Override
  public Uni<CardDto> findByNroDocument(CardDto cardDto) {
    return cardRepository.findByNroDocument(cardDto);
  }

  @Override
  public Uni<CardDto> addCard(CardDto cardDto) {
    return cardRepository.addCard(cardDto);
  }

  @Override
  public Uni<CardDto> updateCard(CardDto cardDto) {
    return cardRepository.updateCard(cardDto);
  }

  @Override
  public Uni<CardDto> deleteCard(CardDto cardDto) {
    return cardRepository.deleteCard(cardDto);
  }
}
