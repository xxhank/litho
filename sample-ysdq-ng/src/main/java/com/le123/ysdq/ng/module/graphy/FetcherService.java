package com.le123.ysdq.ng.module.graphy;

import com.agx.scaffold.JxFunc;
import com.facebook.litho.EventHandler;

import java.util.List;

abstract class FetcherService {
    EventHandler<AlbumViewModels> dataModelEventHandler;

    void registerLoadingEvent(EventHandler<AlbumViewModels> dataModelEventHandler) {
        this.dataModelEventHandler = dataModelEventHandler;
    }

    void unregisterLoadingEvent() {
        dataModelEventHandler = null;
    }

    public List<AlbumViewModels.RowViewModel> fetch(int pageIndex) {
        return fetch_(pageIndex, new JxFunc.Action<Boolean>() {
            @Override public void yield(@androidx.annotation.NonNull Boolean value) {
            }
        });
    }

    abstract public List<AlbumViewModels.RowViewModel> fetch_(int pageIndex, JxFunc.Action<Boolean> callback);
}
