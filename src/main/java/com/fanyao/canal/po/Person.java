package com.fanyao.canal.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: bugProvider
 * @date: 2021/6/22 14:22
 * @description: 数据库的对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    private Integer id;
    private String name;
    private Integer age;
}
