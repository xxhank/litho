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

package com.facebook.litho.testing.espresso;

import com.facebook.litho.component.Component;
import com.facebook.litho.component.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;

@LayoutSpec
public class MyComponentSpec {
  @OnCreateLayout
  public static Component onCreateLayout(
      ComponentContext c, @Prop String text, @Prop(optional = true) Object customViewTag) {
    return Text.create(c)
        .text(text)
        .contentDescription("foobar2")
        .viewTag(customViewTag)
        .testKey("my_test_key")
        .build();
  }
}
