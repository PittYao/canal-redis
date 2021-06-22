package com.fanyao.canal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: bugProvider
 * @date: 2021/6/22 14:21
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SqlType {
    private int id;
    private int commodity_name;
    private int commodity_price;
    private int number;
    private int description;
}
