package br.app.cashew.feature03.cafeteria.controller;

import br.app.cashew.feature03.cafeteria.dto.product.output.ProductDto;
import br.app.cashew.feature03.cafeteria.dto.stock.output.StockDto;
import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.dto.ProductStockWrapper;
import br.app.cashew.feature03.cafeteria.model.Category;
import br.app.cashew.feature03.cafeteria.model.product.Product;
import br.app.cashew.feature03.cafeteria.model.product.ProductStatus;
import br.app.cashew.feature03.cafeteria.service.CafeteriaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cafeterias")
public class CafeteriaController {


    private final CafeteriaService cafeteriaService;
    private final ModelMapper modelMapper;

    @Autowired
    public CafeteriaController(CafeteriaService cafeteriaService, ModelMapper modelMapper) {

        this.cafeteriaService = cafeteriaService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<Cafeteria> createCafeteria(@RequestBody Cafeteria cafeteria, Authentication authentication) {

        Cafeteria response = cafeteriaService.createCafeteria(cafeteria, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{cafeteriaPublicKey}")
    public ResponseEntity<Cafeteria> getCafeteria (@PathVariable String cafeteriaPublicKey) {

        Cafeteria cafeteria = cafeteriaService.getCafeteria(cafeteriaPublicKey);

        return new ResponseEntity<>(cafeteria, HttpStatus.OK);
    }

    @GetMapping("/{cafeteriaPublicKey}/products")
    public ResponseEntity<ProductStockWrapper> getProductsFromCafeteria(@PathVariable String cafeteriaPublicKey,
                                                                     @RequestParam(required = false, defaultValue = "ACTIVE") ProductStatus status,
                                                                     @RequestParam(required = false, defaultValue = "false") boolean onlyStock,
                                                                     Authentication authentication) {

        UUID cafeteriaUuid = UUID.fromString(cafeteriaPublicKey);
        ProductStockWrapper productStockWrapper = new ProductStockWrapper();

        if (status == ProductStatus.INACTIVE || status == ProductStatus.ALL) {

            boolean hasEmployeeOrOwnerAuthority = authentication.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_OWNER".equals(authority.getAuthority()) || "ROLE_EMPLOYEE".equals(authority.getAuthority()));

            if (!hasEmployeeOrOwnerAuthority) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            return getProductStockWrapperResponseEntity(status, onlyStock, cafeteriaUuid, productStockWrapper);
        }

        return getProductStockWrapperResponseEntity(status, onlyStock, cafeteriaUuid, productStockWrapper);
        // TODO refatorar este metodo
    }

    @GetMapping("/{cafeteriaPublicKey}/categories")
    public ResponseEntity<List<Category>> getCategoriesFromCafeteria(@PathVariable String cafeteriaPublicKey) {
        UUID cafeteriaUuid = UUID.fromString(cafeteriaPublicKey);
        return new ResponseEntity<>(cafeteriaService.getCategoriesFromCafeteria(cafeteriaUuid), HttpStatus.OK);
    }
    private ResponseEntity<ProductStockWrapper> getProductStockWrapperResponseEntity(ProductStatus status,
                                                                                     boolean onlyStock,
                                                                                     UUID cafeteriaUuid,
                                                                                     ProductStockWrapper productStockWrapper) {
        List<ProductDto> products;
        List<StockDto> stock = new ArrayList<>();
        products = cafeteriaService.getProductsFromCafeteria(cafeteriaUuid, status).stream()
                .map(this::convertProductToProductDto)
                .toList();

        if (onlyStock) {
            for (ProductDto product : products) {

                stock.add(product.getStock());
            }
            productStockWrapper.setStock(stock);

            return new ResponseEntity<>(productStockWrapper, HttpStatus.OK);
        }
        productStockWrapper.setProducts(products);
        return new ResponseEntity<>(productStockWrapper, HttpStatus.OK);
    }
    private ProductDto convertProductToProductDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
/*
TODO utilizar de JsonViews para evitar o envio desnecessario:
  - do CNPJ da lanchonete
 */
