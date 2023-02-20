package org.nttdata.infraestructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.quarkus.mongodb.reactive.ReactiveMongoDatabase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.nttdata.domain.contract.CardRepository;
import org.nttdata.domain.models.CardDto;
import org.nttdata.infraestructure.entity.Card;

import javax.enterprise.context.ApplicationScoped;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@ApplicationScoped
public class CardRepositoryImpl implements CardRepository {
  private final ReactiveMongoClient reactiveMongoClient;

  public CardRepositoryImpl(ReactiveMongoClient reactiveMongoClient) {
    this.reactiveMongoClient = reactiveMongoClient;
  }

  @Override
  public Multi<CardDto> list() {
    return getCollection().find().map(doc->{
      CardDto cardDto = new CardDto();
      cardDto.setId(doc.getString("id"));
      cardDto.setTypeCard(doc.getInteger("typeCard"));
      cardDto.setNumberCard(doc.getString("numberCard"));
      cardDto.setNumberAccountAssociated(doc.getString("numberAccountAssociated"));
      cardDto.setPin(doc.getString("pin"));
      cardDto.setDueDate(doc.getString("dueDate"));
      cardDto.setCodeValidation(doc.getString("codeValidation"));
      cardDto.setCreated_datetime(doc.getString("created_datetime"));
      cardDto.setUpdated_datetime(doc.getString("updated_datetime"));
      cardDto.setActive(doc.getString("active"));
      return cardDto;
    }).filter(customer->{
      return customer.getActive().equals("S");
    });
  }

  @Override
  public Uni<CardDto> findByNroDocument(CardDto cardDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("cards");
    ReactiveMongoCollection<Document> collection = database.getCollection("card");

    Bson filter = combine(
        eq("numberCard", cardDto.getNumberCard()),
        eq("dueDate", cardDto.getDueDate()),
        eq("codeValidation", cardDto.getCodeValidation())

    );

    return collection.find(filter).onItem().transform(doc -> {
      CardDto card = new CardDto();
      card.setTypeCard(doc.getInteger("typeCard"));
      card.setNumberCard(doc.getString("numberCard"));
      card.setNumberAccountAssociated(doc.getString("numberAccountAssociated"));
      card.setPin(doc.getString("pin"));
      card.setDueDate(doc.getString("dueDate"));
      card.setCodeValidation(doc.getString("codeValidation"));
      card.setCreated_datetime(doc.getString("created_datetime"));
      card.setUpdated_datetime(doc.getString("updated_datetime"));
      card.setActive(doc.getString("active"));
      return card;
    }).toUni();
  }

  @Override
  public Uni<CardDto> addCard(CardDto cardDto) {
    Document document = new Document()
        .append("typeCard", cardDto.getTypeCard())
        .append("numberCard", cardDto.getNumberCard())
        .append("numberAccountAssociated", cardDto.getNumberAccountAssociated())
        .append("pin", cardDto.getPin())
        .append("dueDate", cardDto.getDueDate())
        .append("codeValidation", cardDto.getCodeValidation())
        .append("created_datetime", this.getDateNow())
        .append("updated_datetime", this.getDateNow())
        .append("active", "S");


    return getCollection().insertOne(document).replaceWith(cardDto);
  }

  @Override
  public Uni<CardDto> updateCard(CardDto cardDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("cards");
    ReactiveMongoCollection<Document> collection = database.getCollection("card");

    Bson filter = eq("numberCard", cardDto.getNumberCard());
    Bson updates = combine(
        set("typeCard", cardDto.getTypeCard()),
        set("numberAccountAssociated", cardDto.getNumberAccountAssociated()),
        set("pin", cardDto.getPin()),
        set("dueDate", cardDto.getDueDate()),
        set("codeValidation", cardDto.getCodeValidation()),
        set("updated_datetime", this.getDateNow()));
    return collection.updateOne(filter,updates).replaceWith(cardDto);
  }

  @Override
  public Uni<CardDto> deleteCard(CardDto cardDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("cards");
    ReactiveMongoCollection<Document> collection = database.getCollection("card");

    Bson filter = eq("numberCard", cardDto.getNumberCard());
    Bson updates = combine(set("updated_datetime", cardDto.getUpdated_datetime()), set("active", "N"));

    return collection.updateOne(filter,updates).replaceWith(cardDto);
  }

  private static String getDateNow(){
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date).toString();
  }

  public static Card mapToEntity(Object customerDto) {
    return new ObjectMapper().convertValue(customerDto, Card.class);
  }
  public static CardDto mapToDto(Object customerDto) {
    return new ObjectMapper().convertValue(customerDto, CardDto.class);
  }

  public static CardDto mapToDomain(Card entity) {
    return new ObjectMapper().convertValue(entity, CardDto.class);
  }

  private ReactiveMongoCollection<Document> getCollection() {
    return reactiveMongoClient.getDatabase("cards").getCollection("card");
  }
}
