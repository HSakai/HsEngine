package h.sakai.game.engine;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * ゲームエンジンクラス
 * 
 * @author H.Sakai
 * @version V1.00 2012/12/28 H.Sakai 新規作成
 */
public class Engine implements Runnable {
	
	/** ゲームウィンドウ */
	private GameWindow gameWindow = null;
	
	/** FPS設定 */
	private static final byte FPS = 60;
	
	/** FPS更新間隔 */
	private static final short FPS_TIME = 1000 / FPS;
	
	/** シーンID：開始シーン */
	protected static final byte SCENE_START = 1;
	
	/**
	 * コンストラクタ<br />
	 * 
	 * 設定ファイル等読み込み初期化処理を実行する。
	 */
	public Engine() {
		
		try {
			SystemProperty.read();
		} catch (Exception e) {
			System.out.println( "設定ファイルの読み込みに失敗しました。" );
		}
	}
	
	/**
	 * ゲームを起動する。
	 */
	public void launch() {
		
		gameWindow = new GameWindow();
		gameWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		gameWindow.setVisible( true );
		
		SceneDirector.runScene( SceneFactory.createScene( SCENE_START ) );
		
		Thread mainRoop = new Thread( this );
		mainRoop.start();
	}
	
	/**
	 * ゲームの起動メソッド
	 */
	public void run() {
		
		long time = System.currentTimeMillis();
		
		while ( true ) {
			
			long thisTime = System.currentTimeMillis();
			
			if ( thisTime - time < FPS_TIME ) {
				continue;
			}
			
			int len = SceneDirector.getLength();
			int last = len - 1;
			
			for ( int i = 0; i < len; i++ ) {
				Scene scene = SceneDirector.getScene( i );
				scene.getKeyEvent().updateKeyEvent( gameWindow.getPressKey(), gameWindow.getReleaseKey() );
				scene.processUpdaste( ( i < last ) );
				scene.getKeyEvent().disposeKeyEvent();
			}
			
			gameWindow.getGamePanel().repaint();
			gameWindow.disposeKey();
			
			time = thisTime;
		}
		
	}
	
	/**
	 * ゲームウィンドウクラス
	 * 
	 * @author H.Sakai
	 * @version V1.00 2012/12/28 H.Sakai 新規作成
	 */
	private class GameWindow extends JFrame implements KeyListener {
		
		private static final long serialVersionUID = -8907562095323143296L;
		
		/** ゲームパネル */
		private JPanel panel = null;
		
		/** 押下キー */
		private int _pressKey = -1;
		
		/** 解除キー */
		private int _releaseKey = -1;
		
		/**
		 * コンストラクタ<br />
		 * 
		 * ゲーム画面の初期化処理を実行する。
		 */
		public GameWindow() {
			
			try {
				setTitle( SystemProperty.getInstance().getProperty( "title" ) );
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "画面の初期化に失敗しました。");
				System.exit(0);
			}
			
			setResizable( false );
			
			this.panel = new GamePanel();
			
			Container container = getContentPane();
			container.add( this.panel );
			
			addKeyListener( this );
			
			// パネルサイズに自動調整
			pack();
			
			// 画面中央に配置
			setLocationRelativeTo( null );
		}
		
		/**
		 * ゲームの描画パネルを取得する。
		 * 
		 * @return ゲーム描画パネル
		 */
		public JPanel getGamePanel() {
			return this.panel;
		}
		
		/**
		 * @see KeyListener#keyPressed(KeyEvent)
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			_pressKey = e.getKeyCode();
		}
		
		/**
		 * @see KeyListener#keyReleased(KeyEvent)
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			_releaseKey = e.getKeyCode();
		}
		
		/**
		 * @see KeyListener#keyTyped(KeyEvent)
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
		/**
		 * 押下されたキー情報を取得する。
		 * 
		 * @return 押下キー
		 */
		protected int getPressKey() {
			return _pressKey;
		}
		
		/**
		 * 押下状態を解除されたキーを取得する。
		 * 
		 * @return 押下状態を解除されたキー
		 */
		protected int getReleaseKey() {
			return _releaseKey;
		}
		
		/**
		 * 入力キーを破棄する。
		 */
		public void disposeKey() {
			_pressKey = _releaseKey = -1;
		}
	}
	
	/**
	 * ウィンドウの更に内部のパネルクラス
	 * 
	 * @author H.Sakai
	 * @version V1.00 2012/12/28 H.Sakai 新規作成
	 */
	private class GamePanel extends JPanel {
		
		private static final long serialVersionUID = 8007104410351092814L;
		
		/**
		 * コンストラクタ
		 */
		public GamePanel() {
			
			try {
				setPreferredSize( new Dimension( Integer.parseInt( SystemProperty.getInstance().getProperty("width") ),
						Integer.parseInt( SystemProperty.getInstance().getProperty("height") ) ) );
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "画面の初期化に失敗しました。");
				System.exit(0);
			}
		}
		
		/**
		 * repainコール時に呼び出される。
		 */
		@Override
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			
			int len = SceneDirector.getLength();
			int last = len - 1;
			
			for ( int i = 0; i < len; i++ ) {
				Scene scene = SceneDirector.getScene( i );
				scene.processDraw( g, this, ( i < last ) );
			}
		}
	}
}
