package h.sakai.game.engine.scene.start;

import h.sakai.game.engine.scene.AbstractModel;

/**
 * スタートシーンのモデルクラス
 * 
 * @author Hiroyuki Sakai
 * @version V1.00 2012/12/31 新規作成
 */
public class Start extends AbstractModel {
	
	/**
	 * コンストラクタ
	 * 
	 * @param startSqNo 開始シーケンス
	 */
	public Start( int startSqNo ) {
		super(startSqNo);
	}
	
	/**
	 * 暫定のフレーム更新処理
	 */
	public void onUpdate() {
		this.moveSq( SceneStart.MOVE_GAME );
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public void updateAnimation() {
		
	}
}
