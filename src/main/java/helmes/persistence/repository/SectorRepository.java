package helmes.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import helmes.persistence.entity.Sector;

public interface SectorRepository extends CrudRepository<Sector, Integer> {

    List<Sector> findAllByParentIsNull();

}