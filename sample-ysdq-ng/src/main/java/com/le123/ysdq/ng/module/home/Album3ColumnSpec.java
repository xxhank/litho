package com.le123.ysdq.ng.module.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.litho.Border;
import com.facebook.litho.Column;
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
import com.facebook.litho.annotations.State;
import com.facebook.litho.widget.Card;
import com.facebook.litho.widget.Image;
import com.facebook.litho.widget.Text;
import com.facebook.samples.litho.R;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaPositionType;


/**
 * @author wangchao9
 */
@LayoutSpec
public class Album3ColumnSpec {

    @OnBoundsDefined
    static void onBoundsDefined(ComponentContext context, ComponentLayout layout) {
    }

    @OnAttached
    static void onAttached(ComponentContext c, @Prop String url) {
        Log.d("Album3ColumnSpec", "onAttached");
        new CustomTarget<Drawable>() {
            @Override public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

            }

            @Override public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        };
        Target<Drawable> target = Glide.with(c.getAndroidContext()).load(url).into(new SimpleTarget<Drawable>() {
            @Override public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Album3Column.onUpdateState(c, resource);
            }
        });
        Album3Column.onUpdateGlideTarget(c, c, target);
    }

    @OnDetached
    static void onDetached(ComponentContext c, @Prop String url) {
        Log.d("Album3ColumnSpec", "onDetached");
        Album3Column.onUpdateGlideTarget(c, c, null);
    }

    @OnUpdateState
    static void onUpdateState(StateValue<Drawable> drawable, @Param Drawable image) {
        drawable.set(image);
    }

    @OnUpdateState
    static void onUpdateGlideTarget(StateValue<Target<Drawable>> target, @Param ComponentContext context, @Param Target<Drawable> value) {
        target.set(value);
        if (value == null) {
            Glide.with(context.getAndroidContext()).clear(target.get());
        }
    }


    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop String title,
                                    @Prop String subTitle,
                                    @Prop String url,
                                    @State Drawable drawable,
                                    @State Target<Drawable> target) {
        return Column.create(c)
            .child(Card.create(c)
                .border(border(c, Color.GRAY))

                .content(
                    Row.create(c)
                        .widthPercent(100).heightPercent(100)
                        // .border(border(c, Color.GREEN))
                        .child(
                            Image.create(c)
                                .widthPercent(100).heightPercent(100)
                                .drawable(drawable != null ? drawable
                                                           : getDefaultPoster(c.getAndroidContext()))
                                .scaleType(drawable != null ? ImageView.ScaleType.CENTER_CROP
                                                            : ImageView.ScaleType.FIT_XY)
                        )
                        .child(buildTopLeftMarkText(c))
                        .child(buildTopRightMarkText(c))
                        .child(buildBottomMarkText(c))
                )
                .widthPercent(100)
                .aspectRatio(16f / 9f)
                .cornerRadiusDip(4)
            )
            .child(buildTitleText(c, title))
            .child(buildSubTitleText(c, subTitle))
            .build();

    }

    private static Drawable getDefaultPoster(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(R.drawable.cover_default);
        } else {
            return context.getResources().getDrawable(R.drawable.cover_default);
        }
    }

    private static Text.Builder buildTopLeftMarkText(ComponentContext c) {
        return Text.create(c)
            .text("top left")
            .textSizeSp(12)
            .textColor(0xffffffff)
            .positionDip(YogaEdge.TOP, 10)
            .positionDip(YogaEdge.LEFT, 10)
            .positionType(YogaPositionType.ABSOLUTE);
    }

    private static Text.Builder buildTopRightMarkText(ComponentContext c) {
        return Text.create(c)
            .text("top right")
            .textSizeSp(12)
            .textColor(0xffffffff)
            .positionDip(YogaEdge.TOP, 10)
            .positionDip(YogaEdge.RIGHT, 10)
            .positionType(YogaPositionType.ABSOLUTE);
    }

    private static Text.Builder buildBottomMarkText(ComponentContext c) {
        return Text.create(c)
            .text("bottom")
            .textSizeSp(12)
            .textColor(0xffffffff)

            .positionDip(YogaEdge.RIGHT, 10)
            .positionDip(YogaEdge.LEFT, 10)
            .positionDip(YogaEdge.BOTTOM, 10)
            .positionType(YogaPositionType.ABSOLUTE);
    }

    private static Text.Builder buildSubTitleText(ComponentContext c, @Prop String subTitle) {
        return Text.create(c)
            .text(subTitle)
            .widthPercent(100)
            .textColor(0xff9b9b9b)
            .marginDip(YogaEdge.TOP, 10)
            .marginDip(YogaEdge.BOTTOM, 2)
            .border(border(c, Color.GRAY))
            .textSizeSp(12);
    }

    private static Text.Builder buildTitleText(ComponentContext c, @Prop String title) {
        return Text.create(c)
            .text(title)
            .textColor(0xE6000000)
            .marginDip(YogaEdge.TOP, 10)
            .marginDip(YogaEdge.BOTTOM, 6)
            .widthPercent(100)
            .border(border(c, Color.GRAY))
            .textSizeSp(15);
    }

    private static Border border(ComponentContext c, int color) {
        return Border.create(c)
            .widthDip(YogaEdge.ALL, 1)
            .color(YogaEdge.ALL, color)
            .build();
    }
}
// http://s.yingshidq.com.cn/img/sign/2019/07/26/1377508864

/*
final Component component = MyComponent.create(componentContext)
    .title("My title")
    .imageUri(Uri.parse("http://example.com/myimage"))
    .build();
LithoView view = LithoView.create(context, component);
 */