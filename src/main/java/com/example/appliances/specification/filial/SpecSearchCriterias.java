package com.example.appliances.specification.filial;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriterias {
    private String key;
    private SearchOperations operation;
    private Object value;

    public SpecSearchCriterias(final String key, final SearchOperations operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public static SpecSearchCriterias all() {
        return new SpecSearchCriterias(null, SearchOperations.ALL, null);
    }

    public static SpecSearchCriterias equal(String key, final Object value) {
        return new SpecSearchCriterias(key, SearchOperations.EQUALITY, value);
    }

    public static SpecSearchCriterias notNull(String key) {
        return new SpecSearchCriterias(key, SearchOperations.NOT_NULL, null);
    }
}
