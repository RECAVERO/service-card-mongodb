package org.nttdata.domain.contract;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.nttdata.domain.models.CardDto;

public interface CardRepository {
  Multi<CardDto> list();

  Uni<CardDto> findByNroDocument(CardDto cardDto);

  Uni<CardDto> addCard(CardDto cardDto);

  Uni<CardDto> updateCard(CardDto cardDto);

  Uni<CardDto> deleteCard(CardDto cardDto);
}
