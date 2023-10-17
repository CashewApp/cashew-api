package br.app.cashew.feature03.cafeteria.controller;

import br.app.cashew.feature03.cafeteria.dto.stock.output.StockDto;
import br.app.cashew.feature03.cafeteria.model.Stock;
import br.app.cashew.feature03.cafeteria.model.product.Product;
import br.app.cashew.feature03.cafeteria.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{productPublicKey}")
    public ResponseEntity<Product> getProductInfo(@PathVariable String productPublicKey) {
        UUID productUuid = UUID.fromString(productPublicKey);
        return new ResponseEntity<>(productService.getProductInfo(productUuid), HttpStatus.OK);
    }

    @GetMapping("/{productPublicKey}/stock")
    public ResponseEntity<StockDto> getStockFromProduct(@PathVariable String productPublicKey) {
        UUID productUuid = UUID.fromString(productPublicKey);
        StockDto stock = convertStockToStockDto(productService.getStockFromProduct(productUuid));
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    private StockDto convertStockToStockDto(Stock stock) {
        return modelMapper.map(stock, StockDto.class);
    }
}
