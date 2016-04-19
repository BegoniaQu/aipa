package com.aipa.svc.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.aipa.svc.common.exception.InvalidRequestRuntimeException;
import com.qy.data.common.exception.CloudPlatformRequestRuntimeException;
import com.qy.data.common.exception.CloudPlatformRuntimeException;

public class ServletUtils {
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static Logger logger = LoggerFactory.getLogger(ServletUtils.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private static final JsonEncoding encoding = JsonEncoding.UTF8;

	public static String readStringFromRequest(HttpServletRequest request, Charset charset) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), charset));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new InvalidRequestRuntimeException("read request body error.", "read request body error.", e);
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String readStringFromRequest(HttpServletRequest request) {
		return readStringFromRequest(request, CHARSET);
	}

	public static String getServerIpByRequest(HttpServletRequest request) {
		String fromHost = request.getHeader("X-Real-IP");
		if (null == fromHost || fromHost.isEmpty()) {
			fromHost = request.getRemoteHost();
		}
		return fromHost;
	}

	public static void sendXMLResponse(HttpServletResponse response, int status, String xml) {
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setStatus(status);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(xml);
			out.flush();
		} catch (IOException e) {
			logger.error("send xml error. xml:{}", xml, e);
		}
	}

	public static void sendTextResponse(HttpServletResponse response, int status, String text) {
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setStatus(status);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(text);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void sendObjectResponseByJson(HttpServletResponse response, Object object) {
		try {
			response.setContentType("text/plain; charset=UTF-8");

			JsonGenerator generator = objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(),
					encoding);
			objectMapper.writeValue(generator, object);
		} catch (Exception ex) {
			throw new CloudPlatformRuntimeException("json convert error", ex);
		}
	}

	public static <T> T getObjectRequestFromJson(HttpServletRequest request, Class<T> cls) {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(request.getInputStream());
			T t = objectMapper.readValue(parser, cls);
			return t;
		} catch (IOException ex) {
			throw new InvalidRequestRuntimeException("parse json error", ex);
		}
	}

	public static <T> List<T> getObjectsFromJson(HttpServletRequest request, Class<T> clsT) {
		JsonParser parser;
		try {
			parser = objectMapper.getJsonFactory().createJsonParser(request.getInputStream());

			JsonNode nodes = parser.readValueAsTree();

			List<T> list = new LinkedList<T>();
			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node, clsT));
			}

			return list;
		} catch (IOException e) {
			throw new InvalidRequestRuntimeException("parse json error", e);
		}
	}

	public static String getJsonFromObject(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			logger.error("get json error", e);
			throw new CloudPlatformRuntimeException("get json error", e);
		} catch (JsonMappingException e) {
			logger.error("get json error", e);
			throw new CloudPlatformRuntimeException("get json error", e);
		} catch (IOException e) {
			logger.error("get json error", e);
			throw new CloudPlatformRuntimeException("get json error", e);
		}
	}

	public static String encodeUrl(String url) throws UnsupportedEncodingException {
		return encodeUrl(url, "utf-8");
	}

	public static String decodeUrl(String url) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, "utf-8");
	}

	public static String encodeUrl(String url, String charset) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, charset);
	}

	public static String decodeUrl(String url, String charset) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, charset);
	}

	public static HttpHeaders getHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html", CHARSET));
		return headers;
	}

	public static void printNoView(HttpServletResponse response, Object object, String contentType) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(String.valueOf(object));
			out.flush();
		} catch (IOException e) {
			throw new CloudPlatformRequestRuntimeException("", ResultBean.SYS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					e);
		}
	}

	public static void printNoCharsetView(HttpServletResponse response, Object object, String contentType) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(contentType);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(String.valueOf(object));
			out.flush();
		} catch (IOException e) {
			throw new CloudPlatformRequestRuntimeException("", ResultBean.SYS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					e);
		}
	}

	public static void processException(HttpServletResponse response, Exception e) {
		logger.error("process exception, ex:", e);
		PrintWriter out;
		try {
			out = response.getWriter();
			if (e instanceof CloudPlatformRequestRuntimeException) {
				CloudPlatformRequestRuntimeException ex = (CloudPlatformRequestRuntimeException) e;
				response.setStatus(ex.getHttpStatus().value());
				out.write(ex.getMessage() + "\r\n" + ex.getReason());
			} else {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				out.write(e.getMessage());
			}
		} catch (IOException ioe) {
			throw new CloudPlatformRequestRuntimeException("", ResultBean.SYS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					ioe);
		}
	}

	public static void printNoView(HttpServletResponse response, Object object) {
		printNoView(response, object, "text/plain");
	}
}
