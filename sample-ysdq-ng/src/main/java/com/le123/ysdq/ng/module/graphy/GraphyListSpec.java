/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.le123.ysdq.ng.module.graphy;

import com.agx.scaffold.JxLogger;
import com.agx.scaffold.JxTextUtils;
import com.facebook.litho.Column;
import com.facebook.litho.StateValue;
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
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.OnCheckIsSameContentEvent;
import com.facebook.litho.sections.common.OnCheckIsSameItemEvent;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.widget.Progress;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

@GroupSectionSpec class GraphyListSpec {
    private static final String MAIN_SCREEN = "main_screen";

    @OnCreateInitialState
    static void onCreateInitialState(SectionContext context
        , StateValue<List<AlbumViewModels.RowViewModel>> rowViewModels
        , StateValue<Boolean> fetching
        , StateValue<Integer> pageIndex
        , StateValue<Boolean> hasMore
        , @Prop FetcherService service) {
        pageIndex.set(0);
        fetching.set(false);
        hasMore.set(true);
        rowViewModels.set(new ArrayList<>(1));

        fetching.set(true);
        List<AlbumViewModels.RowViewModel> rowViewModelList = service.fetch(1);
        rowViewModels.set(rowViewModelList);
    }

    @OnUpdateState
    static void updateAlbums(@NonNull StateValue<List<AlbumViewModels.RowViewModel>> rowViewModels
        , StateValue<Integer> pageIndex
        , @Param List<AlbumViewModels.RowViewModel> got) {
        JxLogger.i("");

        Integer value = pageIndex.get();
        if (value == null || value == 1) {
            rowViewModels.set(got);
        } else {
            List<AlbumViewModels.RowViewModel> rowViewModelsNew = new ArrayList<>();
            List<AlbumViewModels.RowViewModel> rowViewModelsOld = rowViewModels.get();
            if (rowViewModelsOld != null) {
                rowViewModelsNew.addAll(rowViewModelsOld);
            } else {
                JxLogger.w("old is empty");
            }
            if (got.size() == 0) {
                JxLogger.w("got is empty");
            } else {
                rowViewModelsNew.addAll(got);
                if (rowViewModelsNew.size() == 0) {
                    JxLogger.w("new is empty");
                }
                rowViewModels.set(rowViewModelsNew);
            }
        }
    }

    @OnUpdateState
    static void updateFetching(StateValue<Boolean> fetching, @Param boolean got) {
        JxLogger.i("");
        fetching.set(got);
    }

    @OnUpdateState
    static void updateHasMore(StateValue<Boolean> hasMore, @Param boolean got) {
        // JxLogger.i("");
        hasMore.set(got);
    }

    @OnUpdateState
    static void updatePageIndex(@NonNull StateValue<Integer> pageIndex, @Param int got) {
        JxLogger.i("");
        pageIndex.set(got);
    }

    @OnCreateService
    static FetcherService onCreateService(SectionContext context, @Prop FetcherService service) {
        JxLogger.i("");
        return service;
    }

    @OnBindService
    static void onBindService(SectionContext context, FetcherService service) {
        JxLogger.i("");
        service.registerLoadingEvent(GraphyList.onDataLoaded(context));
    }

    @OnUnbindService static void onUnBindService(SectionContext context, FetcherService service) {
        JxLogger.i("");
        service.unregisterLoadingEvent();
    }

    @OnEvent(AlbumViewModels.class)
    static void onDataLoaded(SectionContext context
        , @State int pageIndex
        , @FromEvent List<AlbumViewModels.RowViewModel> rows
        , @FromEvent boolean hasMore) {
        JxLogger.i("%d %s", rows.size(), "" + hasMore);
        GraphyList.updateAlbums(context, rows);
        GraphyList.updateFetching(context, false);
        GraphyList.updateHasMore(context, hasMore);
        // GraphyList.updatePageIndex(context, pageIndex + 1);
        SectionLifecycle.dispatchLoadingEvent(context, false, LoadingEvent.LoadingState.SUCCEEDED, null);
    }

    @OnEvent(RenderEvent.class)
    static RenderInfo onRender(SectionContext context, @FromEvent AlbumViewModels.RowViewModel model) {
        return model.createRenderInfo(context);
    }

    @OnEvent(OnCheckIsSameItemEvent.class)
    static boolean onCheckIsSameItem(SectionContext context, @FromEvent AlbumViewModels.RowViewModel previousItem
        , @FromEvent AlbumViewModels.RowViewModel nextItem) {
        return JxTextUtils.equals(previousItem.id, nextItem.id);
    }

    @OnEvent(OnCheckIsSameContentEvent.class)
    static boolean onCheckIsSameContent(SectionContext context, @FromEvent AlbumViewModels.RowViewModel previousItem
        , @FromEvent AlbumViewModels.RowViewModel nextItem) {
        return JxTextUtils.equals(previousItem.id, nextItem.id);
    }

    @OnCreateChildren
    static Children onCreateChildren(SectionContext context
        , @State List<AlbumViewModels.RowViewModel> rowViewModels
        , @State int pageIndex
        , @State boolean fetching
        , @State boolean hasMore) {
        Children.Builder builder = Children.create();
//        for (AlbumViewModels.RowViewModel rowViewModel : rowViewModels) {
//            builder.child(SingleComponentSection.create(context)
//                .key(rowViewModel.id)
//                .component(rowViewModel.createComponent(context))
//            );
//        }
        builder.child(DataDiffSection.<AlbumViewModels.RowViewModel>create(context)
            .data(rowViewModels)
            .renderEventHandler(GraphyList.onRender(context))
            .onCheckIsSameItemEventHandler(GraphyList.onCheckIsSameItem(context))
            .onCheckIsSameContentEventHandler(GraphyList.onCheckIsSameContent(context)));

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

    @OnRefresh
    static void onRefresh(SectionContext context
        , FetcherService service
        , @State List<AlbumViewModels.RowViewModel> rowViewModels
        , @State int pageIndex
        , @State boolean fetching
    ) {
        if (fetching) {
            return;
        }
        GraphyList.updateFetching(context, true);
        GraphyList.updatePageIndex(context, 1);
        service.fetch(1);
    }

    @OnViewportChanged
    static void onViewportChanged(
        SectionContext context
        , int firstVisiblePosition
        , int lastVisiblePosition
        , int totalCount // the total count of items this ListComponent is displaying in the list
        , int firstFullyVisibleIndex
        , int lastFullyVisibleIndex
        , @Prop FetcherService service
        , @State List<AlbumViewModels.RowViewModel> rowViewModels
        , @State int pageIndex
        , @State boolean fetching
        , @State boolean hasMore
    ) {
        JxLogger.i("%d %s %s %d %d %d", pageIndex, "" + fetching, "" + hasMore, lastVisiblePosition, rowViewModels.size(), totalCount);

        if (hasMore) {
            if ((totalCount - 1) == rowViewModels.size() && !fetching) {
                GraphyList.updateFetching(context, true);
                GraphyList.updatePageIndex(context, pageIndex + 1);
                service.fetch(pageIndex + 1);
            }
        } else {
            SectionLifecycle.dispatchLoadingEvent(context, false, LoadingEvent.LoadingState.SUCCEEDED, null);
        }
    }
}
