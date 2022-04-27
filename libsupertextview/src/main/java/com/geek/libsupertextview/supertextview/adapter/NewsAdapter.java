package com.geek.libsupertextview.supertextview.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.geek.libsupertextview.R;
import com.geek.libsupertextview.SuperTextView;
import com.geek.libsupertextview.supertextview.bean.NewsBean;
import com.squareup.picasso.Picasso;

/**
 * @author allen
 * @date 2016/10/31
 */

public class NewsAdapter extends BaseQuickAdapter<NewsBean, BaseViewHolder> {

    public NewsAdapter() {
        super(R.layout.item);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean newsBean) {
        SuperTextView supertextview = helper.itemView.findViewById(R.id.item_super_tv);
        supertextview.setLeftTopString(newsBean.getTitle()).setLeftBottomString(newsBean.getTime());
        Picasso.with(mContext).load(newsBean.getImgUrl()).placeholder(R.drawable.head_default
        ).into(supertextview.getLeftIconIV());
    }
}
