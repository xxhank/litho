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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agx.scaffold.JxLogger;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.StateValue;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.OnUpdateState;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.State;
import com.facebook.litho.sections.LoadEventsHandler;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.yoga.YogaEdge;

import java.util.ArrayList;
import java.util.List;

@LayoutSpec class GraphyRootSpec {
    private static final String MAIN_SCREEN = "main_screen";

    @OnCreateInitialState
    static void onCreateInitialState(ComponentContext context
        , StateValue<List<AlbumViewModels.RowViewModel>> rowViewModels
        , StateValue<Boolean> fetching
        , StateValue<Integer> pageIndex
        , StateValue<Boolean> hasMore
        , @Prop FetcherService service) {
        JxLogger.i("");

        pageIndex.set(0);
        fetching.set(false);
        hasMore.set(true);
        rowViewModels.set(new ArrayList<>(1));

        fetching.set(true);
        service.registerLoadingEvent(GraphyRoot.onDataLoaded(context));
        List<AlbumViewModels.RowViewModel> rowViewModelList = service.fetch(1);
        rowViewModels.get().addAll(rowViewModelList);
    }

    @OnEvent(AlbumViewModels.class)
    static void onDataLoaded(ComponentContext context, @FromEvent List<AlbumViewModels.RowViewModel> rows, @FromEvent boolean hasMore) {
        GraphyRoot.updateAlbums(context, rows);
        GraphyRoot.updateFetching(context, false);
        GraphyRoot.updateHasMore(context, hasMore);
    }

    @OnUpdateState
    static void updateAlbums(@lombok.NonNull StateValue<List<AlbumViewModels.RowViewModel>> rowViewModels
        , StateValue<Integer> pageIndex
        , @Param List<AlbumViewModels.RowViewModel> got) {
        // JxLogger.i("");

        Integer value = pageIndex.get();
        if (value == null || value == 0) {
            rowViewModels.set(got);
        } else {
            rowViewModels.get().addAll(got);
//            List<AlbumViewModels.AlbumViewModels.RowViewModel> rowViewModelsNew = new ArrayList<>();
//            List<AlbumViewModels.AlbumViewModels.RowViewModel> rowViewModelsOld = rowViewModels.get();
//            if (rowViewModelsOld != null) {
//                rowViewModelsNew.addAll(rowViewModelsOld);
//            } else {
//                // JxLogger.w("old is empty");
//            }
//            if (got.size() == 0) {
//                // JxLogger.w("got is empty");
//            } else {
//                rowViewModelsNew.addAll(got);
//                if (rowViewModelsNew.size() == 0) {
//                    // JxLogger.w("new is empty");
//                }
//                rowViewModels.set(rowViewModelsNew);
//            }
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
    static void updatePageIndex(@lombok.NonNull StateValue<Integer> pageIndex, @Param int got) {
        // JxLogger.i("");
        pageIndex.set(got);
    }

    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c
        , @Prop FetcherService service
        , @State boolean hasMore
        , @State int pageIndex
        , @State boolean fetching
        , @State List<AlbumViewModels.RowViewModel> rowViewModels) {

        return RecyclerCollectionComponent.create(c)
            //.disablePTR(true)
            .section(DataDiffSection.<AlbumViewModels.RowViewModel>create(new SectionContext(c))
                .data(rowViewModels)
                .renderEventHandler(GraphyRoot.onRender(c)))
            .paddingDip(YogaEdge.TOP, 8)
            .asyncPropUpdates(true)
            .loadEventsHandler(new LoadEventsHandler() {
                @Override public void onInitialLoad() {
                    JxLogger.i("");
                }

                @Override public void onLoadStarted(boolean empty) {
                    JxLogger.i("");
                }

                @Override public void onLoadSucceeded(boolean empty) {
                    JxLogger.i("");
                }

                @Override public void onLoadFailed(boolean empty) {
                    JxLogger.i("");
                }
            })
            .onScrollListener(new RecyclerView.OnScrollListener() {
                @Override public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            })
            .testKey(MAIN_SCREEN)
            .build();
    }

    @OnEvent(RenderEvent.class)
    static RenderInfo onRender(ComponentContext c, @FromEvent AlbumViewModels.RowViewModel model) {
        return model.createRenderInfo(c);
    }
}
