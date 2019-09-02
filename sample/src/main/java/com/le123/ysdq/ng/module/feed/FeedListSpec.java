package com.le123.ysdq.ng.module.feed;

import com.agx.scaffold.JxFunc;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.EventHandler;
import com.facebook.litho.StateValue;
import com.facebook.litho.annotations.Event;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.OnUpdateState;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.State;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.LoadingEvent;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.SectionLifecycle;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnBindService;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.annotations.OnCreateService;
import com.facebook.litho.sections.annotations.OnRefresh;
import com.facebook.litho.sections.annotations.OnUnbindService;
import com.facebook.litho.sections.annotations.OnViewportChanged;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.widget.Progress;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.NonNull;

@GroupSectionSpec class FeedListSpec {
    @OnCreateChildren static Children onCreateChildren(SectionContext context
        , @State List<RowViewModel> rowViewModels
        , @State boolean hasMore) {

        // JxLogger.i("%d", rowViewModels.size());
        Children.Builder builder = Children.create();
        for (RowViewModel rowViewModel : rowViewModels) {
            builder.child(SingleComponentSection.create(context)
                .key(rowViewModel.id)
                .component(buildComponent(context, rowViewModel))
            );
        }
        if (hasMore) {
            builder.child(SingleComponentSection.create(context)
                .component(Column.create(context).child(
                    Progress.create(context)
                        .widthDip(40)
                        .heightDip(40)
                        .alignSelf(YogaAlign.CENTER)))
            );
        } else {
            builder.child(SingleComponentSection.create(context)
                .component(Column.create(context).child(
                    Text.create(context)
                        .text("No More Data")
                        .textSizeSp(14)
                        .marginDip(YogaEdge.VERTICAL, 20)
                        .alignSelf(YogaAlign.CENTER)
                    )
                )
            );
        }
        return builder.build();
    }

    private static Component buildComponent(SectionContext context, RowViewModel rowViewModel) {
        switch (rowViewModel.albums.size()) {

            case 2:
                return Feed2Column.create(context)
                    .rowViewModel(rowViewModel)
                    .build();
            case 3:
                return Feed3Column.create(context)
                    .rowViewModel(rowViewModel)
                    .build();
            case 1:
            default:
                return Feed1Column.create(context)
                    .album(rowViewModel.albums.get(0))
                    .imageRatio(16 / 9f)
                    .build();
        }
    }

    @OnCreateInitialState
    static void onCreateInitialState(SectionContext context
        , StateValue<List<RowViewModel>> rowViewModels
        , StateValue<Boolean> fetching
        , StateValue<Integer> pageIndex
        , StateValue<Boolean> hasMore
        , @Prop FetcherService service) {
        pageIndex.set(0);
        fetching.set(false);
        hasMore.set(true);
        rowViewModels.set(new ArrayList<>(1));

        fetching.set(true);
        List<RowViewModel> rowViewModelList = service.fetch(1);
        rowViewModels.set(rowViewModelList);
    }

    @OnUpdateState
    static void updateAlbums(@NonNull StateValue<List<RowViewModel>> rowViewModels
        , StateValue<Integer> pageIndex
        , @Param List<RowViewModel> got) {
        // JxLogger.i("");

        Integer value = pageIndex.get();
        if (value == null || value == 0) {
            rowViewModels.set(got);
        } else {
            List<RowViewModel> rowViewModelsNew = new ArrayList<>();
            List<RowViewModel> rowViewModelsOld = rowViewModels.get();
            if (rowViewModelsOld != null) {
                rowViewModelsNew.addAll(rowViewModelsOld);
            } else {
                // JxLogger.w("old is empty");
            }
            if (got.size() == 0) {
                // JxLogger.w("got is empty");
            } else {
                rowViewModelsNew.addAll(got);
                if (rowViewModelsNew.size() == 0) {
                    // JxLogger.w("new is empty");
                }
                rowViewModels.set(rowViewModelsNew);
            }
        }
    }

    @OnUpdateState
    static void updateFetching(StateValue<Boolean> fetching, @Param boolean got) {
        // // JxLogger.i("");
        fetching.set(got);
    }

    @OnUpdateState
    static void updateHasMore(StateValue<Boolean> hasMore, @Param boolean got) {
        // // JxLogger.i("");
        hasMore.set(got);
    }

    @OnUpdateState
    static void updatePageIndex(@NonNull StateValue<Integer> pageIndex, @Param int got) {
        // JxLogger.i("");
        pageIndex.set(got);
    }

    @OnCreateService
    static FetcherService onCreateService(SectionContext context, @Prop FetcherService service) {
        // JxLogger.i("");
        return service;
    }

    @OnBindService
    static void onBindService(SectionContext context, FetcherService service) {
        // JxLogger.i("");
        service.registerLoadingEvent(FeedList.onDataLoaded(context));
    }

    @OnUnbindService static void onUnBindService(SectionContext context, FetcherService service) {
        // JxLogger.i("");
        service.unregisterLoadingEvent();
    }

    @OnEvent(AlbumViewModels.class)
    static void onDataLoaded(SectionContext context
        , @State int pageIndex
        , @FromEvent List<RowViewModel> rows
        , @FromEvent boolean hasMore) {
        // JxLogger.i("%d %s", rows.size(), "" + hasMore);
        FeedList.updateAlbums(context, rows);
        FeedList.updateFetching(context, false);
        FeedList.updateHasMore(context, hasMore);
        // FeedList.updatePageIndex(context, pageIndex + 1);
        SectionLifecycle.dispatchLoadingEvent(context, false, LoadingEvent.LoadingState.SUCCEEDED, null);
    }

    @OnRefresh
    static void onRefresh(SectionContext context
        , FetcherService service
        , @State List<RowViewModel> rowViewModels
        , @State int pageIndex
        , @State boolean fetching
    ) {
        // JxLogger.i("");
        if (fetching) {
            // JxLogger.i("fetching is true");
            return;
        }
        FeedList.updateFetching(context, true);
        FeedList.updatePageIndex(context, 1);
        service.fetch(1);
    }

    @OnViewportChanged
    static void onViewportChanged(
        SectionContext context
        , int firstVisiblePosition
        , int lastVisiblePosition
        , int firstFullyVisibleIndex
        , int lastFullyVisibleIndex
        , int totalCount
        , @Prop FetcherService service
        , @State List<RowViewModel> rowViewModels
        , @State int pageIndex
        , @State boolean fetching
        , @State boolean hasMore
    ) {
        // // JxLogger.i("%d %s %s %d", pageIndex, "" + fetching, "" + hasMore, lastVisiblePosition);

        if (hasMore) {
            if (totalCount == rowViewModels.size() && !fetching && lastVisiblePosition > 0) {
                FeedList.updateFetching(context, true);
                FeedList.updatePageIndex(context, pageIndex + 1);
                service.fetch(pageIndex + 1);
            }
        } else {
            SectionLifecycle.dispatchLoadingEvent(context, false, LoadingEvent.LoadingState.SUCCEEDED, null);
        }
    }

    static abstract class FetcherService {
        boolean                       fetching = false;
        EventHandler<AlbumViewModels> dataModelEventHandler;

        void registerLoadingEvent(EventHandler<AlbumViewModels> dataModelEventHandler) {
            this.dataModelEventHandler = dataModelEventHandler;
            // // JxLogger.i("");
        }

        void unregisterLoadingEvent() {
            // // JxLogger.i("");
            dataModelEventHandler = null;
        }

        public List<RowViewModel> fetch(int pageIndex) {
//            if (fetching) {
//                return new ArrayList<>(1);
//            }
//            // // JxLogger.i("fetching:%s %s %d", this, "" + fetching, pageIndex);
//            fetching = true;
            return fetch_(pageIndex, new JxFunc.Action<Boolean>() {
                @Override public void yield(@androidx.annotation.NonNull Boolean value) {
//                    // // JxLogger.i("%s %d", "fetching: result=" + value, pageIndex);
//                    fetching = false;
                }
            });
        }

        abstract protected List<RowViewModel> fetch_(int pageIndex, JxFunc.Action<Boolean> callback);
    }

    @Event
    static class AlbumViewModels {
        boolean            hasMore = true;
        List<RowViewModel> rows    = new ArrayList<>(1);
    }

    @Builder
    static class RowViewModel {
        String id;
        @Builder.Default List<Album> albums = new ArrayList<>(1);
    }

    @Builder
    static class Album {
        String id;
        String url;
        String title;
        String subTitle;
        String description;
        String category;
        String tag;
        String tagColor;
        String episode;
    }
}
