package com.le123.ysdq.ng.module.graphy;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.Event;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.yoga.YogaEdge;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;


@Event
class AlbumViewModels {
    boolean            hasMore = true;
    List<RowViewModel> rows    = new ArrayList<>(1);

    @Builder
    static class RowViewModel {
        String id;
        @Builder.Default List<Album> albums = new ArrayList<>(1);

        public RenderInfo createRenderInfo(ComponentContext c) {
            return ComponentRenderInfo.create()
                .component(createComponent(c))
                .build();
        }

        public Component createComponent(ComponentContext c) {
            switch (albums.size()) {
                case 2:
                    return Row.create(c)
                        .child(GraphyItemCard.create(c)
                            .imageRatio(16 / 9f)
                            .artist(albums.get(0))
                            .marginDip(YogaEdge.LEFT, 10)
                            .marginDip(YogaEdge.RIGHT, 5)
                        )
                        .child(GraphyItemCard.create(c)
                            .artist(albums.get(1))
                            .imageRatio(16 / 9f)
                            .marginDip(YogaEdge.LEFT, 5)
                            .marginDip(YogaEdge.RIGHT, 10)
                        )
                        .build();
                case 3:
                    return Row.create(c)
                        .child(GraphyItemCard.create(c).artist(albums.get(0))
                            .imageRatio(3 / 4f)

                            .marginDip(YogaEdge.LEFT, 10)
                            .marginDip(YogaEdge.RIGHT, 5)
                        )
                        .child(GraphyItemCard.create(c).artist(albums.get(1))
                            .imageRatio(3 / 4f)
                            .marginDip(YogaEdge.LEFT, 5)
                            .marginDip(YogaEdge.RIGHT, 5)
                        )
                        .child(GraphyItemCard.create(c).artist(albums.get(2))
                            .imageRatio(3 / 4f)
                            .marginDip(YogaEdge.LEFT, 5)
                            .marginDip(YogaEdge.RIGHT, 10)
                        )

                        .build();
                default:
                    return Row.create(c)
                        .child(GraphyItemCard.create(c).artist(albums.get(0))
                            .imageRatio(16 / 9f)
                            .marginDip(YogaEdge.LEFT, 10)
                            .marginDip(YogaEdge.RIGHT, 10)
                        )
                        .build();
            }
            //return GraphyItemCard.create(c).artist(albums.get(0)).build();
        }
    }
}

