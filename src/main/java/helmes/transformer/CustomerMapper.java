package helmes.transformer;

import org.springframework.stereotype.Service;

import helmes.model.CustomerDto;
import helmes.persistence.entity.Customer;

@Service
public class CustomerMapper {

    public Customer mapToEntity(CustomerDto dto, Customer entity) {
        if (dto != null) {
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setAgree(dto.isAgree());
        }
        return entity;
    }

    public CustomerDto mapToDto(Customer entity) {
        return entity == null ?
                new CustomerDto() :
                CustomerDto.builder().id(entity.getId()).name(entity.getName()).agree(entity.isAgree()).build();
    }

}
