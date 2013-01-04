package h.sakai.game.main.scene.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.List;

import h.sakai.game.engine.Scene;

/**
 * ゲームシーンクラス(暫定でテトリス)
 * 
 * @author H.Sakai
 * @version V1.00 2012/12/31 新規作成
 */
public class SceneGame extends Scene {
	
	/** 本シーンのモデルクラス */
	private Game _model = null;
	
	/** シーケンス番号：スタートシーケンス */
	protected static final byte SQ_START    = 1;
	
	/** シーケンス番号：テトリス準備シーケンス */
	protected static final byte SQ_READY_GO = 2;
	
	/** シーケンス番号：テトリスシーケンス */
	protected static final byte SQ_TETRIS   = 3;
	
	/** シーケンス番号：ゲームオーバーシーケンス */
	protected static final byte SQ_GAMEOVER = 4;
	
	/**
	 * コンストラクタ<br />
	 * 
	 * モデルのインスタンス作成
	 */
	public SceneGame() {
		_model = new Game( SQ_START );
	}
	
	/**
	 * @see h.sakai.game.engine.Scene#processInitialize()
	 */
	@Override
	public void processInitialize() {
		_model.init();
	}
	
	/**
	 * @see h.sakai.game.engine.Scene#processUpdaste(boolean)
	 */
	@Override
	public void processUpdaste( boolean isEnterSub ) {
		
		switch ( _model.getSqNo() ) {
			case SQ_START:
				_model.onUpdateStart( isPressKey( KeyEvent.VK_ENTER ) );
				break;
			case SQ_READY_GO:
				_model.onUpdateReadyGo();
				break;
			case SQ_TETRIS:
				
				int keyCode = 0;
				
				if ( isPressKey( KeyEvent.VK_LEFT ) ) {
					keyCode = KeyEvent.VK_LEFT;
				} else if ( isPressKey( KeyEvent.VK_RIGHT ) ) {
					keyCode = KeyEvent.VK_RIGHT;
				} else if ( isPressKey( KeyEvent.VK_DOWN ) ) {
					keyCode = KeyEvent.VK_DOWN;
				} else if ( isPressKey( KeyEvent.VK_SPACE ) ) {
					keyCode = KeyEvent.VK_SPACE;
				}
				
				_model.onUpdateTetris( keyCode );
				break;
			case SQ_GAMEOVER:
				_model.onUpdateGameOver();
				break;
		}
	}
	
	/**
	 * @see h.sakai.game.engine.Scene#processDraw(Graphics, boolean)
	 */
	@Override
	public void processDraw(Graphics g, ImageObserver imgObserver, boolean isEnterSub) {
		
		// 背景画像
		g.drawImage( _model.getBg(), 0, 0, imgObserver );
		
		// 背景ブロック描画
		for ( int i = 0, len = _model.getBgBlockPos().size(); i < len; i++ ) {
			List< Integer > pos = _model.getBgBlockPos().get( i );
			g.drawImage( _model.getBgBlock(), pos.get( 0 ), pos.get( 1 ), imgObserver );
		}
		
		// 壁ブロック描画
		for ( int i = 0, len = _model.getWallBlockPos().size(); i < len; i++ ) {
			List< Integer > pos = _model.getWallBlockPos().get( i );
			int x = pos.get( 0 );
			int y = pos.get( 1 );
			g.drawImage( _model.getBlock(), x, y , x + Game.BLOCK_SIZE, y + Game.BLOCK_SIZE,
					Game.WALL_BLOCK_POS, 0, Game.WALL_BLOCK_POS + Game.BLOCK_SIZE, Game.BLOCK_SIZE, imgObserver );
		}
		
		if ( _model.getSqNo() == SQ_START ) {
			drawEnterKey( g, imgObserver );
		} else if ( _model.getSqNo() == SQ_READY_GO ) {
			drawReadyGo( g, imgObserver );
		} else if ( _model.getSqNo() == SQ_TETRIS ) {
			drawTetris( g, imgObserver );
		} else if ( _model.getSqNo() == SQ_GAMEOVER ) {
			drawTetris( g, imgObserver );
			drawGameOver( g, imgObserver );
		}
	}
	
	/**
	 * PressKeyEnter画像の描画を実行する。
	 * 
	 * @param g グラフィックオブジェクト
	 * @param imgObserver 描画監視オブジェクト
	 */
	private void drawEnterKey( Graphics g, ImageObserver imgObserver ) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite( _model.getPressEnterAlpha() );
		g.drawImage( _model.getPressEnter(), _model.getPressEnterPos().get( 0 ), _model.getPressEnterPos().get( 1 ), imgObserver );
	}
	
	/**
	 * Ready⇒GO!!!の描画を実行する。
	 * 
	 * @param g グラフィックオブジェクト
	 * @param imgObserver 描画監視オブジェクト
	 */
	private void drawReadyGo( Graphics g, ImageObserver imgObserver ) {
		
		List< Integer > pos = _model.getReadyGoPos();
		
		int x = ( _model.isGo() ) ? pos.get( 2 ) : pos.get( 0 );
		int y = ( _model.isGo() ) ? pos.get( 3 ) : pos.get( 1 );
		Image img = ( _model.isGo() ) ? _model.getGo() : _model.getReady();
		
		g.drawImage( img, x, y, imgObserver );
	}
	
	/**
	 * テトリスの描画処理を実行する。
	 * 
	 * @param g グラフィックオブジェクト
	 * @param imgObserver 描画監視オブジェクト
	 */
	private void drawTetris( Graphics g, ImageObserver imgObserver ) {
		
		// グリッドの描画
		short[][] grid = _model.getGrid();
		short[][] gridColor = _model.getGridColor();
		
		for ( int y = 0; y < Game.GRID_Y; y++ ) {
			
			for ( int x = 0; x < Game.GRID_X; x++ ) {
				
				if (grid[ y ][ x ] == 1) {
					
					g.drawImage(_model.getBlock(), ( x + 1 ) * Game.BLOCK_SIZE, y * Game.BLOCK_SIZE,
							( x + 1 ) * Game.BLOCK_SIZE + Game.BLOCK_SIZE, y * Game.BLOCK_SIZE + Game.BLOCK_SIZE,
							gridColor[ y ][ x ] * Game.BLOCK_SIZE, 0, gridColor[ y ][ x ] * Game.BLOCK_SIZE + Game.BLOCK_SIZE,
							Game.BLOCK_SIZE, imgObserver);
				}
			}
		}
		
		// ブロックの描画
		Block block = _model.getBlockObj();
		
		for ( int y = 0; y < Block.MAX_Y; y++ ) {
			for ( int x = 0; x < Block.MAX_X; x++ ) {
				
				if ( block.getBlock()[ y ][ x ] == 1 ) {
					g.drawImage(_model.getBlock(), ( block.getPos().x + 1 + x ) * Game.BLOCK_SIZE, ( block.getPos().y + y ) * Game.BLOCK_SIZE,
							( block.getPos().x + 1 + x ) * Game.BLOCK_SIZE + Game.BLOCK_SIZE, ( block.getPos().y + y ) * Game.BLOCK_SIZE + Game.BLOCK_SIZE,
							block.getImageNo() * Game.BLOCK_SIZE, 0, block.getImageNo() * Game.BLOCK_SIZE + Game.BLOCK_SIZE, Game.BLOCK_SIZE, imgObserver);
				}
			}
		}
	}
	
	/**
	 * ゲームオーバーシーケンスの描画を実行する。
	 * 
	 * @param g グラフィックオブジェクト
	 * @param imgObserver 描画監視オブジェクト
	 */
	private void drawGameOver( Graphics g, ImageObserver imgObserver ) {
		
		int yMax = Game.GRID_Y - 1;
		
		for ( int i = 0, len = _model.getGameOverPos().size(), y = yMax; i < len; i++, y-- ) {
			for ( int x = 1; x < Game.GRID_X + 1; x++ ) {
				g.drawImage( _model.getBlock(), x * Game.BLOCK_SIZE, y * Game.BLOCK_SIZE, x * Game.BLOCK_SIZE + Game.BLOCK_SIZE,
						y * Game.BLOCK_SIZE + Game.BLOCK_SIZE, Game.WALL_BLOCK_POS, 0, Game.WALL_BLOCK_POS + Game.BLOCK_SIZE, Game.BLOCK_SIZE, imgObserver );
			}
		}
	}
}
