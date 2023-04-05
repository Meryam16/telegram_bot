package com.example.damnedtgbot.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

//@Entity
@Data
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long chatId;
    private  String  tripType;
    private String dateToEnd;
    private String dateToStart;
    private String whereToGo;
    private String howManyPeople;
    private String offerType;
    private String abroadOrCountry;
    private String desc;
}
