package com.example.appliances.specification.filial;

import com.example.appliances.entity.Filial;
import com.example.appliances.specification.SearchOperation;
import com.example.appliances.specification.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class FilialSpecification extends BaseSpecification<Filial> {
    protected List<FilialSpecification> values = new ArrayList<>();

    public FilialSpecification(SpecSearchCriterias criteria) {
        super(criteria);
    }

    public FilialSpecification() {
        super();
    }

    @Override
    public <L extends BaseSpecification<Filial>> L update(L specification) {
        this.values.add((FilialSpecification) specification);
        return (L) this;
    }

    @Override
    public Predicate toPredicate(Root<Filial> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case IN:
                return root.get(criteria.getKey()).in(criteria.getValue());
            default:
                return null;
        }
    }

    public FilialSpecification findByName(String name) {
        if (name == null)
            return update(new FilialSpecification());

        String key = "name";
        return update(new FilialSpecification(new SpecSearchCriterias(key, SearchOperations.HAVE, name)));
    }

    public FilialSpecification findBySurname(String surname) {
        if (surname == null)
            return update(new FilialSpecification());

        String key = "surname";
        return update(new FilialSpecification(new SpecSearchCriterias(key, SearchOperations.HAVE, surname)));
    }

    public FilialSpecification findByPatronymic(String patronymic) {
        if (patronymic == null)
            return update(new FilialSpecification());

        String key = "patronymic";
        return update(new FilialSpecification(new SpecSearchCriterias(key, SearchOperations.HAVE, patronymic)));
    }

    public FilialSpecification findByPin(String pin) {
        if (pin == null)
            return update(new FilialSpecification());

        String key = "pin";
        return update(new FilialSpecification(new SpecSearchCriterias(key, SearchOperations.HAVE, pin)));
    }

    public FilialSpecification findByStatusId(Long statusId) {
        if (statusId == null)
            return update(new FilialSpecification());

        String key = "status";
        return update(new FilialSpecification(new SpecSearchCriterias(key, SearchOperations.EQUALITY, statusId)));
    }

    public FilialSpecification findByOrganisationId(Long organisationId) {
        if (organisationId == null)
            return update(new FilialSpecification());

        String key = "equipment.organisation.id";
        return update(new FilialSpecification(new SpecSearchCriterias(key, SearchOperations.EQUALITY, organisationId)));
    }


}
