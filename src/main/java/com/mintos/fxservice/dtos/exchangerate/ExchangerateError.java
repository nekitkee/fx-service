package com.mintos.fxservice.dtos.exchangerate;

import lombok.Data;

@Data
public class ExchangerateError {
    private Integer code;
    private String  type;
    private String info;
}
