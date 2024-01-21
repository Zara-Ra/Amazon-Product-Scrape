package com.favoland.data;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AmazonProduct {
    String productName;
    String brand;
    String cost;
    String description;
    String ASIN;
    String countryOfOrigin;
    String company;
    String UPC;
    String URL;
}
