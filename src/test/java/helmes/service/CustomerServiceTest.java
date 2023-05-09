package helmes.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Sets;

import helmes.model.CustomerDto;
import helmes.persistence.entity.Customer;
import helmes.persistence.entity.Sector;
import helmes.persistence.repository.CustomerRepository;
import helmes.transformer.CustomerMapper;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SectorService sectorService;
    @Mock
    private CustomerMapper customerMapper;

    private Integer sectorId = 123;
    private Sector sector;

    @BeforeEach
    void setup() {
        sector = Sector.builder().id(sectorId).name("The Sector").build();
    }

    @Test
    void shouldSaveNewCustomer() {
        Customer customer = Customer.builder().name("Customer Name").build();
        CustomerDto customerDto = CustomerDto.builder().name("Customer Name").sectors(Sets.newHashSet(sectorId)).build();

        commonMocks(customer, customerDto);
        given(customerMapper.mapToEntity(eq(customerDto), any(Customer.class)))
                .willReturn(customer);

        customerService.save(customerDto);

        verify(customerRepository, never()).findById(any());
        verify(customerMapper).mapToEntity(eq(customerDto), any(Customer.class));
        verify(customerRepository).save(customer);
        verify(customerMapper).mapToDto(customer);
    }

    @Test
    void shouldSaveExistingCustomer() {
        int customerId = 1;
        Customer customer = Customer.builder().id(customerId).name("Customer Name").sectors(Sets.newHashSet(sector)).build();
        CustomerDto customerDto = CustomerDto.builder().id(customerId).name("Customer Name").sectors(Sets.newHashSet(sectorId)).build();

        commonMocks(customer, customerDto);
        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(customer));
        given(customerMapper.mapToEntity(eq(customerDto), any(Customer.class)))
                .willReturn(customer);

        customerService.save(customerDto);

        verify(customerRepository).findById(customerId);
        verify(customerMapper).mapToEntity(eq(customerDto), any(Customer.class));
        verify(customerRepository).save(customer);
        verify(customerMapper).mapToDto(customer);
    }

    @Test
    void throwExceptionWhenCustomerIsNotFound() {
        int customerId = 1;
        CustomerDto customerDto = CustomerDto.builder().id(customerId).name("Customer Name").sectors(Sets.newHashSet(sectorId)).build();

        given(customerRepository.findById(1))
                .willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            customerService.save(customerDto);
        });
    }

    private void commonMocks(Customer customer, CustomerDto customerDto) {
        given(customerRepository.save(customer))
                .willReturn(customer);
        given(customerMapper.mapToDto(customer))
                .willReturn(customerDto);
        given(sectorService.findById(newArrayList(sectorId)))
                .willReturn(newArrayList(sector));
    }
}
