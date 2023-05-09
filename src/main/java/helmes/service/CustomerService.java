package helmes.service;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toSet;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.google.common.collect.Sets;

import helmes.model.CustomerDto;
import helmes.persistence.entity.Customer;
import helmes.persistence.entity.Sector;
import helmes.persistence.repository.CustomerRepository;
import helmes.transformer.CustomerMapper;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SectorService sectorService;
    private final CustomerMapper customerMapper;

    public CustomerDto save(CustomerDto customerDto) {
        Customer customer = customerMapper.mapToEntity(customerDto, findOrInit(customerDto));
        customer.setSectors(Sets.newHashSet(sectorService.findById(newArrayList(customerDto.getSectors()))));

        Customer persisted = customerRepository.save(customer);
        customerDto = customerMapper.mapToDto(persisted);
        customerDto.setSectors(persisted.getSectors().stream().map(Sector::getId).collect(toSet()));
        return customerDto;
    }

    private Customer findOrInit(CustomerDto customerDto) {
        Customer customer = new Customer();
        if (customerDto.getId() != null) {
            customer = customerRepository.findById(customerDto.getId()).orElseThrow();
        }
        return customer;
    }

}
