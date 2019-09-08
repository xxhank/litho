package com.le123.ysdq.ng.module.home;

import com.agx.scaffold.JxTextUtils;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.RenderInfo;

import java.util.ArrayList;
import java.util.List;

@GroupSectionSpec
public class AlbumListSpec {
    private static List<Integer> generateData(int count) {
        List<Integer> data = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        return data;
    }

    @OnEvent(RenderEvent.class)
    static RenderInfo onRender(SectionContext c, @FromEvent Integer model) {
        return ComponentRenderInfo.create()
            .component(
                Album3Column.create(c)
                    .title(JxTextUtils.format("%d Title", model))
                    .subTitle(JxTextUtils.format("%d subTitle", model))
                    .url("https://s.yingshidq.com.cn/tp/tp20180821/q41.png")
                    .build()
            ).build();
    }

    @OnCreateChildren
    static Children onCreateChildren(SectionContext c) {
        return Children.create().child(
            DataDiffSection.<Integer>create(c)
                .data(generateData(32))
                .renderEventHandler(AlbumList.onRender(c))
        ).build();


//        Children.Builder builder = Children.create();
//        for (int i = 0; i < 32; i++) {
//            builder.child(
//                SingleComponentSection.create(c)
//                    .key(String.format(Locale.getDefault(), "%d", i))
//                    .component(Album3Column.create(c)
//                        .title(JxTextUtils.format("%d Title", i))
//                        .subTitle(JxTextUtils.format("%d subTitle", i))
//                        .url("https://s.yingshidq.com.cn/tp/tp20180821/q41.png")
//                        .build())
//            );
//        }
//        return builder.build();
    }
}
