package h.sakai.game.engine;

/**
 * キーイベントの管理クラス
 * 
 * @author H.Sakai
 * @version V1.00 2013/01/01 新規作成
 */
public final class KeyEvent {
	
	private int _pressKey = -1;
	private int _releaseKey = -1;
	
	/**
	 * キーイベントの更新処理を実行する。
	 * 
	 * @param pressKey 入力キー
	 * @param releaseKey リリースキー
	 */
	protected void updateKeyEvent( int pressKey, int releaseKey ) {
		_pressKey = pressKey;
		_releaseKey = releaseKey;
	}
	
	/**
	 * キーイベントを破棄する。
	 */
	protected void disposeKeyEvent() {
		_pressKey = _releaseKey = -1;
	}
	
	/**
	 * 引数のキーが押下されたか判定する。
	 * 
	 * @param keyCode キーコード
	 * @return true:押下された、false:押下されていない
	 */
	public boolean isPressKey( int keyCode ) {
		return keyCode == _pressKey;
	}
	
	/**
	 * 引数のキーが解除されたか判定する。
	 * 
	 * @param keyCode キーコード
	 * @return true:解除された、false:解除されていない
	 */
	public boolean isReleaseKey( int keyCode ) {
		return keyCode == _releaseKey;
	}
}
