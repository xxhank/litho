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

import android.animation.StateListAnimator;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.facebook.litho.drawable.ComparableDrawable;
import com.facebook.litho.layout.LayoutState;
import com.facebook.litho.mount.MountState;
import com.facebook.yoga.YogaDirection;

import javax.annotation.Nullable;

/**
 * Additional information passed between {@link LayoutState} and {@link MountState} used on a {@link
 * View}.
 */
public class ViewNodeInfo {

    private              ComparableDrawable mBackground;
    private              ComparableDrawable mForeground;
    private              Rect               mPadding;
    private              Rect               mExpandedTouchBounds;
    private              YogaDirection      mLayoutDirection;
    private @Nullable    StateListAnimator  mStateListAnimator;
    private @DrawableRes int                mStateListAnimatorRes;

    public void setBackground(ComparableDrawable background) {
        mBackground = background;
    }

    public ComparableDrawable getBackground() {
        return mBackground;
    }

    public void setForeground(ComparableDrawable foreground) {
        mForeground = foreground;
    }

    public ComparableDrawable getForeground() {
        return mForeground;
    }

    public int getPaddingLeft() {
        return (mPadding != null) ? mPadding.left : 0;
    }

    public int getPaddingTop() {
        return (mPadding != null) ? mPadding.top : 0;
    }

    public int getPaddingRight() {
        return (mPadding != null) ? mPadding.right : 0;
    }

    public int getPaddingBottom() {
        return (mPadding != null) ? mPadding.bottom : 0;
    }

    public void setPadding(int l, int t, int r, int b) {
        if (mPadding != null) {
            throw new IllegalStateException("Padding already initialized for this " + "ViewNodeInfo.");
        }

        mPadding = new Rect();
        mPadding.set(l, t, r, b);
    }

    public boolean hasPadding() {
        return (mPadding != null);
    }

    public void setLayoutDirection(YogaDirection layoutDirection) {
        mLayoutDirection = layoutDirection;
    }

    public YogaDirection getLayoutDirection() {
        return mLayoutDirection;
    }

    public void setExpandedTouchBounds(InternalNode node, int l, int t, int r, int b) {
        if (!node.hasTouchExpansion()) {
            return;
        }

        int touchExpansionLeft   = node.getTouchExpansionLeft();
        int touchExpansionTop    = node.getTouchExpansionTop();
        int touchExpansionRight  = node.getTouchExpansionRight();
        int touchExpansionBottom = node.getTouchExpansionBottom();
        if (touchExpansionLeft == 0
            && touchExpansionTop == 0
            && touchExpansionRight == 0
            && touchExpansionBottom == 0) {
            return;
        }

        if (mExpandedTouchBounds != null) {
            throw new IllegalStateException(
                "ExpandedTouchBounds already initialized for this " + "ViewNodeInfo.");
        }

        mExpandedTouchBounds = new Rect();
        mExpandedTouchBounds.set(
            l - touchExpansionLeft,
            t - touchExpansionTop,
            r + touchExpansionRight,
            b + touchExpansionBottom);
    }

    @Nullable public Rect getExpandedTouchBounds() {
        if (mExpandedTouchBounds == null || mExpandedTouchBounds.isEmpty()) {
            return null;
        }

        return mExpandedTouchBounds;
    }

    @Nullable
    public StateListAnimator getStateListAnimator() {
        return mStateListAnimator;
    }

    public void setStateListAnimator(StateListAnimator stateListAnimator) {
        mStateListAnimator = stateListAnimator;
    }

    @DrawableRes
    public int getStateListAnimatorRes() {
        return mStateListAnimatorRes;
    }

    public void setStateListAnimatorRes(@DrawableRes int resId) {
        mStateListAnimatorRes = resId;
    }

    /**
     * Checks if this ViewNodeInfo is equal to the {@param other}
     *
     * @param other the other ViewNodeInfo
     * @return {@code true} iff this NodeInfo is equal to the {@param other}.
     */
    public boolean isEquivalentTo(ViewNodeInfo other) {
        if (this == other) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!ComparableDrawable.isEquivalentTo(mBackground, other.mBackground)) {
            return false;
        }

        if (!ComparableDrawable.isEquivalentTo(mForeground, other.mForeground)) {
            return false;
        }

        if (!CommonUtils.equals(mPadding, other.mPadding)) {
            return false;
        }

        if (!CommonUtils.equals(mExpandedTouchBounds, other.mExpandedTouchBounds)) {
            return false;
        }

        if (!CommonUtils.equals(mLayoutDirection, other.mLayoutDirection)) {
            return false;
        }

        if (mStateListAnimatorRes != other.mStateListAnimatorRes) {
            return false;
        }

        // TODO: (T33421916) We need compare StateListAnimators more accurately
        if (!CommonUtils.equals(mStateListAnimator, other.mStateListAnimator)) {
            return false;
        }

        return true;
    }
}
