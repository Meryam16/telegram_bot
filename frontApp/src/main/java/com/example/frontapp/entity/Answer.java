package com.example.frontapp.entity;


import jakarta.persistence.*;
import lombok.*;
import org.postgresql.largeobject.LargeObject;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@Entity
public class Answer {

    public String id;

    public String text;




}
