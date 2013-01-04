package h.sakai.game.main.scene.game;

import java.awt.AlphaComposite;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import h.sakai.game.engine.Sound;
import h.sakai.game.engine.SystemProperty;
import h.sakai.game.engine.scene.AbstractModel;

/**
 * ゲームのモデルクラス(暫定でテトリス)
 * 
 * @author H.Sakai
 * @version V1.00 2012/12/31 新規作成
 */
public class Game extends AbstractModel {
	
	/** 画面幅 */
	private int _displayWidth = 0;
	
	/** 画面高さ */
	private int _displayHeight = 0;
	
	/** 背景画像 */
	private Image _bgImg = null;
	
	/** 背景ブロック画像 */
	private Image _bgBlockImg = null;
	
	/** ブロック画像 */
	private Image _blockImg = null;
	
	/** Press Enter画像 */
	private Image _pressEnterImg = null;
	
	/** Ready画像 */
	private Image _readyImg = null;
	
	/** GO!!!画像 */
	private Image _goImg = null;
	
	/** 背景ブロック座標 */
	private List< List< Integer > > _bgBlockPos = new ArrayList< List< Integer > >();
	
	/** 壁ブロック座標 */
	private List< List< Integer > > _wallBlockPos = new ArrayList< List< Integer > >();
	
	/** Press Enter座標 */
	private List< Integer > _pressEnterPos = new ArrayList< Integer >();
	
	/** Press Enter画像のα値 */
	private AlphaComposite _pressEnterAlpha = null;
	
	/** ReadyGo座標 */
	private List< Integer > _readyGoPos = new ArrayList< Integer >();
	
	/** ゲームオーバー座標 */
	private List< List< Integer > > _gameOverPos = new ArrayList< List< Integer > >();
	
	/** Press Enter描画開始時間 */
	private long _pressEnterStart = 0;
	
	/** Ready Go!!!描画開始時間 */
	private long _readyGoStart = 0;
	
	/** GO!!!画像描画フラグ */
	private boolean _isGo = false;
	
	/** ReadyGO!!!の描画制御シーケンス */
	private int _animationSq = 0;
	
	/** ゲームBGM */
	private Sound _bgm = null;
	
	/** ブロック固定SE */
	private Sound _fixSe = null;
	
	/** ブロック削除SE */
	private Sound _deleteSe = null;
	
	/** グリッド */
	private short[][] _grid = null;
	
	/** グリッドのブロックカラー */
	private short[][] _gridColor = null;
	
	/** ブロックオブジェクト */
	private Block _block = null;
	
	/** テトリス開始時間 */
	private long _tetrisStart = 0;
	
	/** ゲームオーバー開始時間 */
	private long _gameOverStart = 0;
	
	/** ゲームオーバーの描画逆再生フラグ */
	private boolean _isGameOverRev = false;
	
	/** ブロックの大きさ */
	protected static final byte BLOCK_SIZE = 24;
	
	/** 壁ブロックの抜き取り位置 */
	protected static final short WALL_BLOCK_POS = ( short ) ( 24 * 7 );
	
	/** グリッドサイズ：X方向 */
	protected static final byte GRID_X = 10;
	
	/** グリッドサイズ：Y方向 */
	protected static final byte GRID_Y = 16;
	
	/** Press Enterのα点滅時間 */
	private static final short PRESS_ENTER_DRAW_TIME = 1000;
	
	/** Ready GO!!!のアニメーション時間 */
	private static final short READY_GO_DRAW_TIME = 300;
	
	/** ブロックの移動時間 */
	private static final short BLOCK_MOVE_TIME = 500;
	
	/** ゲームオーバーの詰み上がり時間 */
	private static final short GAME_OVER_DRAW_TIME = 100;
	
	/**
	 * コンストラクタ
	 * 
	 * @param startSqNo 開始シーン番号
	 */
	public Game(int startSqNo) {
		super( startSqNo );
		try {
			_displayWidth = Integer.parseInt( SystemProperty.getInstance().getProperty( "width" ) );
			_displayHeight = Integer.parseInt( SystemProperty.getInstance().getProperty( "height" ) );
		} catch (Exception e) {
			System.out.println( "設定ファイルの読み込みに失敗しました。" );
		}
	}
	
	/**
	 * @see h.sakai.game.engine.scene.AbstractModel#init()
	 */
	@Override
	public void init() {
		
		// グリッド初期化
		_grid = new short[ GRID_Y ][ GRID_X ];
		_gridColor = new short[ GRID_Y ][ GRID_X ];
		
		// ファイルパスルート
		String filePathRoot = new File( "" ).getAbsolutePath();
		
		// 背景読み込み
		ImageIcon bgIcon = new ImageIcon( filePathRoot + "/res/image/bg.png" );
		_bgImg = bgIcon.getImage();
		
		// 背景ブロック読み込み
		ImageIcon bgBlockIcon = new ImageIcon( filePathRoot + "/res/image/bgBlock.png" );
		_bgBlockImg = bgBlockIcon.getImage();
		
		// 背景ブロック座標設定
		for ( int i = 0, y = 0; i < 16; i++, y += 24 ) {
			for ( int j = 0, x = 24; j < 10; j++, x += 24 ) {
				List< Integer > pos = new ArrayList<Integer>();
				pos.add( x );
				pos.add( y );
				_bgBlockPos.add( pos );
			}
		}
		
		// ブロック画像読み込み
		ImageIcon blockIcon = new ImageIcon( filePathRoot + "/res/image/block.png" );
		_blockImg = blockIcon.getImage();
		
		// 壁ブロック座標設定
		for ( int i = 0, y = 0; i < 17; i++, y+= 24 ) {
			for ( int j = 0, x = 0; j < 12; j++, x += 24 ) {
				
				// 最終行以外は両端のみ
				if ( j > 0
						&& j < 11
						&& i < 16 ) {
					continue;
				}
				
				List< Integer > pos = new ArrayList<Integer>();
				pos.add( x );
				pos.add( y );
				_wallBlockPos.add( pos );
			}
		}
		
		// Press Enter画像読み込み
		ImageIcon pressEnterIcon = new ImageIcon( filePathRoot + "/res/image/press.png" );
		_pressEnterImg = pressEnterIcon.getImage();
		_pressEnterStart = System.currentTimeMillis();
		_pressEnterPos.add( ( _displayWidth - _pressEnterImg.getWidth( null ) ) >> 1 );
		_pressEnterPos.add( ( _displayHeight - _pressEnterImg.getHeight( null ) ) >> 1 );
		
		// Ready画像読み込み
		ImageIcon readyIcon = new ImageIcon( filePathRoot + "/res/image/ready.png" );
		_readyImg = readyIcon.getImage();
		
		// GO!!!画像読み込み
		ImageIcon goIcon = new ImageIcon( filePathRoot + "/res/image/go.png" );
		_goImg = goIcon.getImage();
		
		// BGM読み込み
		_bgm = new Sound( filePathRoot + "/res/sound/bgm/Galaxy_loop.wav", true );
		
		// SE読み込み
		_fixSe = new Sound( filePathRoot + "/res/sound/se/kachi.wav" );
		_deleteSe = new Sound( filePathRoot + "/res/sound/se/delete.wav" );
	}
	
	/**
	 * スタートシーケンスのフレーム更新処理を実行する。
	 * 
	 * @param isPressEnter エンターキー押下フラグ
	 */
	protected void onUpdateStart( boolean isPressEnter ) {
		
		if ( !isPressEnter ) {
			
			long time = System.currentTimeMillis();
			long lap = time - _pressEnterStart;
			if ( lap > PRESS_ENTER_DRAW_TIME ) {
				_pressEnterStart = time;
				_pressEnterAlpha = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1.0f );
			} else {
				int rate = 100 - ( int ) lap * 100 / PRESS_ENTER_DRAW_TIME;
				float alpha = rate / 100.0f;
				_pressEnterAlpha = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha );
			}
			
		} else {
			
			moveSq( SceneGame.SQ_READY_GO );
			
			// アニメーション初期化
			_readyGoPos.removeAll( _readyGoPos );
			_readyGoPos.add( -_readyImg.getWidth( null ) );
			_readyGoPos.add( ( _displayHeight - _readyImg.getHeight( null ) ) >> 1 );
			_readyGoPos.add( -_goImg.getWidth( null ) );
			_readyGoPos.add( ( _displayHeight - _goImg.getHeight( null ) ) >> 1 );
			
			_readyGoStart = System.currentTimeMillis();
			
			_animationSq = 0;
			_isGo = false;
		}
	}
	
	/**
	 * テトリス準備シーケンスのフレーム更新処理を実行する。
	 */
	protected void onUpdateReadyGo() {
		
		long time = System.currentTimeMillis();
		long lap = time - _readyGoStart;
		
		if ( lap > READY_GO_DRAW_TIME + ( ( _animationSq == 1 ) ? 700 : 0 ) ) {
			
			_readyGoStart = time;
			
			if ( !_isGo ) {
				
				if ( _animationSq == 0 ) {
					_animationSq = 1;
				} else if ( _animationSq == 1 ) {
					_animationSq = 2;
				} else {
					_isGo = true;
					_animationSq = 0;
				}
				
			} else {
				
				if ( _animationSq == 0 ) {
					_animationSq = 1;
				} else if ( _animationSq == 1 ) {
					_animationSq = 2;
				} else {
					
					moveSq( SceneGame.SQ_TETRIS );
					
					// テトリス画面の初期化
					_bgm.play();
					_block = new Block( this );
					_tetrisStart = System.currentTimeMillis();
				}
			}
			
		} else {
			
			int rate = ( int ) lap * 100 / READY_GO_DRAW_TIME;
			
			Image img = ( _isGo ) ? _goImg : _readyImg;
			int xIdx = ( _isGo ) ? 2 : 0;
			
			if ( _animationSq == 0 ) {
				
				int startPos = -img.getWidth( null );
				int endPos = ( _displayWidth - img.getWidth( null ) ) >> 1;
				
				int x = ( endPos - startPos ) * rate / 100 + startPos;
				_readyGoPos.set( xIdx, x );
				
			} else if ( _animationSq == 2 ) {
				
				int startPos = ( _displayWidth - img.getWidth( null ) ) >> 1;
				int endPos = _displayWidth;
				
				int x = ( endPos - startPos ) * rate / 100 + startPos;
				_readyGoPos.set( xIdx, x );
			}
		}
	}
	
	/**
	 * テトリスのフレーム更新処理を実行する。
	 * 
	 * @param keyCode キーコード
	 */
	protected void onUpdateTetris( int keyCode ) {
		
		long time = System.currentTimeMillis();
		
		if ( keyCode != 0 ) {
			
			int dir = Block.DOWN;
			switch ( keyCode ) {
				case KeyEvent.VK_LEFT:
					dir = Block.LEFT;
					break;
				case KeyEvent.VK_RIGHT:
					dir = Block.RIGHT;
					break;
				case KeyEvent.VK_DOWN:
					dir = Block.DOWN;
					break;
				case KeyEvent.VK_SPACE:
					dir = Block.TURN;
					break;
			}
			
			execTetris( dir );
			
		} else {
			if ( time - _tetrisStart > BLOCK_MOVE_TIME ) {
				execTetris( Block.DOWN );
				_tetrisStart = time;
			}
		}
	}
	
	/**
	 * テトリスのゲーム実行処理。
	 * 
	 * @param dir 移動方向
	 */
	private void execTetris( int dir ) {
		
		if ( dir == Block.TURN ) {
			_block.turn();
		} else {
			
			boolean isFixed = _block.move( dir, this );
			
			if ( isFixed ) {
				
				// ブロック固定SE再生
				_fixSe.play();
				
				// 次のブロックへ
				_block = new Block( this );
			}
			
			// そろった行があれば削除
			deleteLine();
			
			// 積みあがり判定⇒ゲームオーバーかどうか
			if ( isStacked() ) {
				// ゲームオーバーシーケンスへ
				moveSq( SceneGame.SQ_GAMEOVER );
				_gameOverStart = System.currentTimeMillis();
				_gameOverPos.removeAll( _gameOverPos );
				_isGameOverRev = false;
			}
		}
	}
	
	/**
	 * ゲームオーバーシーケンスのフレーム更新処理を実行する。
	 */
	protected void onUpdateGameOver() {
		
		long time = System.currentTimeMillis();
		int lap = ( int ) ( time - _gameOverStart );
		
		if ( lap > GAME_OVER_DRAW_TIME ) {
			
			if ( !_isGameOverRev ) {
				
				if ( _gameOverPos.size() == GRID_Y ) {
					_isGameOverRev = true;
					_grid = new short[ GRID_Y ][ GRID_X ];
					_gridColor = new short[ GRID_Y ][ GRID_X ];
				} else {
					List< Integer > pos = new ArrayList<Integer>();
					for ( int i = 0; i < GRID_X; i++ ) {
						pos.add( i );
					}
					_gameOverPos.add( pos );
				}
				
			} else {
				
				if ( _gameOverPos.size() == 0 ) {
					moveSq( SceneGame.SQ_START );
					_pressEnterStart = System.currentTimeMillis();
				} else {
					_gameOverPos.remove( _gameOverPos.size() - 1 );
				}
			}
			
			_gameOverStart = time;
		}
	}
	
	/**
	 * ブロックを移動できるか調べる
	 * 
	 * @param newPos ブロックの移動先座標
	 * @param block ブロック
	 * @return 移動できたらtrue
	 */
	public boolean isMovable( Point newPos, short[][] block ) {
		
		for ( int y = 0; y < Block.MAX_Y; y++ ) {
			
			for ( int x = 0; x < Block.MAX_X; x++ ) {
				
				// ブロックは4x4の配列で構成されているため、構成部分を調べる
				if ( block[y][x] == 1 ) {
					
					// そのマスが画面の上端外のとき
					if ( newPos.y + y < 0 ) {
						
						// ブロックのあるマスが壁のある0列目以下または
						// MAX_X-1列目以上に移動しようとしてる場合は移動できない
						if ( !( newPos.x + x > -1 && newPos.x + x < GRID_X ) ) {
							return false;
						}
						
					} else if (newPos.y + y < GRID_Y
							&& newPos.x + x > -1
							&& newPos.x + x < GRID_X
							&& _grid[ newPos.y + y ][ newPos.x + x ] == 1 ) {
						// 移動先にすでにブロックがある場合は移動できない
						return false;
					} else if ( !( newPos.x + x > -1 && newPos.x + x < GRID_X ) ) {
						return false;
					} else if ( newPos.y + y > GRID_Y - 1 ) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 落ちきったブロックをグリッドに固定する
	 * 
	 * @param pos ブロックの位置
	 * @param block ブロック
	 * @param color ブロックの色
	 */
	protected void fixBlock( Point pos, short[][] block, byte imageNo ) {
		
		for ( int y = 0; y < Block.MAX_Y; y++ ) {
			
			for ( int x = 0; x < Block.MAX_X; x++ ) {
				
				if ( block[ y ][ x ] == 1 ) {
					
					if ( pos.y + y < 0 ) {
						continue;
					}
					
					_grid[ pos.y + y ][ pos.x + x ] = 1;
					_gridColor[ pos.y + y ][ pos.x + x ] = imageNo;
				}
			}
		}
	}
	
	/**
	 * 行が揃ったら揃った分だけ削除する。
	 */
	protected void deleteLine() {
		
		boolean isDelete = false;
		
		for ( int y = 0; y < GRID_Y; y++ ) {
			
			int count = 0;
			for ( int x = 0; x < GRID_X; x++ ) {
				// ブロックがある列を数える
				if (_grid[ y ][ x ] == 1)
					count++;
			}
			
			// そろった行が見つかった
			if ( count == GRID_X ) {
				
				// その行を消去
				for ( int x = 1; x < GRID_X; x++ ) {
					_grid[ y ][ x ] = 0;
					if ( !isDelete ) {
						isDelete = true;
					}
				}
				
				// それより上の行を落とす
				for ( int ty = y; ty > 0; ty-- ) {
					for (int tx = 0; tx < GRID_X; tx++ ) {
						_grid[ ty ][ tx ] = _grid[ ty - 1 ][ tx ];
						_gridColor[ ty ][tx] = _gridColor[ ty - 1 ][ tx ];
					}
				}
			}
		}
		
		if ( isDelete ) {
			_deleteSe.play();
		}
	}
	
	/**
	 * ブロックが積み上がってるか
	 * 
	 * @return 最上行まで積み上がってたらtrue
	 */
	protected boolean isStacked() {
		
		boolean isStack = _grid[ 0 ][ 4 ] == 1;
		if ( !isStack ) {
			isStack = _grid[ 0 ][ 5 ] == 1;
		}
		
		return isStack;
	}
	
	/**
	 * @see h.sakai.game.engine.scene.AbstractModel#updateAnimation()
	 */
	@Override
	public void updateAnimation() {
		
	}
	
	/**
	 * 背景画像を取得する。
	 * 
	 * @return 背景画像
	 */
	protected Image getBg() {
		return _bgImg;
	}
	
	/**
	 * 背景ブロック画像を取得する。
	 * 
	 * @return 背景ブロック画像
	 */
	protected Image getBgBlock() {
		return _bgBlockImg;
	}
	
	/**
	 * ブロック画像を取得する。
	 * 
	 * @return ブロック画像
	 */
	protected Image getBlock() {
		return _blockImg;
	}
	
	/**
	 * Press Enter画像を取得する。
	 * 
	 * @return Press Enter画像
	 */
	protected Image getPressEnter() {
		return _pressEnterImg;
	}
	
	/**
	 * Ready画像を取得する。
	 * 
	 * @return Ready画像
	 */
	protected Image getReady() {
		return _readyImg;
	}
	
	/**
	 * GO!!!画像を取得する。
	 * 
	 * @return GO!!!画像
	 */
	protected Image getGo() {
		return _goImg;
	}
	
	/**
	 * Press Enter画像のα値を取得する。
	 * 
	 * @return Press Enter画像のα値
	 */
	protected AlphaComposite getPressEnterAlpha() {
		return _pressEnterAlpha;
	}
	
	/**
	 * 背景ブロック座標を取得する。
	 * 
	 * @return 背景ブロック座標
	 */
	protected List< List< Integer > > getBgBlockPos() {
		return _bgBlockPos;
	}
	
	/**
	 * 壁ブロック座標を取得する。
	 * 
	 * @return 壁ブロック座標
	 */
	protected List< List< Integer > > getWallBlockPos() {
		return _wallBlockPos;
	}
	
	/**
	 * Press Enter座標を取得する。
	 * 
	 * @return Press Enter座標
	 */
	protected List< Integer > getPressEnterPos() {
		return _pressEnterPos;
	}
	
	/**
	 * Ready GO!!!画像座標を取得する。
	 * 
	 * @return Ready GO!!!画像座標
	 */
	protected List< Integer > getReadyGoPos() {
		return _readyGoPos;
	}
	
	/**
	 * ゲームオーバー座標を取得する。
	 * 
	 * @return ゲームオーバー座標
	 */
	protected List< List< Integer > > getGameOverPos() {
		return _gameOverPos;
	}
	
	/**
	 * グリッドを取得する。
	 * 
	 * @return グリッド
	 */
	protected short[][] getGrid() {
		return _grid;
	}
	
	/**
	 * グリッドの色を取得する。
	 * 
	 * @return グリッド色
	 */
	protected short[][] getGridColor() {
		return _gridColor;
	}
	
	/**
	 * GO!!!画像描画フラグを取得する。
	 * 
	 * @return GO!!!画像描画フラグ
	 */
	protected boolean isGo() {
		return _isGo;
	}
	
	/**
	 * ブロックオブジェクトを取得する。
	 * 
	 * @return ブロックオブジェクト
	 */
	protected Block getBlockObj() {
		return _block;
	}
}
