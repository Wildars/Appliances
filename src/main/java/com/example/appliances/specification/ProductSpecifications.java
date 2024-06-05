package com.example.appliances.specification;

import com.example.appliances.entity.Brand;
import com.example.appliances.entity.ProducingCountry;
import com.example.appliances.entity.Product;
import com.example.appliances.entity.ProductCategory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> withBrandIdsAndProducingCountryIdsAndCategoryIdAndPriceRange(List<Long> brandIds, List<Long> producingCountryIds, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (brandIds != null && !brandIds.isEmpty()) {
                Join<Product, Brand> brandJoin = root.join("brand");
                predicate = builder.and(predicate, brandJoin.get("id").in(brandIds));
            }
            if (producingCountryIds != null && !producingCountryIds.isEmpty()) {
                Join<Product, ProducingCountry> producingCountryJoin = root.join("producingCountry");
                predicate = builder.and(predicate, producingCountryJoin.get("id").in(producingCountryIds));
            }
            if (categoryId != null) {
                Join<Product, ProductCategory> categoryJoin = root.join("categories");
                predicate = builder.and(predicate, builder.equal(categoryJoin.get("id"), categoryId));
            }
            if (minPrice != null && maxPrice != null) {
                predicate = builder.and(predicate, builder.between(root.get("price"), minPrice, maxPrice));
            } else if (minPrice != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("price"), minPrice));
            } else if (maxPrice != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return predicate;
        };
    }
}
