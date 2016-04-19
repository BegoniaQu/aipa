package com.aipa.svc.common.util;

import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.qy.data.common.exception.CloudPlatformRuntimeException;
import com.qy.data.common.util.StringUtil;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 与xstream一样，反序列化时，遇到未知的field，会抛异常。必须设置
 * 
 * objectMapper线程安全
 * 
 * @author qy
 * @date 2016
 */
public class JsonUtil {

	private static final ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
				false);
		objectMapper
				.configure(
						DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
		objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); 
	}

	public static String getPropertiesFromJson(String str, String[] keys) {
		String allKeys = Arrays.toString(keys);
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);
			JsonNode node = parser.readValueAsTree();
			for (String key : keys) {
				if (node.has(key)) {
					node = node.findValue(key);
				} else {
					throw new CloudPlatformRuntimeException(
							"key not exist in json str, keys:" + allKeys);
				}
			}
			return node.toString();
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error, json="
					+ str + ", keys:" + allKeys, e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error, json="
					+ str + ", keys:" + allKeys, e);
		}
	}

	public static <T> T getObjectFromJson(InputStream instream, Class<T> cls) {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					instream);
			T t = objectMapper.readValue(parser, cls);
			return t;
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error", e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error", e);
		} finally {
			try {
				instream.close();
			} catch (Exception ignore) {

			}
		}
	}

	public static <T> T getObjectFromJson(String str, Class<T> cls) {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);
			T t = objectMapper.readValue(parser, cls);
			return t;
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error, json="
					+ str + ", class=" + cls.getName(), e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error, json="
					+ str + ", class=" + cls.getName(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static String getValueByFieldFromJson(String json, String field) {
		Map<String, String> mapValue = getObjectFromJson(json, HashMap.class);
		return mapValue.get(field);
	}

	public static String getJsonFromObject(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			throw new CloudPlatformRuntimeException("get json error", e);
		} catch (JsonMappingException e) {
			throw new CloudPlatformRuntimeException("get json error", e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("get json error", e);
		}
	}

	public static <T> List<T> parserJsonList(InputStream instream, Class<T> clsT) {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					instream);
			
			JsonNode nodes = parser.readValueAsTree();

			List<T> list = new LinkedList<T>();
			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node, clsT));
			}
			return list;
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error", e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error", e);
		} finally {
			try {
				instream.close();
			} catch (Exception ignore) {

			}
		}
	}

	public static <T> List<T> parserJsonList(String str, Class<T> clsT) {
		try {
			
			if(StringUtil.isEmpty(str)){
				return null;
			}
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);

			JsonNode nodes = parser.readValueAsTree();
			List<T> list = new LinkedList<T>();
			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node, clsT));
			}
			return list;
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		}
	}
	
	public static <T> T parseJson(String str, TypeReference<T> tr){
		try {
			
			if(StringUtil.isEmpty(str)){
				return null;
			}
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);
			return parser.readValueAs(tr);
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] parserJsonArray(String str, Class<T> clsT) {
		try {
			
			if(StringUtil.isEmpty(str)){
				return null;
			}
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);

			JsonNode nodes = parser.readValueAsTree();
			Object[] list = new Object[nodes.size()];
			if(clsT.equals(Integer.class)){
				list = new Integer[nodes.size()];
			} else if(clsT.equals(Long.class)){
				list = new Long[nodes.size()];
			} else if(clsT.equals(String.class)){
				list = new String[nodes.size()];
			} else {
				list = new Object[nodes.size()];
			}
			int i =0;
			for (JsonNode node : nodes) {
				list[i++] = objectMapper.readValue(node, clsT);
			}
			
			return (T[]) list;
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		}
	}

	public static <T> LinkedHashMap<String, T> parserJsonMap(String str,
			Class<T> clsT) {
		LinkedHashMap<String, T> map = new LinkedHashMap<String, T>();
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);

			JsonToken current;

			current = parser.nextToken();
			if (current != JsonToken.START_OBJECT) {
				throw new CloudPlatformRuntimeException(
						"parse json error: root should be object, quiting.");
			}

			while (parser.nextToken() != JsonToken.END_OBJECT) {
				String fieldName = parser.getCurrentName();
				current = parser.nextToken();
				T obj = parser.readValueAs(clsT);
				map.put(fieldName, obj);
			}

			return map;
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		}
	}

	public static <T extends Enum<T>> EnumSet<T> parserJsonEnum(String str,
			Class<T> clsT) {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(
					str);

			JsonNode nodes = parser.readValueAsTree();

			EnumSet<T> enumSet = EnumSet.noneOf(clsT);
			for (JsonNode node : nodes) {
				enumSet.add(objectMapper.readValue(node, clsT));
			}
			return enumSet;
		} catch (JsonParseException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		} catch (IOException e) {
			throw new CloudPlatformRuntimeException("parse json error str:"
					+ str, e);
		}
	}

	/**
	 * 当格式不对，抛出异常422
	 * @param request
	 * @param cls
	 * @return
	 */
	public static <T> T jsonConvert(HttpServletRequest request, Class<T> cls) {
		//XXX:经常出现json错误后，不好查。这里用字符串去转
		String json = null;
		try {
			json = ServletUtils.readStringFromRequest(request);
			return getObjectFromJson(json, cls);
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("json error, json=" + json, e);
		}
	}
	
	public static <T> List<T> jsonsConvert(HttpServletRequest request, Class<T> cls) {
		//XXX:经常出现json错误后，不好查。这里用字符串去转
		try {
			return parserJsonList(request.getInputStream(), cls);
		} catch (IOException e) {
			throw new InvalidRequestRuntimeException("json io error", e);
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("json error", e);
		}
	}
	
	
}
