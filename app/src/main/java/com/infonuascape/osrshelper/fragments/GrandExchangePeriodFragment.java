package com.infonuascape.osrshelper.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.GrandExchangePeriods;
import com.infonuascape.osrshelper.views.GrandExchangeAxisFormatter;
import com.infonuascape.osrshelper.views.GrandExchangePointDialog;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Arrays;

public class GrandExchangePeriodFragment extends OSRSPagerFragment implements OnDataPointTapListener {
	private static final String TAG = "GrandExchangePeriodFrag";

	private final static String EXTRA_GE_PERIOD = "EXTRA_GE_PERIOD";
	private GraphView graphView;
	private ProgressBar progressBar;

	private GrandExchangePeriods period;
	private AlertDialog dialog;

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

		progressBar = view.findViewById(R.id.progress_bar);

		period = (GrandExchangePeriods) getArguments().getSerializable(EXTRA_GE_PERIOD);

		graphView = view.findViewById(R.id.graph);
		graphView.setVisibility(View.GONE);

		graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
		graphView.getGridLabelRenderer().setLabelFormatter(new GrandExchangeAxisFormatter(getActivity(), period.getFormat()));

		return view;
	}

	public void onPageVisible(final DataPoint[] datapoints, DataPoint[] averages) {
		if(datapoints != null && averages != null) {
			progressBar.setVisibility(View.GONE);
			graphView.removeAllSeries();
			DataPoint[] list = Arrays.copyOfRange(datapoints, Math.max(0, datapoints.length - period.getDays()), datapoints.length);
			LineGraphSeries<DataPoint> datapointsSerie = new LineGraphSeries<>(list);
			datapointsSerie.setDrawDataPoints(true);
			datapointsSerie.setColor(getContext().getResources().getColor(R.color.green));
			datapointsSerie.setTitle(getContext().getResources().getString(R.string.daily_average));
			datapointsSerie.setOnDataPointTapListener(this);
			graphView.addSeries(datapointsSerie);

			LineGraphSeries<DataPoint> averageSerie = new LineGraphSeries<>(Arrays.copyOfRange(averages, Math.max(0, averages.length - period.getDays()), averages.length));
			averageSerie.setColor(getContext().getResources().getColor(R.color.orange));
			averageSerie.setTitle(getContext().getResources().getString(R.string.trend));
			averageSerie.setDrawDataPoints(true);
			averageSerie.setDataPointsRadius(6f);
			averageSerie.setOnDataPointTapListener(this);
			graphView.addSeries(averageSerie);

			graphView.getViewport().setMinX(datapointsSerie.getLowestValueX());
			graphView.getViewport().setMaxX(datapointsSerie.getHighestValueX());
			graphView.getViewport().setXAxisBoundsManual(true);

			graphView.getGridLabelRenderer().setNumHorizontalLabels(period.getNbLabels());
			graphView.getGridLabelRenderer().setNumVerticalLabels(10);
			graphView.getLegendRenderer().setVisible(true);
			graphView.getLegendRenderer().setBackgroundColor(getContext().getResources().getColor(R.color.grand_exchange_legend));
			graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

			graphView.getGridLabelRenderer().setHumanRounding(false, true);

			graphView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onTap(Series series, DataPointInterface dataPointInterface) {
		Log.i(TAG, "onTap: series title=" + series.getTitle());
		if(dialog == null || !dialog.isShowing()) {
			dialog = GrandExchangePointDialog.showDialog(getContext(), series.getTitle(), dataPointInterface);
		}
	}

	@Override
	public void onPageVisible() {

	}
}
