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

package com.fblitho.lithoktsample.bordereffects

import com.facebook.litho.geometry.Border
import com.facebook.litho.component.Component
import com.facebook.litho.component.ComponentContext
import com.facebook.litho.component.Row
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge

@LayoutSpec
object CompositePathEffectBorderSpec {

  @OnCreateLayout
  fun onCreateLayout(c: ComponentContext): Component =
      Row.create(c)
          .child(
              Text.create(c)
                  .textSizeSp(20f)
                  .text("This component has a composite path effect of discrete + corner"))
          .border(
              Border.create(c)
                  .widthDip(YogaEdge.ALL, 20f)
                  .color(YogaEdge.LEFT, NiceColor.RED)
                  .color(YogaEdge.TOP, NiceColor.ORANGE)
                  .color(YogaEdge.RIGHT, NiceColor.GREEN)
                  .color(YogaEdge.BOTTOM, NiceColor.BLUE)
                  .dashEffect(floatArrayOf(10f, 5f), 0f)
                  .radiusDip(20f)
                  .build())
          .build()

}
