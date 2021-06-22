package com.fanyao.canal.kafka;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fanyao.canal.dto.CanalBean;
import com.fanyao.canal.po.Person;
import com.fanyao.canal.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.util.Strings;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: bugProvider
 * @date: 2021/6/22 14:24
 * @description: 从kafka消费canal同步的数据
 */
@Slf4j
@Component
public class CanalConsumer {
    //redis操作工具类
    @Resource
    private RedisClient redisClient;

    //监听的队列名称为：example
    @KafkaListener(topics = "example")
    public void receive(ConsumerRecord<?, ?> consumer) {
        String value = (String) consumer.value();
        log.info("topic名称:{},key:{},分区位置:{},下标:{},value:{}", consumer.topic(), consumer.key(), consumer.partition(), consumer.offset(), value);
        //topic名称:example,key:null,分区位置:0,下标:44,value:{"data":[{"id":"11","age":"5","name":"5"}],"database":"test","es":1624346411000,"id":143,"isDdl":false,"mysqlType":{"id":"int","age":"int","name":"varchar(250)"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":4,"age":4,"name":12},"table":"person","ts":1624346411817,"type":"INSERT"}
        JSONObject jsonObject = JSONObject.parseObject(value);
        String tableName = jsonObject.getString("table");
        CanalBean<Person> canalBean = null;
        if (Strings.isNotBlank(tableName) && "person".equals(tableName)) {
            //转换为javaBean
            canalBean = JSONObject.parseObject(value, new TypeReference<CanalBean<Person>>(Person.class) {
            });
        }

        //获取是否是DDL语句
        boolean isDdl = canalBean.isDdl();
        //获取类型
        String type = canalBean.getType();
        //不是DDL语句
        if (!isDdl) {
            List<Person> persons = canalBean.getData();
            //过期时间
            long TIME_OUT = 600L;
            if ("INSERT".equals(type)) {
                //新增语句
                for (Person person : persons) {
                    Integer id = person.getId();
                    //新增到redis中,过期时间是10分钟
                    redisClient.setString(id.toString(), JSONObject.toJSONString(person), TIME_OUT);
                }
            } else if ("UPDATE".equals(type)) {
                //更新语句
                for (Person person : persons) {
                    Integer id = person.getId();
                    //更新到redis中,过期时间是10分钟
                    redisClient.setString(id.toString(), JSONObject.toJSONString(person), TIME_OUT);
                }
            } else {
                //删除语句
                for (Person person : persons) {
                    Integer id = person.getId();
                    //从redis中删除
                    redisClient.deleteKey(id.toString());
                }
            }
        }
    }
}
