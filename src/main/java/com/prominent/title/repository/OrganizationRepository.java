package com.prominent.title.repository;

import com.prominent.title.dto.organization.OrganizationSearchDto;
import com.prominent.title.entity.user.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Integer>, JpaSpecificationExecutor<Organization> {
    boolean existsByOrganizationNameIgnoreCase(String organizationName);

    Organization findByOrganizationNameIgnoreCase(String organizationName);

    @Query("select new com.prominent.title.dto.organization.OrganizationSearchDto(o.organizationId, o.organizationName,o.primaryContactName) from Organization o where lower(o.organizationName) like %?1%")
    List<OrganizationSearchDto> findByOrganizationNameLikeIgnoreCase(String organizationName);
}
