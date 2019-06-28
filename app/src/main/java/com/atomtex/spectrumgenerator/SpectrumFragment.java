package com.atomtex.spectrumgenerator;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atomtex.spectrumgenerator.chart.CustomLineChartRenderer;
import com.atomtex.spectrumgenerator.chart.CustomOnChartGestureListener;
import com.atomtex.spectrumgenerator.chart.CustomXAxisRenderer;
import com.atomtex.spectrumgenerator.domain.NucIdent;
import com.atomtex.spectrumgenerator.util.Util;
import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.atomtex.spectrumgenerator.util.Util.scaleCbr;
import static com.atomtex.spectrumgenerator.util.Util.unScaleCbr;


/**
 * The fragment presents chart for showing spectrum and features for interaction with it.
 * <p>
 * For buttons on the chart was used 'com.github.uin3566:AllAngleExpandableButton' library
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class SpectrumFragment extends Fragment implements ButtonEventListener, OnChartValueSelectedListener {

    public static final String TAG = "TAGGG!!!";
    /**
     * Fragment argument {@link #mSpecDTO}
     */
    public static final String ARG_DTO = "spectrumArgument";
    /**
     * Fragment argument {@link #mPeaks}
     */
    public static final String ARG_PEAKS = "peaksArgument";
    /**
     * Fragment argument {@link #mPeakEnergies}
     */
    public static final String ARG_PEAKS_ENERGY = "peaksEnergyArgument";
    /**
     * Fragment argument {@link #mLineOwners}
     */
    public static final String ARG_LINE_OWNERS = "lineOwnersArgument";

    /**
     * Extra for saving buttons states between changing configuration.
     */
    private static final String EXTRA_BUTTON_STATE = "buttonState";
    /**
     * Extra for saving {@link LimitLine} on the spectrum between changing configuration.
     */
    private static final String EXTRA_LIMIT_LINE = "limitLine";

    /**
     * Extra for saving {@link Highlight} data between changing configuration.
     */
    private static final String EXTRA_HIGHLIGHTED = "highLightedPosition";

    /**
     * Id of the button which responsible for changing type of showing of spectrum.
     * Whether the spectrum will be shown as aplenty of dots or those dots will be linking with line.
     */
    private static final int BUTTON_CHART_TYPE = 1;
    /**
     * Id of the button which responsible for changing type of scale on the chart.
     * Whether the scale of chart will be linear or logarithmic.
     */
    private static final int BUTTON_DATA_MODE = 2;
    /**
     * Id of the button which responsible for changing type value on x axis.
     * Whether the axis will show energies or channels.
     */
    private static final int BUTTON_X_AXIS_MODE = 3;

    /**
     * Id of the button which allows to put marker on the chart.
     */
    private static final int BUTTON_FLAG = 4;
    /**
     * * Id of the button which allows to share the spectrum data.
     */
    private static final int BUTTON_SHARE = 5;

    /**
     * The instance of a {@link Context}
     */
    private Context mContext;

    /**
     * Contains spectrum data
     */
    private SpecDTO mSpecDTO;

    /**
     * Chart for showing spectrum
     */
    private LineChart mSpectrumChart;

    /**
     * List of entries foe chart
     */
    private List<Entry> mEntries;

    /**
     * Array of peaks over the spectrum
     */
    private float[] mPeaks;

    /**
     * Energies of the {@link #mPeaks}
     */
    private float[] mPeakEnergies;

    /**
     * describes the nuclides to which {@link #mPeaks} belong
     */
    private String[] mLineOwners;

    /**
     * Position on the chart that was marked by user
     */
    private LimitLine mRememberedPosition;

    /**
     * Highlighted by user position on the chart
     */
    private Highlight mPrevHighlight;

    /**
     * States of button to restore after configuration changing.
     */
    private boolean[] mStates;

    //bind layout views
    @BindView(R.id.graph_container)
    FrameLayout graphContainer;
    @BindView(R.id.button_controls)
    AllAngleExpandableButton buttonControls;
    @BindView(R.id.status_channel)
    TextView status_channel_tv;
    @BindView(R.id.status_energy)
    TextView status_energy_tv;
    @BindView(R.id.status_imp_value)
    TextView status_imp_value_tv;
    @BindView(R.id.status_spectrum_time)
    TextView status_time_tv;
    @BindView(R.id.status_channel_range)
    TextView status_channel_range_tv;
    @BindView(R.id.status_imp_range_value)
    TextView status_imp_range_tv;
    @BindView(R.id.status_imp_speed)
    TextView status_imp_speed_tv;
//    @BindView(R.id.status_temperature)
//    TextView status_temperature_tv;


    private String fragmentID;
    SpecDTO dto;
    float[] peaks;
    float[] peakEnergies;
    String[] lineOwners;

    /**
     * Creates new instance of the class and put given parameters as arguments in it
     *
     * @param dto          contains spectrum data
     * @param peaks        array of peaks on the spectrum
     * @param peakEnergies energies of the peaks
     * @param lineOwners   describes the nuclides to which these lines belong
     * @return new instance of the class with arguments
     * @see SpecDTO
     */
    public static SpectrumFragment newInstance(SpecDTO dto, float[] peaks, float[] peakEnergies
            , String[] lineOwners) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DTO, dto);
        args.putFloatArray(ARG_PEAKS, peaks);
        args.putFloatArray(ARG_PEAKS_ENERGY, peakEnergies);
        args.putStringArray(ARG_LINE_OWNERS, lineOwners);
        SpectrumFragment fragment = new SpectrumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SpectrumFragment newInstance(SpecDTO dto, float[] peaks, float[] peakEnergies
            , String[] lineOwners, String fragmentID) {
        SpectrumFragment fragment = new SpectrumFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DTO, dto);
        args.putFloatArray(ARG_PEAKS, peaks);
        args.putFloatArray(ARG_PEAKS_ENERGY, peakEnergies);
        args.putStringArray(ARG_LINE_OWNERS, lineOwners);
        args.putString("FR_ID", fragmentID);
//        fragment.fragmentID = fragmentID;
        fragment.setArguments(args);
        Log.e(TAG, "FRAGMENT " + fragmentID + " newInstance");
        return fragment;
    }
    public static SpectrumFragment newInstance(SpecDTO dto, float[] peaks, float[] peakEnergies
            , String[] lineOwners, String fragmentID, String path) {
        SpectrumFragment fragment = new SpectrumFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DTO, dto);
        args.putFloatArray(ARG_PEAKS, peaks);
        args.putFloatArray(ARG_PEAKS_ENERGY, peakEnergies);
        args.putStringArray(ARG_LINE_OWNERS, lineOwners);
        args.putString("FR_ID", fragmentID);
        fragment.setArguments(args);
        return fragment;
    }
    public static SpectrumFragment newInstance(String fragmentID) {

        SpectrumFragment fragment = new SpectrumFragment();
        Bundle args = new Bundle();
        //todo затычки
        SpecDTO dto = new SpecDTO();
        dto = new SpecDTO();
        dto.setSpectrum(new int[]{0});
        dto.setMeasTim(new int[]{0});
        dto.setEnergy(new float[]{0});
        //
        args.putParcelable(ARG_DTO, dto);
        args.putFloatArray(ARG_PEAKS, null);
        args.putFloatArray(ARG_PEAKS_ENERGY, null);
        args.putStringArray(ARG_LINE_OWNERS, null);
        args.putString("FR_ID", fragmentID);
        fragment.setArguments(args);
        return fragment;
    }

/*    public static SpectrumFragment getInstance() {
        return
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            //get dto to show
            mSpecDTO = arguments.getParcelable(ARG_DTO);
            if (mSpecDTO != null) {
                mEntries = new ArrayList<>();
                //make chart entry from spectrum data
                int[] spectrum = mSpecDTO.getSpectrum();
                for (int i = 0; i < spectrum.length; i++) {
                    mEntries.add(new Entry(i, spectrum[i]));
                }
                mPeaks = arguments.getFloatArray(ARG_PEAKS);
                mPeakEnergies = arguments.getFloatArray(ARG_PEAKS_ENERGY);
                mLineOwners = arguments.getStringArray(ARG_LINE_OWNERS);

                fragmentID = arguments.getString("FR_ID");

            }
        }
    }

/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("IsRefresh", "Yes");
        }
    }*/

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBooleanArray(EXTRA_BUTTON_STATE, mStates);
        if (mPrevHighlight != null) {
            outState.putFloatArray(EXTRA_HIGHLIGHTED, new float[]{mPrevHighlight.getX(), mPrevHighlight.getY()});
        }
        if (mRememberedPosition != null) {
            outState.putFloat(EXTRA_LIMIT_LINE, mRememberedPosition.getLimit());
        }
        super.onSaveInstanceState(outState);
    }

    View fragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_spectrum, container, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spectrum, container, false); //todo так было
        ButterKnife.bind(this, view);

        TextView frIDtext = view.findViewById(R.id.fragment_id);
        frIDtext.setText(fragmentID);
        /*chViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Tag", "onClick: ");
//                Toast.makeText(getActivity(), "fragment ID = " + getFragmentID(), Toast.LENGTH_SHORT).show();
                getFragmentID();
            }
        });*/

        //Set name of spectrum file as an action bar title
        ActionBar supportActionBar = ((AppCompatActivity) mContext).getSupportActionBar(); //todo было FragmentActivity вместо AppCompatActivity
        if (supportActionBar != null) {
            supportActionBar.setTitle(mSpecDTO.getFileName()
                    .substring(mSpecDTO.getFileName().lastIndexOf("com.android")));
//                    .substring(mSpecDTO.getFileName().lastIndexOf("spec_")));

        }

        final List<ButtonData> buttonDataList = new ArrayList<>();

        //Images of the chart buttons
        int[] drawable = new int[]{R.drawable.ic_plus, R.drawable.toggle_chart, R.drawable.toggle_log,
                R.drawable.toggle_energy, R.drawable.toggle_flag, R.drawable.ic_share};

        if (savedInstanceState == null) {
            mStates = new boolean[drawable.length];
        } else {
            mStates = savedInstanceState.getBooleanArray(EXTRA_BUTTON_STATE);
            float[] prevHighlightArray = savedInstanceState.getFloatArray(EXTRA_HIGHLIGHTED);
            if (prevHighlightArray != null) {
                mPrevHighlight = new Highlight(prevHighlightArray[0], prevHighlightArray[1], 0);
            }
            float prevLimitLine = savedInstanceState.getFloat(EXTRA_LIMIT_LINE);
            if (prevLimitLine != 0) {
                mRememberedPosition = createRememberedPosition(new Highlight(prevLimitLine, 0, 0));
            }
        }

        //for buttons was used library: com.github.uin3566:AllAngleExpandableButton
        for (int i = 0; i < drawable.length; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(mContext, drawable[i], 5);
            buttonData.getIcon().setState(mStates[i] ? new int[]{android.R.attr.state_checked} : new int[0]);
            buttonDataList.add(buttonData);
        }
        buttonControls.setButtonDatas(buttonDataList);

        buttonControls.setButtonEventListener(this);

        updateChart();

        return view;
    }

    /**
     * Creates a chart and draw the spectrum data on it.
     */
    private void updateChart() {
        if (mSpectrumChart == null) {
            mSpectrumChart = new LineChart(getContext());
            XAxis xAxis = mSpectrumChart.getXAxis();
            YAxis yAxis = mSpectrumChart.getAxisLeft();
            xAxis.setTextColor(Color.YELLOW);
            yAxis.setTextColor(Color.YELLOW);

            //Set renderer for x axis
            mSpectrumChart.setXAxisRenderer(new CustomXAxisRenderer(mSpectrumChart.getViewPortHandler()
                    , mSpectrumChart.getXAxis(), mSpectrumChart.getTransformer(YAxis.AxisDependency.LEFT)));

            mSpectrumChart.setDoubleTapToZoomEnabled(false);
            mSpectrumChart.setOnChartGestureListener(new CustomOnChartGestureListener(mSpectrumChart));

            //set common renderer
            mSpectrumChart.setRenderer(new CustomLineChartRenderer(mSpectrumChart, mSpectrumChart.getAnimator()
                    , mSpectrumChart.getViewPortHandler()));
            xAxis.setGridColor(getResources().getColor(R.color.colorChartGrid));

            yAxis.setGridColor(getResources().getColor(R.color.colorChartGrid));
            //enable acceleration
            mSpectrumChart.setHardwareAccelerationEnabled(true);
            graphContainer.addView(mSpectrumChart);
            Description description = new Description();
            description.setEnabled(false);
            mSpectrumChart.setDescription(description);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setAxisLineColor(Color.YELLOW);
            yAxis.setAxisLineColor(Color.YELLOW);
            yAxis.setDrawZeroLine(true); // draw a zero line
            yAxis.setAxisMinimum(0);
            mSpectrumChart.getAxisRight().setEnabled(false); // no right axis
            mSpectrumChart.getAxisLeft().setGranularity(1);
            mSpectrumChart.setOnChartValueSelectedListener(this);

            int[] spectrum = mSpecDTO.getSpectrum();

            xAxis.setAxisMaximum(spectrum.length);

        }

        LineDataSet dataSet = new LineDataSet(mEntries, null);
        mSpectrumChart.getLegend().setEnabled(false);
        dataSet.setColor(Color.WHITE);
        dataSet.setLineWidth(0);
        dataSet.setDrawValues(false);
        dataSet.setHighLightColor(Color.RED);

        CustomLineChartRenderer renderer = (CustomLineChartRenderer) mSpectrumChart.getRenderer();

        //set type of showing of the  spectrum according to current buttons configuration
        if (mStates[BUTTON_CHART_TYPE]) {
            renderer.setDottedMode(true);
            dataSet.setDrawCircles(true);
            dataSet.setCircleRadius(1);
            dataSet.setCircleColor(Color.WHITE);
            dataSet.setCircleHoleColor(Color.WHITE);
        } else {
            renderer.setDottedMode(false);
            dataSet.setDrawCircles(false);
        }

/*        if (mStates[BUTTON_DATA_MODE]) {
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            int x = Util.getNumberOfDigits(unScaleCbr(dataSet.getYMax()));
            format.setMaximumIntegerDigits(x + 1);
            mSpectrumChart.getAxisLeft().setValueFormatter((value, axis) -> format.format(unScaleCbr(value)));
        } else {
            mSpectrumChart.getAxisLeft().setValueFormatter(new DefaultAxisValueFormatter(0));
        }

        if (mStates[BUTTON_X_AXIS_MODE]) {
            mSpectrumChart.getXAxis().setValueFormatter(new DefaultAxisValueFormatter(0));
        } else {
            mSpectrumChart.getXAxis().setValueFormatter((value, axis) ->
                    String.valueOf((int) NucIdent.getEnergyFromChannel(mSpecDTO.getEnergy(), value)));
        }*/

        LineData lineData = new LineData(dataSet);
        mSpectrumChart.setData(lineData);
        updatePeakLines();
        updateChartStatus();
        mSpectrumChart.invalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        mPrevHighlight = h;
        if (mStates[BUTTON_FLAG] && mRememberedPosition == null) {
            mRememberedPosition = createRememberedPosition(h);
            rememberPosition(true);
            mSpectrumChart.invalidate();
        }
        updateChartStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNothingSelected() {

    }

    /**
     * Allows to mark some position on the chart or delete previous one according to given parameter.
     * <p>
     * Updates chart status data according the position.
     *
     * @param isChecked if the button that responsible for remembering positions is checked
     *                  at the moment.
     */
    private void rememberPosition(boolean isChecked) {
        //create new limit line if isChecked or remove other way
        if (isChecked) {
            if (mRememberedPosition == null) {
                if (mPrevHighlight == null) {
                    return;
                }
                mRememberedPosition = createRememberedPosition(mPrevHighlight);
            }
            mSpectrumChart.getXAxis().addLimitLine(mRememberedPosition);
        } else {
            if (mRememberedPosition != null) {
                mSpectrumChart.getXAxis().removeLimitLine(mRememberedPosition);
            }
            mRememberedPosition = null;
            updateChartStatus();
        }
    }

    /**
     * Creates a new limit line on the chart in position of given {@link Highlight}
     *
     * @param h highlight
     * @return limit line in position of given {@link Highlight}
     */
    private LimitLine createRememberedPosition(Highlight h) {
        LimitLine line;
        float channel = h.getX();
        if (mStates[BUTTON_X_AXIS_MODE]) {
            line = new LimitLine(Math.round(channel), String.valueOf(channel));
        } else {
            String value = String.valueOf(NucIdent.getEnergyFromChannel(mSpecDTO.getEnergy()
                    , channel));
            line = new LimitLine(Math.round(channel), value);
        }
        line.setLineColor(Color.RED);
        line.setLineWidth(0.5f);
        line.setTextColor(Color.RED);
        line.setTextStyle(Paint.Style.FILL);
        return line;
    }

    /**
     * Updates spectrum status data.
     */
    private void updateChartStatus() {

        int startChannel = 0;
        int endChannel = mEntries.size() - 1;
        int impSum = 0;

        if (mPrevHighlight == null) {
            status_channel_tv.setText(String.format(getString(R.string.chart_channel), "…"));
            status_energy_tv.setText(String.format(getString(R.string.chart_energy), 0));
            status_imp_value_tv.setText(String.format(getString(R.string.chart_imp), 0));
        } else {
            mSpectrumChart.highlightValue(mPrevHighlight);
            status_channel_tv.setText(String.format(getString(R.string.chart_channel)
                    , String.valueOf((int) mPrevHighlight.getX())));

            int x = (int) mPrevHighlight.getX();
            status_energy_tv.setText(String.format(getString(R.string.chart_energy),
                    (int) NucIdent.getEnergyFromChannel(mSpecDTO.getEnergy(), x)));

            float impValue = mEntries.get(x).getY();
            if (mStates[BUTTON_DATA_MODE]) {
                impValue = unScaleCbr(impValue);
            }
            status_imp_value_tv.setText(String.format(getString(R.string.chart_imp),
                    (int) impValue));

            if (mRememberedPosition != null) {
                startChannel = (int) Math.min(mRememberedPosition.getLimit(), mPrevHighlight.getX());
                endChannel = (int) Math.max(mRememberedPosition.getLimit(), mPrevHighlight.getX());
            }
        }

        int timeSpectrum = mSpecDTO.getMeasTim()[0];
        status_time_tv.setText(String.format(getString(R.string.chart_time), timeSpectrum));

        status_channel_range_tv.setText(String.format(getString(R.string.chart_range), startChannel,
                endChannel));


        for (int i = startChannel; i <= endChannel; i++) {
            Entry entry = mEntries.get(i);
            if (mStates[BUTTON_DATA_MODE]) {
                impSum += unScaleCbr(entry.getY());
            } else {
                impSum += entry.getY();
            }
        }
        status_imp_range_tv.setText(String.format(getString(R.string.chart_imp), impSum));
        status_imp_speed_tv.setText(String.format(getString(R.string.chart_imp_speed),
                (int) ((float) impSum / timeSpectrum)));

//        status_temperature_tv.setText(String.format(Locale.US, getString(R.string.chart_temperature)
//                , mSpecDTO.getTemperature()));
    }

    /**
     * Draws peak lines on spectrum chart
     */
    private void updatePeakLines() {
        //clear previous lines
        XAxis axis = mSpectrumChart.getXAxis();
        axis.removeAllLimitLines();
        if (mPeaks != null && mPeaks.length > 0) {
            for (int i = 0; i < mPeaks.length; i++) {
                float peak = mPeaks[i];
                LimitLine line;
                //make label for line
                String name = mLineOwners[i] == null ? "" : " " + mLineOwners[i];
                if (mStates[BUTTON_X_AXIS_MODE]) {
                    line = new LimitLine(Math.round(peak), String.format(Locale.US
                            , "%.1f %s", peak, name));
                } else {
                    String label = String.format(Locale.US, "%.1f %s",
                            mPeakEnergies[i], name);
                    line = new LimitLine(Math.round(peak), label);
                }

                line.setLineColor(Color.GREEN);
                line.setLineWidth(0.2f);
                line.setTextColor(Color.GREEN);
                line.setTextStyle(Paint.Style.FILL);
                axis.addLimitLine(line);
            }
        }
        //crete line that was marked by user
        if (mRememberedPosition != null) {
            if (mStates[BUTTON_X_AXIS_MODE]) {
                mRememberedPosition.setLabel(String.valueOf(mRememberedPosition.getLimit()));
            } else {
                mRememberedPosition.setLabel(String.format(Locale.US, "%.1f",
                        NucIdent.getEnergyFromChannel(mSpecDTO.getEnergy(),
                                mRememberedPosition.getLimit())));
            }
            axis.addLimitLine(mRememberedPosition);
        }
    }

    /**
     * Do action according to button that has been pressed.
     *
     * @param i the id of pressed button
     */
    @Override
    public void onButtonClicked(int i) {
        boolean state = !mStates[i];
        mStates[i] = state;
        Drawable icon = buttonControls.getButtonDatas().get(i).getIcon();
        if (state) {
            icon.setState(new int[]{android.R.attr.state_checked});
        } else {
            icon.setState(new int[0]);
        }

        if (i == BUTTON_DATA_MODE) {
            if (mStates[BUTTON_DATA_MODE]) {
                mSpectrumChart.getAxisLeft().setGranularity(0.15f);
                for (Entry entry : mEntries) {
                    entry.setY(scaleCbr(entry.getY()));
                }
            } else {
                mSpectrumChart.getAxisLeft().setGranularity(1);
                for (Entry entry : mEntries) {
                    entry.setY(unScaleCbr(entry.getY()));
                }
            }
            updateChart();
        } else if (i == BUTTON_FLAG) {
            rememberPosition(state);
            mSpectrumChart.invalidate();
        } else if (i == BUTTON_SHARE) {
            Uri path = Uri.parse(mSpecDTO.getFileName());
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("vnd.android.cursor.dir/email");
            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Spectrum file");
            startActivity(Intent.createChooser(emailIntent, "Send file"));
        } else {
            updateChart();
        }
    }

    @Override
    public void onExpand() {

    }

    @Override
    public void onCollapse() {

    }

}
