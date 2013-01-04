package h.sakai.game.engine;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * サウンドファイルのラップクラス<br>
 * 
 * 再生の一時停止は後日追加予定。
 * 
 * @author H.Sakai
 * @version V1.00 2013/01/02 新規作成
 */
public class Sound {
	
	/** サウンドデータ */
	private Clip _soundClip = null;
	
	/** ループフラグ */
	private boolean _isLoop = false;
	
	/**
	 * コンストラクタ<br>
	 * 
	 * ループ再生せずに初期化
	 * 
	 * @param path サウンドファイルパス
	 */
	public Sound( String path ) {
		initialize( path, false );
	}
	
	/**
	 * コンストラクタ<br />
	 * 
	 * 引数のファイルパスのサウンドデータを読み込む。
	 * 
	 * @param path サウンドファイルパス
	 * @param isLoop ループ再生フラグ
	 */
	public Sound( String path, boolean isLoop ) {
		initialize( path, isLoop );
	}
	
	/**
	 * 初期化処理を実行する。
	 * 
	 * @param path サウンドファイルパス
	 * @param isLoop
	 */
	private void initialize( String path, boolean isLoop ) {
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream( new File( path ) );
			AudioFormat format = ais.getFormat();
			DataLine.Info info = new DataLine.Info( Clip.class, format );
			_soundClip = ( Clip ) AudioSystem.getLine( info );
			_soundClip.open( ais );
			_isLoop = isLoop;
			ais.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * サウンドファイルの再生処理を実行する。
	 */
	public void play() {
		if ( _isLoop ) {
			_soundClip.loop( -1 );
		} else {
			
			_soundClip.start();
			_soundClip.setFramePosition(0);
		}
	}
	
	/**
	 * サウンドファイルの再生を中止する。
	 */
	public void stop() {
		_soundClip.stop();
	}
}
