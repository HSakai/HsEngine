package h.sakai.game.engine.scene;

/**
 * モデルのベースとなるクラス
 * 
 * @author Hiroyuki Sakai
 * @version V1.00 2012/12/31 新規作成
 */
public abstract class AbstractModel {

	/** 画面シーケンス番号 */
	private int sqNo = 0;
	
	/** 一つ前のシーケンス番号 */
	private int pastSqNo = 0;
	
	/**
	 * コンストラクタ<br />
	 * 
	 * 強制的に開始シーケンス番号を設定させる。
	 * 
	 * @param startSqNo 開始シーケンス番号
	 */
	public AbstractModel( int startSqNo ) {
		sqNo = startSqNo;
	}
	
	/**
	 * シーケンスの移動を実行する。
	 * 
	 * @param fromSqNo 移動前シーケンス
	 * @param toSqNo 移動後シーケンス
	 */
	public void moveSq( int fromSqNo, int toSqNo ) {
		sqNo = toSqNo;
		pastSqNo = fromSqNo;
	}
	
	/**
	 * シーケンス番号を取得する。
	 * 
	 * @return シーケンス番号
	 */
	public int getSqNo() {
		return this.sqNo;
	}
	
	/**
	 * 一つ前のシーケンス番号を取得する。
	 * 
	 * @return 一つ前のシーケンス番号
	 */
	public int getPastSqNo() {
		return this.pastSqNo;
	}
	
	/**
	 * シーケンスの移動を実行する。
	 * 
	 * @param toSqNo 移動後シーケンス
	 */
	public void moveSq( int toSqNo ) {
		moveSq( sqNo, toSqNo);
	}
	
	/**
	 * シーンの共通初期化処理を実行する。
	 */
	abstract public void init();
	
	/**
	 * アニメーションの更新処理。
	 */
	abstract public void updateAnimation();
}
