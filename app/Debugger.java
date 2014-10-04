

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.Request;
import play.mvc.Http.RequestBody;
import play.mvc.Http.RequestHeader;

import com.fasterxml.jackson.databind.JsonNode;

public class Debugger {

	/* 定数 */
	// デバッグ表示するコンテンツの最大長 (この値を超えた場合 body を表示しない)
	public static final int DEBUG_MAX_CONTENT_LENGTH = 1000;
	// Content-Length 取得失敗時にデバッグ表示する body の最大長 (この長さで substring する)
	public static final int DEBUG_MAX_HINT_CONTENT_LENGTH = 1000;
	// XML の value の表示最大長
	public static final int DEBUG_MAX_XML_VALUE_LENGTH = 100;


	/**
	 * RequestHeader のデバッグ情報をログに出力する
	 * @param requestHeader
	 * @throws Throwable
	 */
	public void debug(RequestHeader requestHeader) throws Throwable {
		// リモートホスト
		String xForwardedFor = requestHeader.getHeader("X-Forwarded-For");
		String remoteAddress = requestHeader.remoteAddress();

		// Header
		String header = debugHeader(requestHeader);

		// デバッグ表示
		StringBuilder out = new StringBuilder();
		out.append("\n");
		out.append("\n");
		out.append("=====================================================");
		out.append("\n");
		out.append("[RouteRequest] "+(new Date()).toString());
		out.append("\n");
		out.append("[Remote Address] " + remoteAddress);
		out.append("\n");
		if (xForwardedFor != null) {
			out.append("[X-Fowarded-For] " + xForwardedFor);
			out.append("\n");
		}
		out.append("=====================================================");
		out.append("\n");
		out.append(header);
		out.append("=====================================================");
		out.append("\n");


		Logger.debug(out.toString());
	}


	/**
	 * Request のデバッグ情報をログに出力する
	 * @param request
	 * @throws Throwable
	 */
	public void debug(Request request) throws Throwable {
		// リモートホスト
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		String remoteAddress = request.remoteAddress();

		// Header
		String header = debugHeader(request);

		// Body
		String body = debugBody(request);

		// デバッグ表示
		StringBuilder out = new StringBuilder();
		out.append("\n");
		out.append("\n");
		out.append("=====================================================");
		out.append("\n");
		out.append("[Request] "+(new Date()).toString());
		out.append("\n");
		out.append("[Remote Address] " + remoteAddress);
		out.append("\n");
		if (xForwardedFor != null) {
			out.append("[X-Fowarded-For] " + xForwardedFor);
			out.append("\n");
		}
		out.append("=====================================================");
		out.append("\n");
		out.append(header);
		if (body != null && !body.isEmpty()) {
			out.append("\n");
			out.append(body);
		}
		out.append("=====================================================");
		out.append("\n");


		Logger.debug(out.toString());
	}


	/**
	 * リクエストヘッダのデバッグ情報を返す
	 * @param request
	 * @return
	 */
	public String debugHeader(RequestHeader request) {
		// リクエスト行
		String requestLine = request.method() + " " + request.uri() + " " + request.version();

		// Header
		Map<String, String[]> headerMap = request.headers();

		// Query String
		Map<String, String[]> queryStringMap = request.queryString();

		// デバッグ表示
		StringBuilder out = new StringBuilder();
		out.append("[Request] " + requestLine);
		out.append("\n");
		out.append("\n");

		for (String key : headerMap.keySet()) {
			String[] values = headerMap.get(key);
			if( values.length == 1 ) {
				out.append("[Header] " + key + ": " + values[0]);
				out.append("\n");
			} else {
				for(int i = 0; i < values.length; ++i) {
					out.append("[Header] " + key + "[" + i + "]" + ": " + values[i]);
					out.append("\n");
				}
			}
		}

		for (String key : queryStringMap.keySet()) {
			String[] values = queryStringMap.get(key);
			if( values.length == 1 ) {
				out.append("[QueryString] " + key + ": " + values[0]);
				out.append("\n");
			} else {
				for(int i = 0; i < values.length; ++i) {
					out.append("[QueryString] " + key + "[" + i + "]" + ": " + values[i]);
					out.append("\n");
				}
			}
		}

		return out.toString();
	}


	/**
	 * リクエストボディのデバッグ情報を返す
	 * @param request
	 * @return
	 */
	public String debugBody(Request request) {
		StringBuilder body = new StringBuilder();

		String method = request.method().toLowerCase();
		RequestBody requestBody = request.body();
		if( method.equals("post") || method.equals("put") ) {
			if( requestBody.asFormUrlEncoded() != null ) {
				// ContentType が application/x-www-form-urlencoded の場合
				Map<String, String[]> bodyMap = requestBody.asFormUrlEncoded();

				body.append("[Body] Form Url Encoded\n");
				for(String key : bodyMap.keySet()) {
					String[] values = bodyMap.get(key);
					if( values.length == 1 ) {
						body.append("+ ");
						body.append(key);
						body.append(": ");
						body.append((values[0] == null) ? "(null)" : values[0]);
						body.append("\n");
					} else {
						for(int i = 0; i < values.length; ++i) {
							body.append("+ ");
							body.append(key);
							body.append("[" + i + "]: ");
							body.append((values[i] == null) ? "(null)" : values[i]);
							body.append("\n");
						}
					}
				}

			} else if( requestBody.asMultipartFormData() != null ) {
				// ContentType が multipart/form-data の場合
				Map<String, String[]> bodyMap = requestBody.asMultipartFormData().asFormUrlEncoded();
				List<FilePart> files = requestBody.asMultipartFormData().getFiles();

				body.append("[Body] Multipart Form Data\n");
				for (String key : bodyMap.keySet()) {
					String[] values = bodyMap.get(key);
					if( values.length == 1 ) {
						body.append("+ ");
						body.append(key);
						body.append(": ");
						body.append("\n--- begin "+key+" ---\n");
						body.append((values[0] == null) ? "(null)" : values[0]);
						body.append("\n---  end  "+key+" ---\n");
					} else {
						for(int i = 0; i < values.length; ++i) {
							body.append("+ ");
							body.append(key+"[" + i + "]");
							body.append(": ");
							body.append("\n  ");
							body.append("\n--- begin "+key+"[" + i + "] ---\n");
							body.append((values[i] == null) ? "(null)" : values[i]);
							body.append("\n");
							body.append("\n---  end  "+key+"[" + i + "] ---\n");
						}
					}
				}
				if (files != null) {
					for (FilePart file : files) {
						body.append("+ ");
						body.append(file.getKey());
						body.append(": ");
						body.append(file.getContentType());
						body.append("\n  ");
						body.append(file.getFile().getAbsolutePath());
						body.append("\n");
					}
				}

			} else if( requestBody.asXml() != null ) {
				// ContentType が text/xml の場合
				Document document = requestBody.asXml();
				body.append("[Body] XML\n");
				body.append(convertXmlToString(document));

			} else if( requestBody.asJson() != null ) {
				// ContentType が application/json の場合
				JsonNode node = requestBody.asJson();
				body.append("[Body] JSON\n");
				body.append(convertJsonToString(node));

			} else if( requestBody.asText() != null ) {
				// ContentType が text/* の場合
				body.append("[Body] ");
				String text = requestBody.asText();
				if (DEBUG_MAX_CONTENT_LENGTH < text.length()) {
					body.append(text.substring(0, DEBUG_MAX_CONTENT_LENGTH));
				} else {
					body.append(text);
				}
			} else if ( requestBody.asRaw() != null ) {
				//
				body.append("[Body] ");
				byte[] raw = requestBody.asRaw().asBytes();
				if (DEBUG_MAX_CONTENT_LENGTH < raw.length) {
					for (int i = 0; i < DEBUG_MAX_CONTENT_LENGTH; ++i) {
						body.append(raw[i]);
					}
				} else {
					body.append(raw);
				}
			} else {
				body.append("[Body] NO CONTENT " + request.getHeader("content-type"));
			}
		}

		return body.toString();
	}


	private String convertXmlToString(Document document) {
		StringBuilder sb = new StringBuilder();

		NodeList nodes = document.getChildNodes();
		int length = nodes.getLength();
		for(int i = 0; i < length; ++i) {
			Node node = nodes.item(i);
			sb.append(convertXmlToString(document, node, 0));
		}

		return sb.toString();
	}
	private String convertXmlToString(Document document, Node parentNode, int depth) {
		StringBuilder sb = new StringBuilder();


		// 親ノード出力
		// インデントを揃える
		addIndent(sb, depth);

		// <parentNode>hogehoge</parentNode> の場合 (要素 value の場合)
		// hogehoge を表示して終了
		if( parentNode.getLocalName() == null ) {
			sb.append(" (element value): ");
			sb.append(formatXmlValue(parentNode.getNodeValue()));
			return sb.toString();
		}


		// 要素 value ではない場合

		// 目印を入れる
		sb.append("+ ");

		// Key と Value 表示
		sb.append(parentNode.getLocalName());
		sb.append(": ");
		if( parentNode.getNodeValue() != null ) {
			sb.append(parentNode.getNodeValue());
			addNewLine(sb, depth);
		}

		// 属性がある場合表示
		NamedNodeMap attrs = parentNode.getAttributes();
		int attrsLength = attrs.getLength();
		for(int i = 0; i < attrsLength; ++i) {
			Node attr = attrs.item(i);
			addNewLine(sb, depth);
			sb.append("(attr) ");
			sb.append(attr.getLocalName());
			sb.append("=");
			sb.append(attr.getNodeValue());
		}

		// 子ノードがあれば出力
		NodeList nodes = parentNode.getChildNodes();
		int length = nodes.getLength();
		sb.append("\n");
		for (int i = 0; i < length; ++i) {
			Node node = nodes.item(i);
			sb.append(convertXmlToString(document, node, (depth + 1)));
		}

		return sb.toString();
	}
	private String formatXmlValue(String value) {
		String val = value;

		if( val == null ) return val;

		if( DEBUG_MAX_XML_VALUE_LENGTH < value.length() ) {
			val = value.substring(0, DEBUG_MAX_XML_VALUE_LENGTH);
		}

		return val;
	}


	private String convertJsonToString(JsonNode node) {
		StringBuilder sb = new StringBuilder();

		Iterator<Entry<String, JsonNode>> it = node.fields();
		while(it.hasNext()) {
			Entry<String, JsonNode> entry = it.next();

			convertJsonToString(entry.getKey(), entry.getValue(), 0);
		}

		return sb.toString();
	}
	private String convertJsonToString(String key, JsonNode node, int depth) {
		StringBuilder sb = new StringBuilder();

		// 自ノードを出力
		// インデントを揃える
		addIndent(sb, depth);
		//
		sb.append("+ ");
		// Key と Value 表示
		sb.append(key);
		sb.append(": ");
		sb.append(node.getNodeType().toString());
		sb.append("\n");

		// 子ノードがあれば出力
		Iterator<Entry<String, JsonNode>> it = node.fields();
		while(it.hasNext()) {
			Entry<String, JsonNode> entry = it.next();

			convertJsonToString(entry.getKey(), entry.getValue(), (depth+1));
		}

		return sb.toString();
	}




	private void addNewLine(StringBuilder sb, int depth) {
		sb.append("\n");
		addIndent(sb, depth);
		sb.append("  ");
	}
	private void addIndent(StringBuilder sb, int depth) {
		for(int i = 0; i < depth; ++i) {
			sb.append(" ");
		}
	}

}
