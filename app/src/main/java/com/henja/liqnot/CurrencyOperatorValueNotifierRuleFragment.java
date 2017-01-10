package com.henja.liqnot;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import bo.CurrencyOperatorValueNotifierRule;
import bo.Notifier;
import bo.NotifierCurrency;
import bo.NotifierCurrencyData;
import bo.NotifierDirector;
import bo.NotifierRuleOperator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrencyOperatorValueNotifierRuleFragment.OnCurrencyOperatorValueNotifierFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrencyOperatorValueNotifierRuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrencyOperatorValueNotifierRuleFragment extends Fragment implements CurrencySelectionRecyclerViewAdapter.CurrencyListener {
    private static final String NOTIFIER_DIRECTOR_KEY = "notifier_director_key";
    private NotifierDirector notifierDirector;
    private CurrencyOperatorValueNotifierRule rule;
    private OnCurrencyOperatorValueNotifierFragmentInteractionListener mListener;

    public CurrencyOperatorValueNotifierRuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notifierDirector the notifier director to include the new notifier.
     * @return A new instance of fragment CurrencyOperatorValueNotifierRuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrencyOperatorValueNotifierRuleFragment newInstance(NotifierDirector notifierDirector) {
        CurrencyOperatorValueNotifierRuleFragment fragment = new CurrencyOperatorValueNotifierRuleFragment();
        Bundle args = new Bundle();
        args.putSerializable(CurrencyOperatorValueNotifierRuleFragment.NOTIFIER_DIRECTOR_KEY, notifierDirector);

        /*args.put putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        rule = new CurrencyOperatorValueNotifierRule();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.notifierDirector = (NotifierDirector) getArguments().getSerializable(CurrencyOperatorValueNotifierRuleFragment.NOTIFIER_DIRECTOR_KEY);


        View v = inflater.inflate(R.layout.fragment_currency_operator_value_notifier_rule, container, false);

        NotifierCurrencyData[] currenciesData = new NotifierCurrencyData[NotifierCurrency.values().length];
        int index = 0;

        for (NotifierCurrency currency : NotifierCurrency.values()) {
            currenciesData[index] = new NotifierCurrencyData(currency, null);
            index++;
        }

        CurrencySelectionRecyclerViewAdapter recyclerAdapter = new CurrencySelectionRecyclerViewAdapter(currenciesData, this);
        RecyclerView currencyRecyvlerView = (RecyclerView) v.findViewById(R.id.currency_recycler_view);
        currencyRecyvlerView.setAdapter(recyclerAdapter);


        Spinner operatorSpinner = (Spinner) v.findViewById(R.id.operator_spinner);
        final ArrayAdapter<CharSequence> operatorAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.operators_array, android.R.layout.simple_spinner_item);
        operatorSpinner.setAdapter(operatorAdapter);
        operatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String operatorLabel = operatorAdapter.getItem(position).toString();

                switch(operatorLabel){
                    case "Less than":
                        rule.setOperator(NotifierRuleOperator.LESS_THAN);
                        break;
                    case "Bigger than":
                        rule.setOperator(NotifierRuleOperator.BIGGER_THAN);
                        break;
                    default:
                        rule.setOperator(NotifierRuleOperator.UNKNOWN);
                }

                checkRule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                rule.setOperator(NotifierRuleOperator.UNKNOWN);
            }

        });


        EditText valueEditText = (EditText) v.findViewById(R.id.value_edit_text);
        valueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rule.setValue(Double.parseDouble(s.toString()));
                checkRule();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button okButton = (Button) v.findViewById(R.id.button_ok);
        okButton.setEnabled(false);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotifier();
            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnCurrencyOperatorValueNotifierFragmentInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCurrencyOperatorValueNotifierFragmentInteractionListener) {
            mListener = (OnCurrencyOperatorValueNotifierFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void createNotifier(){
        if (this.rule.isValid()){
            Notifier newNotifier = new Notifier("");
            newNotifier.setRule(this.rule);
            this.mListener.onNotifierCreated(newNotifier);
            this.notifierDirector.addNotifier(newNotifier);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCurrencyOperatorValueNotifierFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnCurrencyOperatorValueNotifierFragmentInteractionListener(Uri uri);

        public void onNotifierCreated(Notifier notifier);
    }

    public void checkRule(){
        Button okButton = (Button) getView().findViewById(R.id.button_ok);

        if (rule.isValid()){
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(false);
        }
    }

    @Override
    public void OnCurrencyClick(NotifierCurrency currency) {
        TextView currencyTextView = (TextView) this.getView().findViewById(R.id.currency_selected_text);
        currencyTextView.setText(currency.name());
        this.rule.setCurrency(currency);
        checkRule();
    }
}
