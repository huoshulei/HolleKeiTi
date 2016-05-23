package edu.hsl.hollekeiti.phone.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by Administrator on 2016/05/05.
 */
public class MusicService extends Service {
    MediaPlayer cPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AssetManager assetManager = getAssets();//获得assets文件夹的资源
        try {
            //加载音乐资源
            AssetFileDescriptor fileDescriptor = assetManager.openFd("music/om.mp3");
            cPlayer = new MediaPlayer();
            cPlayer.setDataSource(fileDescriptor.getFileDescriptor()
                    , fileDescriptor.getStartOffset()
                    , fileDescriptor.getLength());

            cPlayer.prepare();//准备播放
            cPlayer.start();//播放
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        cPlayer.stop();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
