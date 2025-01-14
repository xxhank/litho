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
package com.facebook.samples.litho.hscroll;

import com.facebook.litho.event.ClickEvent;
import com.facebook.litho.component.Column;
import com.facebook.litho.component.Component;
import com.facebook.litho.component.ComponentContext;
import com.facebook.litho.component.Row;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.sections.widget.RecyclerCollectionEventsController;
import com.facebook.litho.widget.ItemSelectedEvent;
import com.facebook.litho.widget.Spinner;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import java.util.ArrayList;
import java.util.List;

@LayoutSpec
public class HorizontalScrollScrollerComponentSpec {

  @OnCreateLayout
  static Component onCreateLayout(ComponentContext c, @Prop Integer[] colors) {
    return Column.create(c)
        .paddingDip(YogaEdge.ALL, 5)
        .child(
            Row.create(c)
                .alignContent(YogaAlign.STRETCH)
                .marginDip(YogaEdge.TOP, 10)
                .child(
                    Text.create(c)
                        .alignSelf(YogaAlign.CENTER)
                        .flexGrow(2f)
                        .text("Scroll to: ")
                        .textSizeSp(20))
                .child(
                    Text.create(c)
                        .alignSelf(YogaAlign.CENTER)
                        .flexGrow(0.5f)
                        .text("PREVIOUS")
                        .clickHandler(HorizontalScrollScrollerComponent.onClick(c, false))
                        .textSizeSp(20))
                .child(
                    Text.create(c)
                        .alignSelf(YogaAlign.CENTER)
                        .flexGrow(0.5f)
                        .text("NEXT")
                        .clickHandler(HorizontalScrollScrollerComponent.onClick(c, true))
                        .textSizeSp(20)))
        .child(
            Row.create(c)
                .alignContent(YogaAlign.STRETCH)
                .marginDip(YogaEdge.TOP, 10)
                .child(
                    Text.create(c)
                        .alignSelf(YogaAlign.CENTER)
                        .flexGrow(2f)
                        .text("Smooth scroll to: ")
                        .textSizeSp(20))
                .child(
                    Spinner.create(c)
                        .flexGrow(1.f)
                        .options(getPositionsFromDataset(colors))
                        .selectedOption("0")
                        .itemSelectedEventHandler(
                            HorizontalScrollScrollerComponent.onScrollToPositionSelected(c))))
        .build();
  }

  private static List<String> getPositionsFromDataset(Integer[] colors) {
    final List<String> positions = new ArrayList<>();
    for (int i = 0; i < colors.length; i++) {
      positions.add(i, Integer.toString(i));
    }
    return positions;
  }

  @OnEvent(ItemSelectedEvent.class)
  static void onScrollToPositionSelected(
      ComponentContext c,
      @FromEvent String newSelection,
      @Prop RecyclerCollectionEventsController eventsController) {
    eventsController.requestScrollToPositionWithSnap(Integer.parseInt(newSelection));
  }

  @OnEvent(ClickEvent.class)
  static void onClick(
      ComponentContext c,
      @Prop RecyclerCollectionEventsController eventsController,
      @Param boolean forward) {
    if (forward) {
      eventsController.requestScrollToNextPosition(true);
    } else {
      eventsController.requestScrollToPreviousPosition(true);
    }
  }
}
