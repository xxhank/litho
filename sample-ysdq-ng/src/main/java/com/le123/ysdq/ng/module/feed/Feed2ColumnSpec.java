package com.le123.ysdq.ng.module.feed;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.target.Target;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.State;
import com.facebook.yoga.YogaEdge;

@LayoutSpec class Feed2ColumnSpec {
    @OnCreateLayout
    static Component onCreateLayout(ComponentContext context
        , @Prop FeedListSpec.RowViewModel rowViewModel
        , @State Drawable drawable
        , @State Target<Drawable> target) {

        Row.Builder builder = Row.create(context)
            .paddingDip(YogaEdge.HORIZONTAL, 10);
        builder
            .child(FeedItem.create(context)
                .album(rowViewModel.albums.get(0))
                .imageRatio(16 / 9f)
                .marginDip(YogaEdge.RIGHT, 5)
            )
            .child(FeedItem.create(context)
                .album(rowViewModel.albums.get(1))
                .marginDip(YogaEdge.LEFT, 5)
                .imageRatio(16 / 9f))
        //.border(PosterSpec.border(context, Color.GRAY))
        ;
        return builder.build();
    }
}
