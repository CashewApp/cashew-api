package br.app.cashew.feature03.cafeteria.service;

import br.app.cashew.feature03.cafeteria.exception.ProductDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.model.Stock;
import br.app.cashew.feature03.cafeteria.model.product.Product;
import br.app.cashew.feature03.cafeteria.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private static final String ERROR_MESSAGE_PRODUCT_DOES_NOT_EXISTS_EXCEPTION = "Produto nao existe";
    private static final String ERROR_MESSAGE_FIELD = "product";

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductInfo(UUID productUuid) {
        return productRepository.findByPublicKey(productUuid)
                .orElseThrow(() -> new ProductDoesNotExistsException(ERROR_MESSAGE_PRODUCT_DOES_NOT_EXISTS_EXCEPTION, ERROR_MESSAGE_FIELD));

    }

    public Stock getStockFromProduct(UUID productUuid) {
        return productRepository.findStockByPublicKey(productUuid)
                .orElseThrow(() -> new ProductDoesNotExistsException(ERROR_MESSAGE_PRODUCT_DOES_NOT_EXISTS_EXCEPTION, ERROR_MESSAGE_FIELD));
    }

    public Cafeteria getCafeteriaProductBelongs(UUID productUuid) {

        return productRepository.findCafeteriaByPublicKey(productUuid)
                .orElseThrow(() -> new ProductDoesNotExistsException(ERROR_MESSAGE_PRODUCT_DOES_NOT_EXISTS_EXCEPTION, ERROR_MESSAGE_FIELD));
    }
}
