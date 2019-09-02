package com.le123.ysdq.ng.module.feed;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;

@LayoutSpec class FeedItemSpec {
    @OnCreateLayout
    static Component onCreateLayout(ComponentContext context
        , @Prop FeedListSpec.Album album
        , @Prop float imageRatio) {
        return Column.create(context)
            //.border(LithoUtil.border(context, Color.GRAY))
            .paddingDip(YogaEdge.BOTTOM, 10)
            .child(Poster.create(context)
                .marginDip(YogaEdge.HORIZONTAL, -2)
                .url(album.url).imageRatio(imageRatio)
                .topLeft(album.category)
                .topRight(album.tag)
                .bottom(album.episode)
            )
            .child(Text.create(context)
                .text(album.title)
                .textSizeSp(16)
                .marginDip(YogaEdge.TOP, 6)
                .marginDip(YogaEdge.BOTTOM, 5)
                .textColor(0xE6000000)
            )
            .child(Text.create(context)
                .text(album.subTitle)
                .textSizeSp(14)
                .marginDip(YogaEdge.BOTTOM, 5)
                .textColor(0xFF9b9b9b)
            )
            .child(Text.create(context)
                .text(album.description)
                .textSizeSp(12)
                .marginDip(YogaEdge.BOTTOM, 6)
                .textColor(0xFF9b9b9b)
            )
            .build();
    }
}
