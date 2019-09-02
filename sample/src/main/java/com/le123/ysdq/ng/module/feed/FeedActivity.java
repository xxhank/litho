package com.le123.ysdq.ng.module.feed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.agx.scaffold.JxFunc;
import com.agx.scaffold.JxTextUtils;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.le123.ysdq.ng.module.home.ChannelHeaderFetcher;
import com.le123.ysdq.ng.module.home.ChannelPageFetcher;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    ChannelHeaderFetcher.Response.Channel channel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.feed_activity);

        ComponentContext context = new ComponentContext(this);
        FeedList section = FeedList.create(new SectionContext(this))
            .service(new FeedListSpec.FetcherService() {
                @Override public List<FeedListSpec.RowViewModel> fetch_(int pageIndex, JxFunc.Action<Boolean> callback) {
                    FeedListSpec.AlbumViewModels model = new FeedListSpec.AlbumViewModels();
                    if (channel == null) {
                        ChannelHeaderFetcher.fetch(value -> value.ifPresent(value1 -> {
                            ChannelHeaderFetcher.Response response = value1.value;
                            if (response.channels.size() == 0) {
                                callback.yield(false);
                                return;
                            }
                            channel = response.channels.get(0);
                            fetchPage(channel, pageIndex, model, callback);
                        }).orElse(value1 -> {
                            callback.yield(false);
                        }));
                    } else {
                        fetchPage(channel, pageIndex, model, callback);
                    }

                    return model.rows;
                }

                void fetchPage(ChannelHeaderFetcher.Response.Channel channel, int pageIndex, FeedListSpec.AlbumViewModels model, JxFunc.Action<Boolean> callback) {
                    ChannelPageFetcher.fetch(channel.page, pageIndex, pageValue -> pageValue.ifPresent(page -> {
                        model.rows = new ArrayList<>(10);
                        model.hasMore = page.value.data.hasMore == 1;
                        for (ChannelPageFetcher.Response.Data.Rec rec : page.value.data.rec) {
                            // JxLogger.i("%s %d", rec.name, rec.data.size());
                            // JxLogger.i("style %s", rec.style);
                            switch (rec.style) {
                                case ChannelPageFetcher.Response.Data.Rec.STYLE_SPECIAL:
                                case ChannelPageFetcher.Response.Data.Rec.STYLE_FEED:
                                case ChannelPageFetcher.Response.Data.Rec.STYLE_FEED_SHORT_VIDEO: {
                                    List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList = rec.data;
                                    buildRowData(dataXList.subList(0, 1), 1, true, model.rows, pageIndex);
                                    break;
                                }
                                case ChannelPageFetcher.Response.Data.Rec.STYLE_TWO: {
                                    List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList = rec.data;
                                    buildRowData(dataXList, 2, true, model.rows, pageIndex);
                                    break;
                                }
                                case ChannelPageFetcher.Response.Data.Rec.STYLE_THREE: {
                                    List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList = rec.data;
                                    buildRowData(dataXList, 3, false, model.rows, pageIndex);
                                    break;
                                }
                                case ChannelPageFetcher.Response.Data.Rec.STYLE_MIXTURE_TWO: {
                                    List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList = rec.data;
                                    buildRowData(dataXList.subList(0, 1), 1, true, model.rows, pageIndex);
                                    buildRowData(dataXList.subList(1, dataXList.size()), 2, true, model.rows, pageIndex);
                                    break;
                                }
                                case ChannelPageFetcher.Response.Data.Rec.STYLE_MIXTURE_THREE: {
                                    List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList = rec.data;
                                    buildRowData(dataXList.subList(0, 1), 1, true, model.rows, pageIndex);
                                    buildRowData(dataXList.subList(1, dataXList.size()), 3, false, model.rows, pageIndex);
                                    break;
                                }
                                default: {
                                    List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList = rec.data;
                                    buildRowData(dataXList.subList(0, 1), 1, true, model.rows, pageIndex);
                                    // JxLogger.w("unsupported style %s", rec.style);
                                    break;
                                }
                            }
                        }
                        if (dataModelEventHandler != null) {
                            dataModelEventHandler.dispatchEvent(model);
                        } else {
                            // JxLogger.i("dataModelEventHandler==null");
                        }
                        callback.yield(true);
                    }).orElse(p -> {
                        callback.yield(false);
                    }));
                }

                private void buildRowData(List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList, int colCount, boolean useHImage, List<FeedListSpec.RowViewModel> rows, int pageIndex) {
                    int count = dataXList.size();
                    for (int row = 0; row < count / colCount; row++) {
                        List<FeedListSpec.Album> albums = new ArrayList<>(colCount);
                        StringBuilder            rowid  = new StringBuilder();
                        for (int col = 0; col < colCount; col++) {
                            ChannelPageFetcher.Response.Data.Rec.DataX datum = dataXList.get(colCount * row + col);
                            rowid.append(datum.aid);
                            albums.add(buildAlbum(datum, useHImage));
                        }
                        rows.add(FeedListSpec.RowViewModel.builder()
                            .id(pageIndex + "_" + row + "_" + rowid.toString())
                            .albums(albums)
                            .build());
                    }
                }

                private FeedListSpec.Album buildAlbum(ChannelPageFetcher.Response.Data.Rec.DataX datum, boolean useHImage) {
                    return FeedListSpec.Album.builder()
                        .id(datum.aid)
                        .title(datum.name)
                        .subTitle(datum.subname)
                        .description(JxTextUtils.equals(datum.subname, datum.description) ? "" : datum.description)
                        .url(useHImage ? datum.pich : datum.pic)
                        .category(datum.categoryName)
                        .tag(datum.cornerTitle).tagColor(datum.cornerColor)
                        .episode(datum.vt.equals("11") ? datum.duration : datum.episodes)
                        .build();
                }
            })
            .build();

        RecyclerCollectionComponent component = RecyclerCollectionComponent.create(context)
            .section(section)
            .build();
        LithoView view = LithoView.create(context, component);
        setContentView(view);
    }
}
