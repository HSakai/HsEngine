/* 
 * アクアノーツフロンティア(サーバサイド)アプリ
 * 
 * ＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿＿
 * |本アプリケーションのソースコードは株式会社そらゆめに |
 * |帰属致します。ソースコードの許可のないコピーや       |
 * |持ち出し等行為は原則的に禁止致します。               |
 * |                                                     |
 * |Copyright 2005 SORAYUME Co., Ltd. Allrights Reserved.|
 * ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣
 */
package h.sakai.game.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 設定ファイル情報を保持するクラス<br>
 * 
 * <ul>
 * 設定ファイルのリロードはサーブレットを呼び出して下さい。
 * </ul>
 * 
 * @author Bean
 * @version V1.00 2010/06/25 Bean 新規作成
 */
public final class SystemProperty {
	
	/** 過去の更新時間 */
	private static long lastModified = 0;
	
	/** 本クラスのインスタンス */
	private static SystemProperty instance = null;
	
	/** 設定情報 */
	private static Map<String, List<String>> prop = null;
	
	/** 設定ファイルパス */
	private static final String SYSTEM_PROP_PATH = new File("").getAbsolutePath() + "/conf/SystemProperty.xml";
	
	/** propertyタグ */
	private static final String TAG_PROPERTY = "property";
	
	/** valueタグ */
	private static final String TAG_VALUE = "value";
	
	/** id属性 */
	private static final String ATTR_ID = "id";
	
	/**
	 * コンストラクタ
	 */
	private SystemProperty() {
	}
	
	/**
	 * 本クラスのインスタンスを取得する。
	 * 
	 * @return 本クラスのインスタンス
	 * @throws ParserConfigurationException 
	 */
	public static SystemProperty getInstance() throws Exception {
		
		synchronized (SystemProperty.class) {
			if (instance == null) {
				instance = new SystemProperty();
				read();
			}
		}
		
		return instance;
	}
	
	/**
	 * 設定ファイルの読み込みを実行する。
	 * @throws ParserConfigurationException 
	 */
	public static void read() throws Exception {
		
		synchronized (SystemProperty.class) {
			if (instance == null) {
				instance = new SystemProperty();
			}
		}
		
		// Map作成
		if (prop != null) {
			prop.clear();
			prop = null;
		}
		
		prop = new HashMap<String, List<String>>();
		
		// ドキュメントビルダーファクトリを生成
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbfactory.newDocumentBuilder();
		
		// 読み込み時のファイル更新時間を取得
		File file = new File( SYSTEM_PROP_PATH );
		lastModified = file.lastModified();
		
		// ドキュメント取得
		Document doc = builder.parse(file);
		
		// ルート要素を取得（タグ名：properties）
		Element root = doc.getDocumentElement();
		
		// properties要素のリストを取得
		NodeList list = root.getElementsByTagName(TAG_PROPERTY);
		
		Element element = null;       // properties要素
		NodeList values = null;       // value要素リスト
		Element valueElement = null;  // value要素
		List<String> valueList = null;// value値リスト
		
		// properties要素の数だけループ
		for (int i = 0; i < list.getLength(); i++) {
			
			// properties要素を取得
			element = (Element) list.item(i);
			
			valueList = new ArrayList<String>();
			
			values = element.getElementsByTagName(TAG_VALUE);
			
			for (int j = 0; j < values.getLength(); j++) {
				valueElement = (Element) values.item(j);
				if (valueElement.getFirstChild() != null) {
					valueList.add(valueElement.getFirstChild().getNodeValue());
				} else {
					valueList.add(null);
				}
			}
			
			prop.put(element.getAttribute(ATTR_ID), valueList);
		}
		
		valueList = null;
	}
	
	/**
	 * ファイルの更新時間を調べ、更新されていたら
	 * 再読み込みを実行する。
	 * 
	 * @throws Exception 全例外
	 */
	private void updateFile() throws Exception {
		
		// 読み込み時のファイル更新時間を取得
		File file = new File(SYSTEM_PROP_PATH);
		
		if (lastModified != file.lastModified()) {
			read();
		}
	}
	
	/**
	 * 設定ファイルのキーに一致した一つの設定情報を取得する。
	 * 
	 * @param propKey キー情報
	 * @return 設定ファイル情報
	 * @throws Exception 全例外
	 */
	public String getProperty(String propKey) throws Exception {
		
		try {
			updateFile();
		} catch (Exception e) {
			throw new Exception();
		}
		
		return prop.get(propKey).get(0);
	}
	
	/**
	 * 設定ファイルのキーに一致した複数の設定情報を取得する。
	 * 
	 * @param propKey キー情報
	 * @return 設定ファイル情報
	 * @throws Exception 全例外
	 */
	public List<String> getProperties(String propKey) throws Exception {
		
		try {
			updateFile();
		} catch (Exception e) {
			throw new Exception();
		}
		
		return prop.get(propKey);
	}
}