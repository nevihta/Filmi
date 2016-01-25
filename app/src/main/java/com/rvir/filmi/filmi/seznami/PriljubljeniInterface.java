package com.rvir.filmi.filmi.seznami;

import android.view.View;

public interface PriljubljeniInterface {
    public View remove(int idFilma, String idFilmaApi);
    public void recommend(int idFilma);
}