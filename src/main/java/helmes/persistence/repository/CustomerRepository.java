package helmes.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import helmes.persistence.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}