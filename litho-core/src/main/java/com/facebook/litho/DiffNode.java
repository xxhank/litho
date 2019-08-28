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

import com.facebook.litho.component.Component;
import com.facebook.litho.layout.LayoutOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * A lightweight representation of a layout node, used to cache measurements between two Layout tree
 * calculations.
 */
public class DiffNode implements Cloneable {

    static final int UNSPECIFIED = -1;

    private       LayoutOutput     mContent;
    private       LayoutOutput     mBackground;
    private       LayoutOutput     mForeground;
    private       LayoutOutput     mBorder;
    private       LayoutOutput     mHost;
    private       VisibilityOutput mVisibilityOutput;
    private       Component        mComponent;
    private       float            mLastMeasuredWidth;
    private       float            mLastMeasuredHeight;
    private       int              mLastWidthSpec;
    private       int              mLastHeightSpec;
    private final List<DiffNode>   mChildren;

    public DiffNode() {
        mChildren = new ArrayList<>(4);
    }

    public int getChildCount() {
        return mChildren == null ? 0 : mChildren.size();
    }

    public DiffNode getChildAt(int i) {
        return mChildren.get(i);
    }

    public Component getComponent() {
        return mComponent;
    }

    public void setComponent(Component component) {
        mComponent = component;
    }

    public float getLastMeasuredWidth() {
        return mLastMeasuredWidth;
    }

    public void setLastMeasuredWidth(float lastMeasuredWidth) {
        mLastMeasuredWidth = lastMeasuredWidth;
    }

    public float getLastMeasuredHeight() {
        return mLastMeasuredHeight;
    }

    public void setLastMeasuredHeight(float lastMeasuredHeight) {
        mLastMeasuredHeight = lastMeasuredHeight;
    }

    public int getLastWidthSpec() {
        return mLastWidthSpec;
    }

    public int getLastHeightSpec() {
        return mLastHeightSpec;
    }

    public void setLastWidthSpec(int widthSpec) {
        mLastWidthSpec = widthSpec;
    }

    public void setLastHeightSpec(int heightSpec) {
        mLastHeightSpec = heightSpec;
    }

    public List<DiffNode> getChildren() {
        return mChildren;
    }

    public void addChild(DiffNode node) {
        mChildren.add(node);
    }

    public LayoutOutput getContent() {
        return mContent;
    }

    public void setContent(LayoutOutput content) {
        mContent = content;
    }

    public VisibilityOutput getVisibilityOutput() {
        return mVisibilityOutput;
    }

    public void setVisibilityOutput(VisibilityOutput visibilityOutput) {
        mVisibilityOutput = visibilityOutput;
    }

    public LayoutOutput getBackground() {
        return mBackground;
    }

    public void setBackground(LayoutOutput background) {
        mBackground = background;
    }

    public LayoutOutput getForeground() {
        return mForeground;
    }

    public void setForeground(LayoutOutput foreground) {
        mForeground = foreground;
    }

    public LayoutOutput getBorder() {
        return mBorder;
    }

    public void setBorder(LayoutOutput border) {
        mBorder = border;
    }

    public LayoutOutput getHost() {
        return mHost;
    }

    public void setHost(LayoutOutput host) {
        mHost = host;
    }
}
