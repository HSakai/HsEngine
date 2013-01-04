package h.sakai.game.main;

import h.sakai.game.engine.Engine;

/**
 * 起動クラス
 * 
 * @author H.Sakai
 * @version V1.00 2012/12/28 H.Sakai 新規作成
 */
public class Launch {
	
	public static void main( String... args ) {
		
		// ゲームの起動
		Engine gameEngine = new Engine();
		gameEngine.launch();
	}
}
