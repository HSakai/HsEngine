package h.sakai.game.main.scene.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ブロック構成クラス
 * 
 * @author Hiroyuki Sakai
 * @version V1.00 2013/01/03 新規作成
 */
public class Block {
	
	/** 色番号 */
	private byte _imageNo = 0;
	
	/** ブロック構成情報 */
	private short[][] _block;
	
	/** 座標情報 */
	private Point _pos;
	
	/** ゲームモデル */
	private final Game _game;
	
	/** ブロックサイズ：X方向 */
	protected static final byte MAX_X = 4;
	
	/** ブロックサイズ：Y方向 */
	protected static final byte MAX_Y = 4;
	
	/** 移動方向：左 */
	protected static final byte LEFT  = 0;
	
	/** 移動方向：右 */
	protected static final byte RIGHT = 1;
	
	/** 移動方向：下 */
	protected static final byte DOWN  = 2;
	
	/** 移動方向：回転 */
	protected static final byte TURN  = 3;
	
	/** ブロックの構成情報 */
	private static final List< short[][] > BLOCK_INFO = new ArrayList<short[][]>(); {
		
		// 縦棒ブロック
		short[][] barBlock = new short[ MAX_Y ][ MAX_X ];
		barBlock[0][1] = 1;
		barBlock[1][1] = 1;
		barBlock[2][1] = 1;
		barBlock[3][1] = 1;
		
		BLOCK_INFO.add( barBlock );
		
		// Z型ブロック
		short[][] zShapeBlock = new short[ MAX_Y ][ MAX_X ];
		zShapeBlock[1][2] = 1;
		zShapeBlock[2][1] = 1;
		zShapeBlock[2][2] = 1;
		zShapeBlock[3][1] = 1;
		
		BLOCK_INFO.add( zShapeBlock );
		
		// 四角ブロック
		short[][] squareBlock = new short[ MAX_Y ][ MAX_X ];
		squareBlock[1][1] = 1;
		squareBlock[1][2] = 1;
		squareBlock[2][1] = 1;
		squareBlock[2][2] = 1;
		
		BLOCK_INFO.add( squareBlock );
		
		// L型ブロック
		short[][] lShapeBlock = new short[ MAX_Y ][ MAX_X ];
		lShapeBlock[1][1] = 1;
		lShapeBlock[1][2] = 1;
		lShapeBlock[2][2] = 1;
		lShapeBlock[3][2] = 1;
		
		BLOCK_INFO.add( lShapeBlock );
		
		// 逆Z型ブロック
		short[][] reverseZShapeBlock = new short[ MAX_Y ][ MAX_X ];
		reverseZShapeBlock[1][1] = 1;
		reverseZShapeBlock[2][1] = 1;
		reverseZShapeBlock[2][2] = 1;
		reverseZShapeBlock[3][2] = 1;
		
		BLOCK_INFO.add( reverseZShapeBlock );
		
		// T型ブロック
		short[][] tShapeBlock = new short[ MAX_Y ][ MAX_X ];
		tShapeBlock[1][1] = 1;
		tShapeBlock[2][1] = 1;
		tShapeBlock[2][2] = 1;
		tShapeBlock[3][1] = 1;
		
		BLOCK_INFO.add( tShapeBlock );
		
		// 逆L型ブロック
		short[][] reverseLShapeBlock = new short[ MAX_Y ][ MAX_X ];
		reverseLShapeBlock[1][1] = 1;
		reverseLShapeBlock[1][2] = 1;
		reverseLShapeBlock[2][1] = 1;
		reverseLShapeBlock[3][1] = 1;
		
		BLOCK_INFO.add( reverseLShapeBlock );
	}
	
	/**
	 * コンストラクタ<br>
	 * 
	 * ブロックの初期化処理を実行する。
	 * 
	 * @param game ゲームモデル
	 */
	public Block( Game game ) {
		Random random = new Random();
		int blockNo = random.nextInt( 7 );
		_block = BLOCK_INFO.get( blockNo );
		_imageNo = ( byte ) blockNo;
		_pos = new Point( 4, -4 );
		_game = game;
	}
	
	/**
	 * 引数のdirの方向にブロックを移動。
	 * 
	 * @param dir 方向
	 * @param game ゲームモデル
	 * @return ブロックが固定されたらtrue
	 */
	public boolean move( int dir, Game game ) {
		
		switch (dir) {
			case LEFT:
				Point newPos = new Point( _pos.x - 1, _pos.y );
				if (_game.isMovable( newPos, _block ) ) {
					_pos = newPos;
				}
				break;
			case RIGHT:
				newPos = new Point(_pos.x+1, _pos.y);
				if (_game.isMovable( newPos, _block ) ) {
					_pos = newPos;
				}
				break;
			case DOWN:
				newPos = new Point( _pos.x, _pos.y + 1 );
				if ( _game.isMovable( newPos, _block ) ) {
					_pos = newPos;
				} else {
					// ブロックをボードに固定する
					_game.fixBlock( _pos, _block, _imageNo );
					// 固定されたらtrueを返す
					return true;
				}
				break;
		}
		
		return false;
	}
	
	/**
	 * ブロックの回転処理を実行する。
	 */
	protected void turn() {
		
		short[][] turnedBlock = new short[ MAX_Y ][ MAX_X ];
		
		// 回転したブロック
		for ( int y = 0; y < MAX_Y; y++ ) {
			for ( int x = 0; x < MAX_X; x++ ) {
				turnedBlock[ x ][ MAX_Y - 1 - y ] = _block[ y ][ x ];
			}
		}
		
		// 回転可能な場合、ブロックの入れ替え
		if ( _game.isMovable( _pos, turnedBlock ) ) {
			_block = turnedBlock;
		}
	}
	
	/**
	 * ブロックの構成情報を取得する。
	 * 
	 * @return ブロックの構成情報
	 */
	protected short[][] getBlock() {
		return _block;
	}
	
	/**
	 * ブロックの座標を取得する。
	 * 
	 * @return ブロック座標
	 */
	protected Point getPos() {
		return _pos;
	}
	
	/**
	 * 色番号を取得する。
	 * 
	 * @return
	 */
	protected int getImageNo() {
		return _imageNo;
	}
}
