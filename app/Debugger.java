

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.mvc.Http.Request;
import play.mvc.Http.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;

public class Debugger {

	/* 定数 */
	// デバッグ表示するコンテンツの最大長 (この値を超えた場合 body を表示しない)
	public static final int DEBUG_MAX_CONTENT_LENGTH = 1000;
	// Content-Length 取得失敗時にデバッグ表示する body の最大長 (この長さで substring する)
	public static final int DEBUG_MAX_HINT_CONTENT_LENGTH = 1000;
	// XML の value の表示最大長
	public static final int DEBUG_MAX_XML_VALUE_LENGTH = 100;


	
	public static void callOnRequest(Request request) {

		// リクエスト行
		String requestLine = request.method() + " " + request.uri() + " " + request.version();

		// Header
		Map<String, String[]> headerMap = request.headers();

		// Query String
		Map<String, String[]> queryStringMap = request.queryString();

		// Body
		String method = request.method().toLowerCase();
		RequestBody requestBody = request.body();
		StringBuilder body = new StringBuilder("\n");
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
				// ContentType が application/multipart/form-data の場合
				Map<String, String[]> bodyMap = requestBody.asMultipartFormData().asFormUrlEncoded();

				body.append("[Body] Multipart Form Data\n");
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
				body.append(requestBody.asText().substring(0, DEBUG_MAX_CONTENT_LENGTH));
			} else {
				body.append("[Body] ELIMINATED CONTENT " + request.getHeader("content-type"));
			}
		}

		// デバッグ表示
		StringBuilder out = new StringBuilder();
		out.append("\n");
		out.append("\n");
		out.append("=====================================================");
		out.append("\n");
		out.append("Received Time: " + (new Date()).toString());
		out.append("\n");
		out.append("[Request] " + requestLine);
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

		out.append(body.toString());
		out.append("\n");

		out.append("=====================================================");
		out.append("\n");


		Logger.debug(out.toString());
	}






	private static String convertXmlToString(Document document) {
		StringBuilder sb = new StringBuilder();

		NodeList nodes = document.getChildNodes();
		int length = nodes.getLength();
		for(int i = 0; i < length; ++i) {
			Node node = nodes.item(i);
			sb.append(convertXmlToString(document, node, 0));
		}

		return sb.toString();
	}
	private static String convertXmlToString(Document document, Node parentNode, int depth) {
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
	private static String formatXmlValue(String value) {
		String val = value;

		if( val == null ) return val;

		if( DEBUG_MAX_XML_VALUE_LENGTH < value.length() ) {
			val = value.substring(0, DEBUG_MAX_XML_VALUE_LENGTH);
		}

		return val;
	}


	private static String convertJsonToString(JsonNode node) {
		StringBuilder sb = new StringBuilder();

		Iterator<Entry<String, JsonNode>> it = node.fields();
		while(it.hasNext()) {
			Entry<String, JsonNode> entry = it.next();

			convertJsonToString(entry.getKey(), entry.getValue(), 0);
		}

		return sb.toString();
	}
	private static String convertJsonToString(String key, JsonNode node, int depth) {
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




	private static void addNewLine(StringBuilder sb, int depth) {
		sb.append("\n");
		addIndent(sb, depth);
		sb.append("  ");
	}
	private static void addIndent(StringBuilder sb, int depth) {
		for(int i = 0; i < depth; ++i) {
			sb.append(" ");
		}
	}

}
