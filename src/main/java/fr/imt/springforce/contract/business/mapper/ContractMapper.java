package fr.imt.springforce.contract.business.mapper;

import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.contract.business.model.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    ContractDetails toDto(Contract contract);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "actualReturnDate", ignore = true)
    Contract toEntity(ContractDetails dto);
}
