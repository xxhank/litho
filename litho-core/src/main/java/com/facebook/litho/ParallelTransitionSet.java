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

import com.facebook.litho.animation.AnimationBinding;
import com.facebook.litho.animation.ParallelBinding;
import com.facebook.litho.transition.Transition;
import com.facebook.litho.transition.TransitionSet;

import java.util.List;

/**
 * A {@link TransitionSet} that runs its child transitions in parallel, optionally with a stagger.
 */
public class ParallelTransitionSet extends TransitionSet {

  private final int mStaggerMs;

  public <T extends Transition> ParallelTransitionSet(T... children) {
    super(children);
    mStaggerMs = 0;
  }

  public <T extends Transition> ParallelTransitionSet(List<T> children) {
    super(children);
    mStaggerMs = 0;
  }

  public <T extends Transition> ParallelTransitionSet(int staggerMs, T... children) {
    super(children);
    mStaggerMs = staggerMs;
  }

  public <T extends Transition> ParallelTransitionSet(int staggerMs, List<T> children) {
    super(children);
    mStaggerMs = staggerMs;
  }

  @Override
  AnimationBinding createAnimation(List<AnimationBinding> children) {
    return new ParallelBinding(mStaggerMs, children);
  }
}
