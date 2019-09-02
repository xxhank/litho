package com.le123.ysdq.ng.module.feed;

import com.facebook.litho.Border;
import com.facebook.litho.ComponentContext;
import com.facebook.yoga.YogaEdge;

class LithoUtil {
    public static Border border(ComponentContext c, int color) {
        return Border.create(c)
            .widthDip(YogaEdge.ALL, 1)
            .color(YogaEdge.ALL, color)
            .build();
    }
}
