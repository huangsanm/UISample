package com.huashengmi.ui.android.ui.view.chart;

public class QuartEaseOut implements BaseEasingMethod{

	public QuartEaseOut() {
        super();
    }

    @Override
    public float next(float time) {
        return (float) Math.pow(time - 1, 4) + 1;
    }
}
