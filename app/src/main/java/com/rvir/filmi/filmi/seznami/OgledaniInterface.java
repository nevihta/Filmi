package com.rvir.filmi.filmi.seznami;

import android.view.View;

public interface OgledaniInterface {
    public View remove(int idFilma);
    public void writeReview(int idFilma);
    public void recommend(int idFilma);
}