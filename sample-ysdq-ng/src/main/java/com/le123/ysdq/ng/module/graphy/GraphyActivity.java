package com.le123.ysdq.ng.module.graphy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.agx.scaffold.JxFunc;
import com.agx.scaffold.JxLogger;
import com.agx.scaffold.JxTextUtils;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.samples.litho.lithography.Artist;
import com.facebook.samples.litho.lithography.Datum;
import com.le123.ysdq.ng.module.home.ChannelHeaderFetcher;
import com.le123.ysdq.ng.module.home.ChannelPageFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphyActivity extends AppCompatActivity {

    private static final Datum[] DATA =
        new Datum[]{

            new Artist(
                "Richard James Lane",
                "Lithographer to Queen Victoria and Prince Albert, Lane produced over a "
                    + "thousand prints of his various lithographs of several hundred portraitures.  "
                    + "One of his better known works is "
                    + "of a eighteen year old Queen Victoria.",
                1800,
                "https://upload.wikimedia.org/wikipedia/commons/3/33/George_Francis_Lyon.jpg"),
            new Artist(
                "Louis and Fritz Wolff",
                "Deaf brothers from Heilbronn, Louis (Ludwig) and Fritz (Fredrich) Wolff "
                    + "were nineteenth century lithographers who composed scenes of buildings, squares "
                    + "and everyday urban life.",
                1802,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/B\u00F6ckingen_am_See_1848_Gebr_Wolff.jpg/512px-B\u00F6ckingen_am_See_1848_Gebr_Wolff.jpg"),

            new Artist(
                "\u00C9mile Lassalle",
                "A recipient of the French National Order of the Legion of Honour and a "
                    + "student of Pierre Lacour, Lassalle was a painter and lithographer in the "
                    + "mid-1800s.",
                1813,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Atlas_pittoresque_pl_004.jpg/512px-Atlas_pittoresque_pl_004.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/Atlas_pittoresque_pl_119.jpg/512px-Atlas_pittoresque_pl_119.jpg"),
            new Artist(
                "Jule Arnout",
                "Jule Arnout, pupil of his father, Jean Baptiste Arnout, captured "
                    + "landscapes, monuments and cities from France, Switzerland, Italy and England.",
                1814,
                "https://upload.wikimedia.org/wikipedia/commons/0/0e/Sommerset_house_by_ARNOUT%2C_LOUIS_JULES_-_GMII.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/2/2d/Arnout_Boulevard_St_Martin.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/a/ac/Jules_Arnout_Saint_Isaac%27s_Cathedral.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/1/1a/Trafalgar_square_by_ARNOUT%2C_LOUIS_JULES_-_GMII.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/8/84/Windsor_castle_by_ARNOUT%2C_LOUIS_JULES_-_GMII.jpg"),
            new Artist(
                "Anastas Jovanovi\u0107",
                "Serbian Jovanovi\u0107 is best known as a historic and pioneering "
                    + "photographer, but his efforts to capture his hometown of Belgrade started with "
                    + "paint and lithography.",
                1817,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Victory_of_King_Milutin_over_the_Tatars%2C_Anastas_Jovanovi\u0107_%281853%29.jpg/512px-Victory_of_King_Milutin_over_the_Tatars%2C_Anastas_Jovanovi\u0107_%281853%29.jpg"),

            new Artist(
                "Gaston Marichal",
                "Little is known about this French lithographer, except that he worked in "
                    + "Spain in the latter half of the  1800s.",
                1830,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Iglesia_Compa\u00F1ia_Chile.JPG/512px-Iglesia_Compa\u00F1ia_Chile.JPG"),

            new Artist(
                "Vincent van Gogh",
                "The renowned post-impressionist painter dabbled briefly in lithography, "
                    + "producing ten works in 1882/3.  Many of his prints, such as \"Sorrow\" and \"At "
                    + "Eternity's Gate\" he also captured in other media.",
                1853,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/Van_Gogh_-_In_the_Orchard_-_1883.jpg/512px-Van_Gogh_-_In_the_Orchard_-_1883.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/Vincent_Van_Gogh_27.JPG/512px-Vincent_Van_Gogh_27.JPG"),
            new Artist(
                "Juan Comba Garc\u00EDa", // Garcia
                "A Spanish cartoonist, photographer and painter, Juan Comba Garc\u00EDa "
                    + "was bestowed the title of \"Graphic Chronicler of the Restoration\" due to his "
                    + "683 graphic works during the time of King Alfonso XII.",
                1854,
                "https://upload.wikimedia.org/wikipedia/commons/c/cf/Duque_de_Sesto_pide_la_mano_de_Mar%C3%ADa_de_las_Mercedes_de_Orleans.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Inauguraci\u00F3n_de_la_Estaci\u00F3n_definitiva_del_ferrocarril_de_Madrid_a_Ciudad_Real_y_Badajoz_%28Comba%29.jpg/512px-Inauguraci\u00F3n_de_la_Estaci\u00F3n_definitiva_del_ferrocarril_de_Madrid_a_Ciudad_Real_y_Badajoz_%28Comba%29.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/1/1b/Palacio-del-Pardo-1885-Juan-Comba.jpg")
        };

    ChannelHeaderFetcher.Response.Channel channel = null;

    FetcherService service = new FetcherService() {
        @Override public List<AlbumViewModels.RowViewModel> fetch_(int pageIndex, JxFunc.Action<Boolean> callback) {
            AlbumViewModels model = new AlbumViewModels();
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

        void fetchPage(ChannelHeaderFetcher.Response.Channel channel, int pageIndex, AlbumViewModels model, JxFunc.Action<Boolean> callback) {
            ChannelPageFetcher.fetch(channel.page, pageIndex, pageValue -> pageValue.ifPresent(page -> {
                model.rows = new ArrayList<>(10);
                model.hasMore = page.value.data.hasMore == 1;
                for (ChannelPageFetcher.Response.Data.Rec rec : page.value.data.rec) {
                    // JxLogger.i("%s %d", rec.name, rec.data.size());
                    // JxLogger.i("style %s", rec.style);
                    switch (rec.style) {
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
                        case ChannelPageFetcher.Response.Data.Rec.STYLE_SPECIAL:
                        case ChannelPageFetcher.Response.Data.Rec.STYLE_FEED:
                        case ChannelPageFetcher.Response.Data.Rec.STYLE_FEED_SHORT_VIDEO:
                        default: {
                            List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList = rec.data;
                            buildRowData(dataXList.subList(0, 1), 1, true, model.rows, pageIndex);
                            JxLogger.w("unsupported style %s", rec.style);
                            break;
                        }
                    }
                }
                if (dataModelEventHandler != null) {
                    JxLogger.i("dataModelEventHandler!=null %d", model.rows.size());

                    dataModelEventHandler.dispatchEvent(model);
                } else {
                    JxLogger.i("dataModelEventHandler==null");
                }
                callback.yield(true);
            }).orElse(p -> {
                callback.yield(false);
            }));
        }

        private void buildRowData(List<ChannelPageFetcher.Response.Data.Rec.DataX> dataXList, int colCount, boolean useHImage, List<AlbumViewModels.RowViewModel> rows, int pageIndex) {
            int count = dataXList.size();
            for (int row = 0; row < count / colCount; row++) {
                List<Album>   albums = new ArrayList<>(colCount);
                StringBuilder rowid  = new StringBuilder();
                for (int col = 0; col < colCount; col++) {
                    ChannelPageFetcher.Response.Data.Rec.DataX datum = dataXList.get(colCount * row + col);
                    rowid.append(datum.aid);
                    albums.add(buildAlbum(datum, useHImage));
                }
                rows.add(AlbumViewModels.RowViewModel.builder()
                    .id(pageIndex + "_" + row + "_" + rowid.toString())
                    .albums(albums)
                    .build());
            }
        }

        private Album buildAlbum(ChannelPageFetcher.Response.Data.Rec.DataX datum, boolean useHImage) {
            return Album.builder()
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
    };
    public List<AlbumViewModels.RowViewModel> rowViewModels = new ArrayList<>(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Datum> datumList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datumList.addAll(Arrays.asList(DATA));
        }

        setContentView(
            LithoView.create(this,
                GraphyRoot.create(new ComponentContext(this))
                    .service(service)
                    //.rowViewModels(rowViewModels)
                    .build()));
    }
}
