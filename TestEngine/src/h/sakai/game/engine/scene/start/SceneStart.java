package h.sakai.game.engine.scene.start;

import h.sakai.game.engine.Scene;
import h.sakai.game.main.common.AppConst;

/**
 * スタートシーンクラス
 * 
 * @author H.Sakai
 * @version V1.00 2012/12/31 新規作成
 */
public class SceneStart extends Scene {
	
	/** モデルクラス */
	private Start _model = null;
	
	/** シーケンス番号：フレーム更新 */
	protected static final byte UPDATE_SCENE = 1;
	
	/** シーケンス番号：ゲームシーンへ移動 */
	protected static final byte MOVE_GAME = 2;
	
	/**
	 * コンストラクタ
	 */
	public SceneStart() {
		_model = new Start( UPDATE_SCENE );
	}
	
	@Override
	public void processInitialize() {
		
	}
	
	@Override
	public void processUpdaste(boolean isEnterSub) {
		switch ( _model.getSqNo() ) {
			case UPDATE_SCENE:
				_model.onUpdate();
				break;
			case MOVE_GAME:
				this.jumpScene( AppConst.SCENE_GAME );
				break;
		}
	}
}
