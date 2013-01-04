package h.sakai.game.engine;

import java.util.Stack;

/**
 * シーンの管理クラス
 * 
 * @author H.Sakai
 * @version V1.00 2012/12/31 H.Sakai 新規作成
 */
public final class SceneDirector {
	
	/** シーンのスタック */
	private static Stack<Scene> sceneStack = new Stack<Scene>();
	
	/**
	 * 引数のシーン以外を全て破棄し、引数のシーンをトップシーンとして起動する。
	 * 
	 * @param scene シーンクラス
	 */
	protected static void runScene( Scene scene ) {
		
		// シーンのスタックが全て無くなるまで破棄処理
		while ( sceneStack.size() > 0 ) {
			Scene pastScene = sceneStack.pop();
			pastScene.dispose();
		}
		
		pushScene( scene );
	}
	
	/**
	 * 一番手前のシーンを破棄し、そこに新たなシーンを挿入する。
	 * 
	 * @param scene シーンクラス
	 */
	protected static void replaceScene( Scene scene ) {
		Scene topScene = sceneStack.pop();
		topScene.dispose();
		pushScene( scene );
	}
	
	/**
	 * シーンを挿入する。
	 * 
	 * @param scene シーンクラス
	 */
	protected static void pushScene( Scene scene ) {
		
		sceneStack.push( scene );
		
		scene.processInitialize();
		scene.processBegin();
	}
	
	/**
	 * トップシーンを破棄し、一つ前のシーンを再開する。
	 */
	protected static void returnScene() {
		
		Scene topScene = sceneStack.pop();
		topScene.dispose();
		
		Scene backScene = sceneStack.get( sceneStack.size() - 1 );
		backScene.processBegin();
	}
	
	/**
	 * 実行中のシーン数を取得する。
	 * 
	 * @return シーン数
	 */
	protected static int getLength() {
		return sceneStack.size();
	}
	
	/**
	 * 指定インデックスのシーンを取得する。
	 * 
	 * @param index 要素番号
	 * @return シーンクラス
	 */
	protected static Scene getScene( int index ) {
		return sceneStack.get( index );
	}
}
