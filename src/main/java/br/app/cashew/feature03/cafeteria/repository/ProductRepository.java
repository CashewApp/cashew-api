package br.app.cashew.feature03.cafeteria.repository;

import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.model.Stock;
import br.app.cashew.feature03.cafeteria.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCafeteriaAndStatusTrue(Cafeteria cafeteria);
    List<Product> findByCafeteriaAndStatusFalse(Cafeteria cafeteria);
    List<Product> findByCafeteria(Cafeteria cafeteria);
    Optional<Product> findByPublicKey(UUID productPublicKey);

    @Query("SELECT p.stock FROM Product p WHERE p.publicKey = :publicKey")
    Optional<Stock> findStockByPublicKey(@Param("publicKey") UUID productPublicKey);

    @Query("SELECT p.cafeteria FROM Product p WHERE p.publicKey = :publicKey")
    Optional<Cafeteria> findCafeteriaByPublicKey(@Param("publicKey") UUID productPublicKey);
}
