package com.le123.ysdq.ng.module.hunt;

import com.facebook.litho.Component;
import com.facebook.litho.LithoView;

import java.util.List;

public class LithoController {
    private LithoView view;

    void take(LithoView view) {
        this.view = view;
        setup();
    }

    private void setup() {

    }

    private void renderContent(List<Object> items) {
        Component component = null;//TODO:: create real component
        view.setComponentAsync(component);
    }
}
