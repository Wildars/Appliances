package com.example.appliances.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageRequest {

    @JsonProperty("imageName")
    String imageName;

    @JsonProperty("imageData")
    String imageData;

    @JsonProperty("imageExtension")
    String imageExtension;


}
