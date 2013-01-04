package h.sakai.game.engine;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

/**
 * シーンの基底クラス<br />
 * 
 * 各シーンはこれを継承してシーンを作成する。
 * 
 * @author H.Sakai
 * @version V1.00 2012/12/28 H.Sakai 新規作成
 */
abstract public class Scene {
	
	/** キーイベント */
	private KeyEvent _keyEvent = new KeyEvent();
	
	/**
	 * キーイベントを取得する。
	 * 
	 * @return キーイベント
	 */
	protected KeyEvent getKeyEvent() {
		return _keyEvent;
	}
	
	/**
	 * @see KeyEvent#isPressKey(int)
	 */
	public boolean isPressKey( int keyCode ) {
		return _keyEvent.isPressKey( keyCode );
	}
	
	/**
	 * @see KeyEvent#isReleaseKey(int)
	 */
	public boolean isReleaseKey( int keyCode ) {
		return _keyEvent.isReleaseKey( keyCode );
	}
	
	/**
	 * シーンの初期化処理を実行する。
	 */
	public void processInitialize() {
		
	}
	
	/**
	 * シーンの開始処理を実行する。
	 */
	public void processBegin() {
		
	}
	
	/**
	 * フレーム更新イベント
	 * 
	 * @param isEnterSub サブシーンが親の場合:true、自身が親の場合:false
	 */
	public void processUpdaste( boolean isEnterSub ) {
		
	}
	
	/**
	 * 描画イベント
	 * 
	 * @param g グラフィックオブジェクト
	 * @param imgObserver 描画監視インスタンス
	 * @param isEnterSub サブシーンが親シーンの場合:true、自身が親シーンの場合:false
	 */
	public void processDraw( Graphics g, ImageObserver imgObserver, boolean isEnterSub ) {
		
	}
	
	/**
	 * シーンの破棄処理を実行する。
	 */
	public void dispose() {
		
	}
	
	/**
	 * 引数のシーンIDに移動する。
	 * 
	 * @param sceneId シーンID
	 */
	public void jumpScene( int sceneId ) {
		SceneDirector.runScene( SceneFactory.createScene( sceneId ) );
	}
	
	/**
	 * 引数のシーンをサブシーンとして呼び出す。
	 * 
	 * @param sceneId シーンID
	 */
	public void callSubScene( int sceneId ) {
		SceneDirector.pushScene( SceneFactory.createScene( sceneId ) );
	}
	
	/**
	 * 引数のシーンと今のシーンを入れ替える。
	 * 
	 * @param sceneId シーンID
	 */
	public void callScene( int sceneId ) {
		SceneDirector.replaceScene( SceneFactory.createScene( sceneId ) );
	}
	
	/**
	 * 一つ前のシーンに戻る。
	 */
	public void returnScene() {
		SceneDirector.returnScene();
	}
}
