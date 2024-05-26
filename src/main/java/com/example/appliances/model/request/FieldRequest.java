package com.example.appliances.model.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldRequest {
    private String name;
    private String length;
    private Boolean fireResistant;
    private String width;
    private String thickness;
    private String manufacturerArticle;
    private Integer code;
    private String vidKromki;
    private String objectPrimeneniya;
    private String typePrimeneniya;

    private Long categoryId;
}