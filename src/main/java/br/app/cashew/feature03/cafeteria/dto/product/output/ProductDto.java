package br.app.cashew.feature03.cafeteria.dto.product.output;

import br.app.cashew.feature03.cafeteria.dto.category.output.CategoryDto;
import br.app.cashew.feature03.cafeteria.dto.stock.output.StockDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private int productID;
    private String name;
    private CategoryDto category;
    private Double price;
    private String photoUrl;
    private StockDto stock;
    private String publicKey;
}
