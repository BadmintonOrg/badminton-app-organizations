package si.fri.rso.badmintonapporganizations.models.converters;

import si.fri.rso.badmintonapporganizations.lib.Organization;
import si.fri.rso.badmintonapporganizations.models.entities.OrganizationEntity;

public class OrganizationConverter {

    public static Organization toDto(OrganizationEntity entity) {

        Organization dto = new Organization();
        dto.setOrganizationId(entity.getId());
        dto.setName(entity.getName());

        return dto;

    }

    public static OrganizationEntity toEntity(Organization dto) {

        OrganizationEntity entity = new OrganizationEntity();
        entity.setName(dto.getName());
        entity.setId(dto.getOrganizationId());
        return entity;

    }

}
