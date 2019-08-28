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

import android.graphics.Rect;

import androidx.annotation.Nullable;

import com.facebook.litho.component.Component;
import com.facebook.litho.event.EventHandler;
import com.facebook.litho.event.FocusedVisibleEvent;
import com.facebook.litho.event.FullImpressionVisibleEvent;
import com.facebook.litho.event.InvisibleEvent;
import com.facebook.litho.event.UnfocusedVisibleEvent;
import com.facebook.litho.event.VisibilityChangedEvent;
import com.facebook.litho.event.VisibleEvent;
import com.facebook.litho.mount.MountState;

/**
 * Stores information about a {@link Component} which has registered handlers for {@link
 * VisibleEvent} or {@link InvisibleEvent}. The information is passed to {@link MountState} which
 * then dispatches the appropriate events.
 */
public class VisibilityOutput {

    private           long                                     mId;
    private           Component                                mComponent;
    private final     Rect                                     mBounds = new Rect();
    private           float                                    mVisibleHeightRatio;
    private           float                                    mVisibleWidthRatio;
    private           EventHandler<VisibleEvent>               mVisibleEventHandler;
    private           EventHandler<FocusedVisibleEvent>        mFocusedEventHandler;
    private           EventHandler<UnfocusedVisibleEvent>      mUnfocusedEventHandler;
    private           EventHandler<FullImpressionVisibleEvent> mFullImpressionEventHandler;
    private           EventHandler<InvisibleEvent>             mInvisibleEventHandler;
    private @Nullable EventHandler<VisibilityChangedEvent>     mVisibilityChangedEventHandler;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Component getComponent() {
        return mComponent;
    }

    public void setComponent(Component component) {
        mComponent = component;
    }

    public Rect getBounds() {
        return mBounds;
    }

    public void setBounds(int l, int t, int r, int b) {
        mBounds.set(l, t, r, b);
    }

    public void setBounds(Rect bounds) {
        mBounds.set(bounds);
    }

    public void setVisibleHeightRatio(float visibleHeightRatio) {
        mVisibleHeightRatio = visibleHeightRatio;
    }

    public float getVisibleHeightRatio() {
        return mVisibleHeightRatio;
    }

    public void setVisibleWidthRatio(float visibleWidthRatio) {
        mVisibleWidthRatio = visibleWidthRatio;
    }

    public float getVisibleWidthRatio() {
        return mVisibleWidthRatio;
    }

    public void setVisibleEventHandler(EventHandler<VisibleEvent> visibleEventHandler) {
        mVisibleEventHandler = visibleEventHandler;
    }

    public EventHandler<VisibleEvent> getVisibleEventHandler() {
        return mVisibleEventHandler;
    }

    public void setFocusedEventHandler(EventHandler<FocusedVisibleEvent> focusedEventHandler) {
        mFocusedEventHandler = focusedEventHandler;
    }

    public EventHandler<FocusedVisibleEvent> getFocusedEventHandler() {
        return mFocusedEventHandler;
    }

    public void setUnfocusedEventHandler(EventHandler<UnfocusedVisibleEvent> unfocusedEventHandler) {
        mUnfocusedEventHandler = unfocusedEventHandler;
    }

    public EventHandler<UnfocusedVisibleEvent> getUnfocusedEventHandler() {
        return mUnfocusedEventHandler;
    }

    public void setFullImpressionEventHandler(
        EventHandler<FullImpressionVisibleEvent> fullImpressionEventHandler) {
        mFullImpressionEventHandler = fullImpressionEventHandler;
    }

    public EventHandler<FullImpressionVisibleEvent> getFullImpressionEventHandler() {
        return mFullImpressionEventHandler;
    }

    public void setInvisibleEventHandler(EventHandler<InvisibleEvent> invisibleEventHandler) {
        mInvisibleEventHandler = invisibleEventHandler;
    }

    public EventHandler<InvisibleEvent> getInvisibleEventHandler() {
        return mInvisibleEventHandler;
    }

    public void setVisibilityChangedEventHandler(
        @Nullable EventHandler<VisibilityChangedEvent> visibilityChangedEventHandler) {
        mVisibilityChangedEventHandler = visibilityChangedEventHandler;
    }

    @Nullable
    public EventHandler<VisibilityChangedEvent> getVisibilityChangedEventHandler() {
        return mVisibilityChangedEventHandler;
    }
}
