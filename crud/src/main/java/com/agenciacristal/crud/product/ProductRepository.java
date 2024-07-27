package com.agenciacristal.crud.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // @Query("select * from Product where name = ?") // se puede hacer esto pero no se cmo
    Optional<Product> findProductByName(String name);
    /*@Query(
            value = "SELECT * FROM Product u WHERE u.price > 0",
            nativeQuery = true)*/
    @Query(value="SELECT * FROM Product u", nativeQuery = true)
    List<Product> findAllActiveProductsNative();
}
