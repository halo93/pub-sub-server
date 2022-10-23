package com.d7001d.pubsubserver.repository;

import com.d7001d.pubsubserver.model.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends CrudRepository<Data, String> {
    boolean existsByKey(String key);
    List<Data> findAllByKey(String key);
}
