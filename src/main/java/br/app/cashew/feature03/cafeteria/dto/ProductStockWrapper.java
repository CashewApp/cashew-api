package br.app.cashew.feature03.cafeteria.dto;

import br.app.cashew.feature03.cafeteria.dto.product.output.ProductDto;
import br.app.cashew.feature03.cafeteria.dto.stock.output.StockDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductStockWrapper {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ProductDto> products;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StockDto> stock;
}
