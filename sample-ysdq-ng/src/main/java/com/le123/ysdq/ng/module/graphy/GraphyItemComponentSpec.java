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

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.le123.ysdq.ng.module.graphy.item.parts.ActionsComponent;
import com.le123.ysdq.ng.module.graphy.item.parts.FeedImageComponent;
import com.le123.ysdq.ng.module.graphy.item.parts.FooterComponent;
import com.le123.ysdq.ng.module.graphy.item.parts.TitleComponent;


@LayoutSpec
public class GraphyItemComponentSpec {

    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c, @Prop GraphyListSpec.Album artist, @Prop float imageRatio) {
        return Column.create(c)
            .child(
                Column.create(c)
                    .child(FeedImageComponent.create(c).images(new String[]{artist.url}).imageRatio(imageRatio))
                    .child(ActionsComponent.create(c)))
            .child(TitleComponent.create(c).title(artist.title))
            .child(FooterComponent.create(c).text(artist.subTitle))
            .build();
    }
}
