package com.le123.ysdq.ng.module.feed;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.target.Target;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.State;
import com.facebook.yoga.YogaEdge;

@LayoutSpec class Feed1ColumnSpec {
    @OnCreateLayout
    static Component onCreateLayout(ComponentContext context
        , @Prop FeedListSpec.Album album
        , @Prop float imageRatio
        , @State Drawable drawable
        , @State Target<Drawable> target) {
        return FeedItem.create(context)
            .paddingDip(YogaEdge.HORIZONTAL, 10)
            .album(album)
            .imageRatio(imageRatio)
            .build();
    }
}
