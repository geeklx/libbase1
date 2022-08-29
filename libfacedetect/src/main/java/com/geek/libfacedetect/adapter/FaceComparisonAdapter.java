package com.geek.libfacedetect.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.geek.libfacedetect.R;
import com.geek.libfacedetect.beans.FaceBean2;

public class FaceComparisonAdapter extends BaseQuickAdapter<FaceBean2, BaseViewHolder> {

    public FaceComparisonAdapter() {
        super(R.layout.item_facecomparison);
    }

    @Override
    protected void convert(BaseViewHolder helper, FaceBean2 item) {
//        switch (helper.getLayoutPosition() % 3) {
//            case 0:
//                helper.setImageResource(R.id.brademo1_img, R.drawable.animation_img1);
//                break;
//            case 1:
//                helper.setImageResource(R.id.brademo1_img, R.drawable.animation_img2);
//                break;
//            case 2:
//                helper.setImageResource(R.id.brademo1_img, R.drawable.animation_img3);
//                break;
//            default:
//                break;
//        }
        helper.setText(R.id.tv_name, item.getName());
//        helper.setText(R.id.brademo1_tweetText,item.getText());
        helper.setText(R.id.tv_content, item.getImg());
//        ((TextView) helper.getView(R.id.brademo1_tweetText)).setText(SpannableStringUtils.getInstance(mContext.getApplicationContext()).getBuilder(item.getText()).
//                append("点击查看博客链接").setClickSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Uri url = Uri.parse("http://blog.51cto.com/liangxiao");
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(url);
//                mContext.startActivity(intent);
//            }
//        }).create());
//        ((TextView) helper.getView(R.id.brademo1_tweetText)).setMovementMethod(ClickableMovementMethod.getInstance());
//        helper.getView(R.id.brademo1_tweetText).setFocusable(false);
//        helper.getView(R.id.brademo1_tweetText).setClickable(true);
//        helper.getView(R.id.brademo1_tweetText).setLongClickable(false);

        helper.addOnClickListener(R.id.tv_name);
        helper.addOnClickListener(R.id.tv_content);
    }


}
