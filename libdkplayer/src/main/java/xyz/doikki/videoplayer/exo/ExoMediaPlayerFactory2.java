package xyz.doikki.videoplayer.exo;

import android.content.Context;

import xyz.doikki.videoplayer.player.PlayerFactory;

public class ExoMediaPlayerFactory2 extends PlayerFactory<ExoMediaPlayer2> {

    public static ExoMediaPlayerFactory2 create() {
        return new ExoMediaPlayerFactory2();
    }

    @Override
    public ExoMediaPlayer2 createPlayer(Context context) {
        return new ExoMediaPlayer2(context);
    }
}