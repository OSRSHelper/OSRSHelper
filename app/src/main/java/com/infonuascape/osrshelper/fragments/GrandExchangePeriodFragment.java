package com.infonuascape.osrshelper.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.GrandExchangePeriods;
import com.infonuascape.osrshelper.views.GrandExchangeAxisFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;

public class GrandExchangePeriodFragment extends OSRSFragment {
	private final static String EXTRA_GE_PERIOD = "EXTRA_GE_PERIOD";
	private GraphView graphView;

	private GrandExchangePeriods period;

	public static GrandExchangePeriodFragment newInstance(final GrandExchangePeriods period) {
		GrandExchangePeriodFragment fragment = new GrandExchangePeriodFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_GE_PERIOD, period);
		fragment.setArguments(b);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.ge_detail_period, null);

		period = (GrandExchangePeriods) getArguments().getSerializable(EXTRA_GE_PERIOD);

		graphView = view.findViewById(R.id.graph);
		graphView.getGridLabelRenderer().setLabelFormatter(new GrandExchangeAxisFormatter(getActivity(), period.getFormat()));

		return view;
	}

	public void onPageVisible(final DataPoint[] datapoints, DataPoint[] averages) {
		if(datapoints != null && averages != null) {
			graphView.removeAllSeries();
			DataPoint[] list = Arrays.copyOfRange(datapoints, 0, period.getDays());
			LineGraphSeries<DataPoint> datapointsSerie = new LineGraphSeries<>(list);
			datapointsSerie.setDrawDataPoints(true);
			datapointsSerie.setColor(getContext().getResources().getColor(R.color.red));
			datapointsSerie.setTitle("Daily Average");
			graphView.addSeries(datapointsSerie);

			LineGraphSeries<DataPoint> averageSerie = new LineGraphSeries<>(Arrays.copyOfRange(averages, 0, period.getDays()));
			averageSerie.setColor(getContext().getResources().getColor(R.color.colorPrimary));
			averageSerie.setTitle("Trend");
			graphView.addSeries(averageSerie);

			graphView.getViewport().setMinX(datapointsSerie.getLowestValueX());
			graphView.getViewport().setMaxX(datapointsSerie.getHighestValueX());
			graphView.getViewport().setXAxisBoundsManual(true);

			graphView.getGridLabelRenderer().setNumHorizontalLabels(period.getNbLabels());
			graphView.getGridLabelRenderer().setNumVerticalLabels(10);
			graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
			graphView.getLegendRenderer().setVisible(true);
			graphView.getLegendRenderer().setBackgroundColor(getContext().getResources().getColor(R.color.colorAccentSemiTransparent));
			graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

			graphView.getGridLabelRenderer().setHumanRounding(false, true);


		}
	}
}
