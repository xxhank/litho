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

package com.facebook.litho.test;

import android.graphics.Rect;

import com.facebook.litho.mount.MountState;
import com.facebook.litho.component.Component;
import com.facebook.litho.layout.LayoutState;

/**
 * Stores information about a {@link Component} which is only available when tests are run.
 * TestOutputs are calculated in {@link LayoutState} and transformed into {@link TestItem}s in
 * {@link MountState}.
 */
public class TestOutput {
    private       String mTestKey;
    private       long   mHostMarker     = -1;
    private       long   mLayoutOutputId = -1;
    private final Rect   mBounds         = new Rect();

    public String getTestKey() {
        return mTestKey;
    }

    public void setTestKey(String testKey) {
        mTestKey = testKey;
    }

    public Rect getBounds() {
        return mBounds;
    }

    public void setBounds(Rect bounds) {
        mBounds.set(bounds);
    }

    public void setBounds(int left, int top, int right, int bottom) {
        mBounds.set(left, top, right, bottom);
    }

    public void setHostMarker(long hostMarker) {
        mHostMarker = hostMarker;
    }

    public long getHostMarker() {
        return mHostMarker;
    }

    public long getLayoutOutputId() {
        return mLayoutOutputId;
    }

    public void setLayoutOutputId(long layoutOutputId) {
        mLayoutOutputId = layoutOutputId;
    }
}
