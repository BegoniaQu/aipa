package com.aipa.svc.common.util;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qy.data.common.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by qy
 */


public class KafkaUtil {
    public static String getKafka_broker_list() {
        return kafka_broker_list;
    }

    public static void setKafka_broker_list(String kafka_broker_list) {
        KafkaUtil.kafka_broker_list = kafka_broker_list;
    }

    public static String getKafka_environment() {
        return kafka_environment;
    }

    public static void setKafka_environment(String kafka_environment) {
        KafkaUtil.kafka_environment = kafka_environment;

    }

    public static String kafka_environment="";
    public static String kafka_broker_list="";
    private static boolean sync_log = false;
    private static Producer<String,String> kafka_producer=null;
    public static final String KEY_USER_NAME = "__UserName";
    private static Logger logger = LoggerFactory.getLogger("kafka.log");
    @SuppressWarnings("unused")
	private static String seperator = "!^&$";
    
    @SuppressWarnings("unused")
	private static StringBuilder append(StringBuilder sb,String property,String value){
        if (null!=value){
            sb.append(property+"="+value+"&");
        }
        return sb;
    }
    private static Producer<String,String> getProducer(){
        if (null==kafka_producer){
            Properties props = new Properties();
            //此处配置的是kafka的端口
            props.put("metadata.broker.list", kafka_broker_list);

            //配置value的序列化类
            props.put("serializer.class", "kafka.serializer.StringEncoder");
            //配置key的序列化类
            props.put("key.serializer.class", "kafka.serializer.StringEncoder");

            //request.required.acks
            //0, which means that the producer never waits for an acknowledgement from the broker (the same behavior as 0.7). This option provides the lowest latency but the weakest durability guarantees (some data will be lost when a server fails).
            //1, which means that the producer gets an acknowledgement after the leader replica has received the data. This option provides better durability as the client waits until the server acknowledges the request as successful (only messages that were written to the now-dead leader but not yet replicated will be lost).
            //-1, which means that the producer gets an acknowledgement after all in-sync replicas have received the data. This option provides the best durability, we guarantee that no messages will be lost as long as at least one in sync replica remains.
            props.put("request.required.acks","-1");
//            Properties properties = new Properties();
//            properties.setProperty("metadata.broker.list",kafka_broker_list);
//            properties.setProperty("serializer.class", "org.apache.kafka.common.serialization.StringSerializer");
//            properties.setProperty("key.serializer.class", "org.apache.kafka.common.serialization.StringSerializer");
//            properties.setProperty("key.serializer.class", "org.apache.kafka.common.serialization.StringSerializer");
//            properties.setProperty("request.required.acks", "1");
            kafka_producer =new Producer<String, String>(new ProducerConfig(props));
        }
        return kafka_producer;
    }

    public static void kafkaTrace(HttpServletRequest request){
        Date _begin=new Date();
        String userid = RequestExtract.getAuthUserName(request);
        if (StringUtil.isEmpty(userid)){
            userid="null";
        }
        String userip = request.getHeader("x-forwarded-for");
        if(userip == null || userip.length() == 0 || "unknown".equalsIgnoreCase(userip)){
            userip = request.getHeader("Proxy-Client-IP");
        }
        if(userip == null || userip.length() == 0 || "unknown".equalsIgnoreCase(userip)){
            userip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(userip == null || userip.length() == 0 || "unknown".equalsIgnoreCase(userip)) {
            userip = request.getRemoteAddr();
        }
        String info = String.format("{\"category\":\"%s\",\"subtype\":\"%s\",\"userid\":\"%s\",\"userip\":\"%s\",\"url\":\"%s\",\"paras\":\"%s\",\"timestamp\":\"%s\"}", LogCategory.access.name(), LogCategory.access.name(), userid, userip, request.getRequestURI(), request.getQueryString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        try {
            if (sync_log) {
                getProducer().send(new KeyedMessage<String, String>(kafka_environment, LogCategory.access.name(), info));
            }
        }catch (Exception e){
            logger.warn("Failed to send the message to kafka:" + info,e);
        }
        logger.info(info);
        logger.debug("time spend for call kafka: ",new Date().getTime()-_begin.getTime());
//        StringBuilder sb = new StringBuilder("category="+LogCategory.access.name()+";");
//        append(sb,"userid",userid);
//        append(sb,"lbs",ParameterTool.getParameterString(request,"lbs",null));
//        append(sb,"source",ParameterTool.getParameterString(request,"source",null));
//        append(sb, "", ParameterTool.getParameterString(request, "source", null));
//
//        return sb.toString();
    }
    public static void kafkaAlert(String rawLog){
        kafkaStringTrace(LogCategory.alert,LogCategory.alert,rawLog);
    }
    public static void kafkaStringTrace(LogCategory category, LogCategory subCategory, String rawLog){
        String info = String.format("{\"category\":\"%s\",\"subtype\":\"%s\",\"timestamp\":\"%s\",\"data\":\"%s\"}",category.name(),subCategory.name(),new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),rawLog);
        try {
            if (sync_log) {
                getProducer().send(new KeyedMessage<String, String>(kafka_environment, LogCategory.access.name(), info));
            }
        }catch (Exception e){
            logger.warn("Failed to send the message to kafka:" + info,e);
        }
        logger.info(info);
    }

    public static  void kafkaObjectTrace(LogCategory category,LogCategory subCategory,Object object){
        String info=null;
        try {
            info = JsonUtil.getJsonFromObject(object);
        }
        catch (Exception e){
            logger.error("Failed to convert the object to json", category, subCategory);
            return ;
        }
        info=String.format("{\"category\":\"%s\",\"subtype\":\"%s\",\"timestamp\":\"%s\",\"data\":%s}", category.name(), subCategory.name(), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), info);
        try {
            if (sync_log) {
                getProducer().send(new KeyedMessage<String, String>(kafka_environment, LogCategory.access.name(), info));
            }
        }catch (Exception e){
            logger.warn("Failed to send the message to kafka:" + info,e);
        }
        logger.info(info);
    }
    //example :        kafkaStringTrace(LogCategory.circle,LogCategory.circle_info_add,"json string of the new added circle",logger);


}
