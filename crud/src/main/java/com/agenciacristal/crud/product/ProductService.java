package com.agenciacristal.crud.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    HashMap<String, Object> datos;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getProducts() {
        //return this.productRepository.findAll();
        //return this.productRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        return this.productRepository.findAllActiveProductsNative();
        /*Para probar sin conexión a BD */
        /*
        return List.of(
                new Product(2541L,
                        "Laptop MAC",
                        500, LocalDate.of(2025, Month.MARCH, 5),
                        2
                )
        );
        */
        //tengo
    }

    public ResponseEntity<Object> getProduct(Long id) {
        boolean existe = this.productRepository.existsById(id);
        datos = new HashMap<>();
        if (!existe) {
            datos.put("error", true);
            datos.put("message", "El producto no existe");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.NOT_FOUND
            );
        } else {
            Optional<Product> elProducto = productRepository.findById(id);
            return new ResponseEntity<>(
                    elProducto,
                    HttpStatus.ACCEPTED
            );
        }

    }

    public ResponseEntity<Object> newProduct(Product product) {
        Optional<Product> res = productRepository.findProductByName(product.getName());
        datos = new HashMap<>();
        if (res.isPresent() && product.getId() == null) {
            datos.put("error", true);
            datos.put("message", "Ya existe un producto con ese nombre");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
            // throw new IllegalStateException("Ya existe el producto"); //Esto puede ser para el log
        }

        datos.put("message", "Se guardó con éxito");
        if (product.getId() != null) {
            datos.put("message", "Se actualizó con éxito");
        }
        productRepository.save(product);
        datos.put("data", product);
        return new ResponseEntity<>(
                datos,
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<Object> deleteProduct(Long id) {
        boolean existe = this.productRepository.existsById(id);
        datos = new HashMap<>();
        if (!existe) {
            datos.put("error", true);
            datos.put("message", "El producto no existe");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        } else {
            productRepository.deleteById(id);
            datos.put("message", "Producto eliminado");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.ACCEPTED
            );
        }
    }
}
