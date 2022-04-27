package com.geek.libgoodview.ordinarygoodview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.geek.libgoodview.R;

/**
 * @author fosung
 */
public class GoodViewActivity extends AppCompatActivity {

    GoodView mGoodView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_view);
        mGoodView = new GoodView(this);
    }

    public void good(View view) {
        ((ImageView) view).setImageResource(R.mipmap.good_checked);
        mGoodView.setText("+1");
        mGoodView.show(view);
    }

    public void good2(View view) {
        ((ImageView) view).setImageResource(R.mipmap.good_checked);
        mGoodView.setImage(getResources().getDrawable(R.mipmap.good_checked));
        mGoodView.show(view);
    }

    public void collection(View view) {
        ((ImageView) view).setImageResource(R.mipmap.collection_checked);
        mGoodView.setTextInfo("收藏成功", Color.parseColor("#f66467"), 12);
        mGoodView.show(view);
    }

    public void bookmark(View view) {
        ((ImageView) view).setImageResource(R.mipmap.bookmark_checked);
        mGoodView.setTextInfo("收藏成功", Color.parseColor("#ff941A"), 12);
        mGoodView.show(view);
    }

    public void reset(View view) {
        ((ImageView) findViewById(R.id.good)).setImageResource(R.mipmap.good);
        ((ImageView) findViewById(R.id.good2)).setImageResource(R.mipmap.good);
        ((ImageView) findViewById(R.id.collection)).setImageResource(R.mipmap.collection);
        ((ImageView) findViewById(R.id.bookmark)).setImageResource(R.mipmap.bookmark);
        mGoodView.reset();
    }
}