package xyz.doikki.videoplayer.ijk;

import android.content.Context;

import xyz.doikki.videoplayer.player.PlayerFactory;

public class IjkPlayerFactory2 extends PlayerFactory<IjkPlayer2> {

    public static IjkPlayerFactory2 create() {
        return new IjkPlayerFactory2();
    }

    @Override
    public IjkPlayer2 createPlayer(Context context) {
        return new IjkPlayer2(context);
    }
}