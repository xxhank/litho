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

package com.facebook.litho.sections;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.facebook.litho.event.EventHandler;
import com.facebook.litho.event.EventTrigger;
import com.facebook.litho.StateContainer;
import com.facebook.litho.TreeProps;
import com.facebook.litho.component.ComponentContext;
import com.facebook.litho.component.ComponentsLogger;
import com.facebook.litho.widget.SectionsDebug;

import java.lang.ref.WeakReference;

public class SectionContext extends ComponentContext {

    private SectionTree                mSectionTree;
    private WeakReference<Section>     mScope;
    private EventHandler<LoadingEvent> mTreeLoadingEventHandler;
    private KeyHandler                 mKeyHandler;

    public SectionContext(Context context) {
        this(context, null, null);
    }

    public SectionContext(ComponentContext context) {
        this(
            context.getAndroidContext(),
            context.getLogTag(),
            context.getLogger(),
            context.getTreePropsCopy());
    }

    public SectionContext(Context context, @Nullable String logTag, ComponentsLogger logger) {
        this(context, logTag, logger, null);
    }

    public SectionContext(
        Context context,
        @Nullable String logTag,
        ComponentsLogger logger,
        @Nullable TreeProps treeProps) {
        super(context, logTag, logger);
        super.setTreeProps(treeProps);
        mKeyHandler = new KeyHandler();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public static SectionContext withSectionTree(SectionContext context, SectionTree sectionTree) {
        SectionContext sectionContext = new SectionContext(context);
        sectionContext.mSectionTree = sectionTree;
        sectionContext.mTreeLoadingEventHandler = new SectionTreeLoadingEventHandler(sectionTree);

        return sectionContext;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public static SectionContext withScope(SectionContext context, Section scope) {
        SectionContext sectionContext = new SectionContext(context);
        sectionContext.mSectionTree = context.mSectionTree;
        sectionContext.mTreeLoadingEventHandler = context.mTreeLoadingEventHandler;
        sectionContext.mScope = new WeakReference<>(scope);

        return sectionContext;
    }

    /**
     * Notify the {@link SectionTree} that it needs to synchronously perform a state update.
     *
     * @param stateUpdate state update to perform
     */
    @Override public void updateStateSync(StateContainer.StateUpdate stateUpdate, String attribution) {
        Section     section     = mScope.get();
        SectionTree sectionTree = mSectionTree;
        if (sectionTree == null || section == null) {
            return;
        }

        if (SectionsDebug.ENABLED) {
            Log.d(
                SectionsDebug.TAG,
                "(" + mSectionTree.hashCode() + ") updateState from " + stateUpdate.getClass().getName());
        }

        sectionTree.updateState(section.getGlobalKey(), stateUpdate, attribution);
    }

    @Override public void updateStateLazy(StateContainer.StateUpdate stateUpdate) {
        SectionTree sectionTree = mSectionTree;
        Section     section     = mScope.get();

        sectionTree.updateStateLazy(section.getGlobalKey(), stateUpdate);
    }

    /**
     * Notify the {@link SectionTree} that it needs to asynchronously perform a state update.
     *
     * @param stateUpdate state update to perform
     */
    @Override public void updateStateAsync(StateContainer.StateUpdate stateUpdate, String attribution) {
        Section     section     = mScope.get();
        SectionTree sectionTree = mSectionTree;
        if (sectionTree == null || section == null) {
            return;
        }

        if (SectionsDebug.ENABLED) {
            Log.d(
                SectionsDebug.TAG,
                "("
                    + mSectionTree.hashCode()
                    + ") updateStateAsync from "
                    + stateUpdate.getClass().getName());
        }

        sectionTree.updateStateAsync(section.getGlobalKey(), stateUpdate, attribution);
    }

    @Override
    public <E> EventHandler<E> newEventHandler(int id, Object[] params) {
        Section section = mScope.get();
        if (section == null) {
            throw new IllegalStateException("Called newEventHandler on a released Section");
        }

        return new EventHandler<>(section, id, params);
    }

    /**
     * @return New instance of {@link EventTrigger} that is created by the current mScope.
     */
    @Override <E> EventTrigger<E> newEventTrigger(String childKey, int id) {
        Section section   = mScope == null ? null : mScope.get();
        String        parentKey = section == null ? "" : section.getGlobalKey();
        return new EventTrigger<>(parentKey, id, childKey);
    }

    public Section getSectionScope() {
        Section section = mScope.get();

        return section;
    }

    @Override KeyHandler getKeyHandler() {
        return mKeyHandler;
    }

    @Nullable
    SectionTree getSectionTree() {
        return mSectionTree;
    }

    EventHandler<LoadingEvent> getTreeLoadingEventHandler() {
        return mTreeLoadingEventHandler;
    }

    @Override
    public void setTreeProps(TreeProps treeProps) {
        super.setTreeProps(treeProps);
    }

    @Override
    protected @Nullable TreeProps getTreeProps() {
        return super.getTreeProps();
    }
}
