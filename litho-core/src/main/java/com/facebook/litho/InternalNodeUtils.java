/*
 * Copyright 2019-present Facebook, Inc.
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

import com.facebook.litho.component.ComponentContext;

public class InternalNodeUtils {

    public static InternalNode create(ComponentContext context) {
        NodeConfig.InternalNodeFactory factory = NodeConfig.sInternalNodeFactory;
        if (factory != null) {
            return factory.create(context);
        } else {
            return new DefaultInternalNode(context);
        }
    }

    /**
     * Check that the root of the nested tree we are going to use, has valid layout directions with
     * its main tree holder node.
     */
    public static boolean hasValidLayoutDirectionInNestedTree(InternalNode holder, InternalNode nestedTree) {
        return nestedTree.isLayoutDirectionInherit()
            || (nestedTree.getResolvedLayoutDirection() == holder.getResolvedLayoutDirection());
    }
}
