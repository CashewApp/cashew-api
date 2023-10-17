package br.app.cashew.feature03.cafeteria.service;

import br.app.cashew.feature03.cafeteria.exception.ProductDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.model.product.Product;
import br.app.cashew.feature03.cafeteria.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductInfo(UUID productUuid) {
        return productRepository.findByPublicKey(productUuid)
                .orElseThrow(() -> new ProductDoesNotExistsException("Produto nao existe", "product"));

    }
}
