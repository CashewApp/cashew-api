package br.app.cashew.feature03.cafeteria.dto.stock.output;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class StockDto {
    private int stockID;
    private Calendar lastUpdate;
    private int availableQuantity;
}
