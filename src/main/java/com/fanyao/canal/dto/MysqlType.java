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
public class MysqlType {
    private String id;
    private String commodity_name;
    private String commodity_price;
    private String number;
    private String description;
}
