/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.litho;

import static com.facebook.litho.component.Column.create;
import static com.facebook.litho.testing.helper.ComponentTestHelper.mountComponent;
import static org.assertj.core.api.Java6Assertions.assertThat;

import com.facebook.litho.component.Component;
import com.facebook.litho.component.ComponentContext;
import com.facebook.litho.component.ComponentHost;
import com.facebook.litho.testing.TestDrawableComponent;
import com.facebook.litho.testing.TestViewComponent;
import com.facebook.litho.testing.testrunner.ComponentsTestRunner;
import com.facebook.litho.testing.util.InlineLayoutSpec;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

@RunWith(ComponentsTestRunner.class)
public class MountStateFocusableTest {

  private ComponentContext mContext;
  private boolean mFocusableDefault;

  @Before
  public void setup() {
    mContext = new ComponentContext(RuntimeEnvironment.application);
    mFocusableDefault = new ComponentHost(mContext).isFocusable();
  }

  @Test
  public void testInnerComponentHostFocusable() {
    final LithoView lithoView =
        mountComponent(
            mContext,
            new InlineLayoutSpec() {
              @Override
              protected Component onCreateLayout(ComponentContext c) {
                return create(c)
                    .child(create(c).focusable(true).child(TestViewComponent.create(c)))
                    .build();
              }
            });

    assertThat(lithoView.getChildCount()).isEqualTo(1);
    // TODO(T16959291): The default varies between internal and external test runs, which indicates
    // that our Robolectric setup is not actually identical. Until we can figure out why,
    // we will compare against the dynamic default instead of asserting false.
    assertThat(lithoView.isFocusable()).isEqualTo(mFocusableDefault);

    ComponentHost innerHost = (ComponentHost) lithoView.getChildAt(0);
    assertThat(innerHost.isFocusable()).isTrue();
  }

  @Test
  public void testRootHostFocusable() {
    final LithoView lithoView =
        mountComponent(
            mContext,
            new InlineLayoutSpec() {
              @Override
              protected Component onCreateLayout(ComponentContext c) {
                return create(c).focusable(true).child(TestDrawableComponent.create(c)).build();
              }
            });

    assertThat(lithoView.getChildCount()).isEqualTo(0);
    assertThat(lithoView.isFocusable()).isTrue();
  }
}
