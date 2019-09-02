package com.le123.ysdq.ng.module.feed;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agx.scaffold.JxTextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.Row;
import com.facebook.litho.StateValue;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnAttached;
import com.facebook.litho.annotations.OnBoundsDefined;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnDetached;
import com.facebook.litho.annotations.OnUpdateState;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.PropDefault;
import com.facebook.litho.annotations.State;
import com.facebook.litho.widget.Card;
import com.facebook.litho.widget.Image;
import com.facebook.litho.widget.Text;
import com.facebook.samples.litho.R;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaPositionType;

@LayoutSpec class PosterSpec {
    @PropDefault static final float imageRatio = 2 / 1f;

    @OnAttached
    static void onAttached(ComponentContext c, @Prop String url) {
        // JxLogger.i("");
        Target<Drawable> target = Glide.with(c.getAndroidContext()).load(url).into(
            new CustomTarget<Drawable>(100, 50) {
                @Override public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    Poster.updateDrawable(c, resource);
                }

                @Override public void onLoadCleared(@Nullable Drawable placeholder) {
                    Poster.updateGlideTarget(c, c, null);
                }
            }
        );
        Poster.updateGlideTarget(c, c, target);
    }

    @OnBoundsDefined
    static void onBoundsDefined(ComponentContext context, ComponentLayout layout, @Prop String url) {
        // JxLogger.i("");
    }

    @OnDetached
    static void onDetached(ComponentContext c, @Prop String url) {
        // JxLogger.i("");
        Poster.updateGlideTarget(c, c, null);
    }

    @OnUpdateState
    static void updateDrawable(StateValue<Drawable> drawable, @Param Drawable image) {
        // JxLogger.i("");
        drawable.set(image);
    }

    @OnUpdateState
    static void updateGlideTarget(StateValue<Target<Drawable>> target, @Param ComponentContext context, @Param Target<Drawable> value) {
        target.set(value);
        if (value == null) {
            Glide.with(context.getAndroidContext()).clear(target.get());
        }
    }

    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop(optional = true) float imageRatio,
                                    @Prop(optional = true) String topLeft,
                                    @Prop(optional = true) String topRight,
                                    @Prop(optional = true) String bottom,

                                    @State Drawable drawable,
                                    @State Target<Drawable> target) {

        Row.Builder builder = Row.create(c)
            .widthPercent(100).heightPercent(100)
            // .border(border(c, Color.GREEN))
            .child(
                Image.create(c)
                    // .border(border(c, Color.RED))
                    .widthPercent(100).heightPercent(100)
                    .drawable(drawable != null ? drawable
                                               : getDefaultPoster(c.getAndroidContext()))
                    .scaleType(drawable != null ? ImageView.ScaleType.CENTER_CROP
                                                : ImageView.ScaleType.FIT_XY)
            );

        if (!JxTextUtils.isEmptyOrNull(topLeft)) {
            builder.child(buildTopLeftMarkText(c, topLeft));
        }
        if (!JxTextUtils.isEmptyOrNull(topRight)) {
            builder.child(buildTopRightMarkText(c, topRight));
        }
        if (!JxTextUtils.isEmptyOrNull(bottom)) {
            builder.child(buildBottomMarkText(c, bottom));
        }

        return Card.create(c)
            .widthPercent(100)
            .aspectRatio(imageRatio)
            .content(builder)
            .clipToOutline(true)
            .clipChildren(true)
            .cornerRadiusDip(4)
            .build();

    }

    private static Drawable getDefaultPoster(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(R.drawable.cover_default);
        } else {
            return context.getResources().getDrawable(R.drawable.cover_default);
        }
    }

    private static Text.Builder buildTopLeftMarkText(ComponentContext c, String text) {
        return Text.create(c)
            .text(text)
            .textSizeSp(12)
            .textColor(0xffffffff)
            .backgroundColor(0x7F000000)
            .positionDip(YogaEdge.TOP, 0)
            .positionDip(YogaEdge.LEFT, 0)
            .paddingDip(YogaEdge.ALL, 5)
            .positionType(YogaPositionType.ABSOLUTE);
    }

    private static Text.Builder buildTopRightMarkText(ComponentContext c, String text) {
        return Text.create(c)
            .text(text)
            .textSizeSp(12)
            .backgroundColor(0x7F000000)
            .textColor(0xffffffff)
            .positionDip(YogaEdge.TOP, 0)
            .positionDip(YogaEdge.RIGHT, 0)
            .paddingDip(YogaEdge.ALL, 5)
            .positionType(YogaPositionType.ABSOLUTE);
    }

    private static Text.Builder buildBottomMarkText(ComponentContext c, String text) {
        return Text.create(c)
            .text(text)
            .textSizeSp(12)
            .backgroundRes(R.drawable.bg_gradient)
            .textColor(0xffffffff)
            .textAlignment(Layout.Alignment.ALIGN_OPPOSITE)
            .positionDip(YogaEdge.RIGHT, 0)
            .positionDip(YogaEdge.LEFT, 0)
            .positionDip(YogaEdge.BOTTOM, 0)
            .paddingDip(YogaEdge.ALL, 5)
            .positionType(YogaPositionType.ABSOLUTE);
    }
}
