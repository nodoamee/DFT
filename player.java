package sample;

import javax.sound.sampled.*;

/**
 * Created by abedaigorou on 16/03/03.
 */
public class player extends Thread
{
    private static final int BITS = 8;
    private static final int HZ = 32000;
    private static final int MONO = 1;

    // リニアPCM 16bit 8000Hz x １秒
    private byte[] voice = new byte[ HZ * BITS / 8 * MONO ];
    private SourceDataLine source;

    public boolean g_bPlayer = false;

    // コンストラクタ
    player()
    {
        g_bPlayer = true;

        try
        {
            // オーディオフォーマットの指定
            AudioFormat linear = new AudioFormat( HZ, BITS, MONO, true, false );

            // ソースデータラインを取得
            DataLine.Info info = new DataLine.Info( SourceDataLine.class, linear );
            source = (SourceDataLine) AudioSystem.getLine(info);

            // ソースデータラインを開く
            source.open( linear );

            // スピーカー出力開始
            source.start();

        } catch (LineUnavailableException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // スレッド実行
    public void run()
    {
        while( true )
        {
            if( !g_bPlayer ) return;

            // スピーカーに音声データを出力
            source.write( voice, 0, voice.length );

            // 一応、ウエイト
            try{
                Thread.sleep( 100 );
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // データ設定
    public void setVoice( byte[] b )
    {
        voice = b;
    }

    // 終了
    public void end()
    {
        g_bPlayer = false;

        // ソースデータラインを停止
        source.stop();

        // ソースデータラインを閉じる
        source.close();
    }
}