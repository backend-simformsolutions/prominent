package com.prominent.title.utility;

import com.prominent.title.entity.resource.GeneralAddress;
import com.prominent.title.entity.user.Organization;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

public class EntitySpecifications {
    private static final String IS_DEFAULT = "isDefault";

    private EntitySpecifications() {
    }

    /**
     * This method used to search organization name with search term.
     *
     * @param searchTerm searchTerm
     * @return {@link Specification}
     * @see Specification
     * @see Organization
     */
    public static Specification<Organization> searchOrganizationName(String searchTerm) {
        return (root, query, builder) -> {
            Join<GeneralAddress, Organization> addressOrganizationJoin = root.join("addressList", JoinType.LEFT);
            if (searchTerm != null) {
                Predicate predicate;
                predicate = builder.and(builder.or(builder.isTrue(addressOrganizationJoin.get(IS_DEFAULT)), addressOrganizationJoin.isNull()), builder.or(
                        builder.like(builder.lower(root.get("organizationName")), "%" + searchTerm.toLowerCase() + "%")));
                return predicate;
            } else
                return builder.or(builder.isTrue(addressOrganizationJoin.get(IS_DEFAULT)), addressOrganizationJoin.isNull());
        };
    }
}