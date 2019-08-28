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

package com.facebook.litho.component;

import android.view.MotionEvent;
import android.view.View;

import com.facebook.litho.event.EventHandler;
import com.facebook.litho.event.TouchEvent;

import static com.facebook.litho.event.EventDispatcherUtils.dispatchOnTouch;

/**
 * Touch listener that triggers its underlying event handler.
 */
public class ComponentTouchListener implements View.OnTouchListener {
    private EventHandler<TouchEvent> mEventHandler;

    public EventHandler<TouchEvent> getEventHandler() {
        return mEventHandler;
    }

    public void setEventHandler(EventHandler<TouchEvent> eventHandler) {
        mEventHandler = eventHandler;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mEventHandler != null && dispatchOnTouch(mEventHandler, v, event);
    }
}
