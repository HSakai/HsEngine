package h.sakai.game.engine;

import h.sakai.game.engine.scene.start.SceneStart;
import h.sakai.game.main.common.AppConst;
import h.sakai.game.main.scene.game.SceneGame;

/**
 * シーン生成クラス
 * 
 * @author Hiroyuki Sakai
 * @version V1.00 2012/12/31 新規作成
 */
public final class SceneFactory {
	
	/**
	 * シーンクラスを作成する。
	 * 
	 * @param sceneId シーンID
	 * @return シーンクラス
	 */
	protected static Scene createScene( int sceneId ) {
		
		switch ( sceneId ) {
			case Engine.SCENE_START:
				return new SceneStart();
			case AppConst.SCENE_GAME:
				return new SceneGame();
			default:
				return null;
		}
	}
}
