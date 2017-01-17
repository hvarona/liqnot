package com.henja.liqnot;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.henja.liqnot.app.LiqNotApp;
import com.henja.liqnot.ws.ApiCalls;
import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.ConnectionException;
import com.henja.liqnot.ws.GetAccountInfo;
import com.henja.liqnot.ws.WebsocketWorkerThread;

import java.util.ArrayList;

import bo.Account;
import bo.Asset;
import bo.CurrencyOperatorValueNotifierRule;
import bo.Notifier;
import bo.NotifierDirector;
import bo.NotifierRuleOperator;
import bo.SharedDataCentral;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrencyOperatorValueNotifierRuleFragment.OnCurrencyOperatorValueNotifierFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrencyOperatorValueNotifierRuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrencyOperatorValueNotifierRuleFragment extends Fragment {
    private NotifierDirector notifierDirector;
    private CurrencyOperatorValueNotifierRule rule;
    private OnCurrencyOperatorValueNotifierFragmentInteractionListener mListener;
    private CountDownTimer searchForAccountInfoTimer;
    private Account account;
    private String lastAccountNameCall;
    private ApiFunction lastApiFunctionCall;

    public CurrencyOperatorValueNotifierRuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CurrencyOperatorValueNotifierRuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrencyOperatorValueNotifierRuleFragment newInstance() {
        return new CurrencyOperatorValueNotifierRuleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rule = new CurrencyOperatorValueNotifierRule();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.notifierDirector = ((LiqNotApp)getActivity().getApplication()).getNotifierDirector();//(NotifierDirector) getArguments().getSerializable(CurrencyOperatorValueNotifierRuleFragment.NOTIFIER_DIRECTOR_KEY);
        this.notifierDirector.addNotifierDirectorListener(new NotifierDirector.NotifierDirectorListener() {
            @Override
            public void OnNewNotifier(Notifier notifier) {
                //
            }

            @Override
            public void OnNotifierRemoved(Notifier notifier) {
                //
            }

            @Override
            public void OnAssetsLoaded() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Spinner baseCurrencySpinner = (Spinner) getView().findViewById(R.id.base_currency_recycler_view);
                        final Spinner quotedCurrencySpinner = (Spinner) getView().findViewById(R.id.quoted_currency_recycler_view);

                        ArrayAdapter<String> baseCurrencyAdapter = (ArrayAdapter) baseCurrencySpinner.getAdapter();
                        baseCurrencyAdapter.clear();
                        baseCurrencyAdapter.addAll(SharedDataCentral.getAssetsList());
                        ArrayAdapter<String> quotedCurrencyAdapter = (ArrayAdapter) quotedCurrencySpinner.getAdapter();
                        quotedCurrencyAdapter.clear();
                        quotedCurrencyAdapter.addAll(SharedDataCentral.getSmartcoinAssesList());
                    }
                });
            }
        });


        View v = inflater.inflate(R.layout.fragment_currency_operator_value_notifier_rule, container, false);

        final EditText accountNameEditText = (EditText) v.findViewById(R.id.account_name_edit_text);
        accountNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                accountNameEditText.setTextColor(Color.BLACK);
                searchForAccountInfoTimer.cancel();
                account = SharedDataCentral.getAccount(s.toString());

                if (rule != null) {
                    rule.setAccount(account);

                    if ((account == null) || (account.getId() == null) || (account.getId().equals(""))) {
                        account = new Account(s.toString());
                        searchForAccountInfoTimer.start();
                    } else {
                        accountNameEditText.setTextColor(Color.GREEN);
                    }
                }
                checkRule();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Creation of the timer for searching the user info inserted by the user
        this.searchForAccountInfoTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                try{
                final ProgressBar accountNameProgressBar = (ProgressBar) getView().findViewById(R.id.accountNameProgressBar);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        accountNameProgressBar.setVisibility(View.VISIBLE);
                    }
                });

                if (!account.getName().equals("")) {
                    lastAccountNameCall = account.getName();
                    ApiCalls apiCalls = new ApiCalls();
                    ApiFunction function = new GetAccountInfo(account.getName());
                    lastApiFunctionCall = function;
                    apiCalls.addFunction(function);
                    apiCalls.addListener(new ApiCalls.ApiCallsListener() {
                        @Override
                        public void OnAllDataReceived() {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    if (lastAccountNameCall.equals(account.getName())) {
                                        accountNameProgressBar.setVisibility(View.GONE);

                                        Account accountInfo = SharedDataCentral.getAccount(account.getName());
                                        if (accountInfo != null) {
                                            account = accountInfo;
                                            rule.setAccount(account);
                                            accountNameEditText.setTextColor(Color.GREEN);
                                            checkRule();
                                        } else {
                                            //TODO notify the user that the account doesn't exists
                                            accountNameEditText.setTextColor(Color.RED);
                                            checkRule();
                                        }
                                    }
                                }
                            });
                        }

                        @Override
                        public void OnError(final ApiFunction errorFunction) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    if (lastApiFunctionCall == errorFunction) {
                                        accountNameProgressBar.setVisibility(View.GONE);
                                        accountNameEditText.setTextColor(Color.RED);
                                    }
                                }
                            });
                            searchForAccountInfoTimer.cancel();
                            searchForAccountInfoTimer.start();
                        }


                        @Override
                        public void OnConnectError() {
                            System.out.println("account " + account.getName() + " connection error");
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    accountNameProgressBar.setVisibility(View.GONE);
                                }
                            });
                            searchForAccountInfoTimer.cancel();
                            searchForAccountInfoTimer.start();
                        }
                    });
                    System.out.println("Preparing the websocketthread");
                    WebsocketWorkerThread wsthread;
                    try {
                        wsthread = new WebsocketWorkerThread(apiCalls);
                        wsthread.start();
                    } catch (ConnectionException e) {
                        System.out.println("account" + account.getName() + " connection error");
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                accountNameProgressBar.setVisibility(View.GONE);
                            }
                        });
                        searchForAccountInfoTimer.cancel();
                        searchForAccountInfoTimer.start();
                    }
                }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        ArrayList<String> baseCurrencyStringList;
        if (SharedDataCentral.getAssetsCount() <= 0){
            baseCurrencyStringList = new ArrayList<String>();
            baseCurrencyStringList.add("Loading...");
        } else {
            baseCurrencyStringList = SharedDataCentral.getAssetsList();
        }

        ArrayAdapter<String> baseCurrencyAdapter = new ArrayAdapter<>(this.getContext(),R.layout.spinner_layout,baseCurrencyStringList);
        final Spinner baseCurrencySpinner = (Spinner) v.findViewById(R.id.base_currency_recycler_view);
        baseCurrencySpinner.setAdapter(baseCurrencyAdapter);
        baseCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Asset baseCurrency = SharedDataCentral.getAssetBySymbol(baseCurrencySpinner.getSelectedItem().toString());
                    rule.setBaseCurrency(baseCurrency);
                } catch(IllegalArgumentException e){
                    rule.setBaseCurrency(null);
                }
                checkRule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> quotedCurrencyStringList;
        if (SharedDataCentral.getAssetsCount() <= 0){
            quotedCurrencyStringList = new ArrayList<>();
            quotedCurrencyStringList.add("Loading...");
        } else {
            quotedCurrencyStringList = SharedDataCentral.getSmartcoinAssesList();
        }

        ArrayAdapter<String> quotedCurrencyAdapter = new ArrayAdapter<>(this.getContext(),R.layout.spinner_layout,quotedCurrencyStringList);
        final Spinner quotedCurrencySpinner = (Spinner) v.findViewById(R.id.quoted_currency_recycler_view);
        quotedCurrencySpinner.setAdapter(quotedCurrencyAdapter);
        quotedCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Asset quotedCurrency = SharedDataCentral.getAssetBySymbol(quotedCurrencySpinner.getSelectedItem().toString());
                    rule.setQuotedCurrency(quotedCurrency);
                } catch(IllegalArgumentException e){
                    rule.setQuotedCurrency(null);
                }
                checkRule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner operatorSpinner = (Spinner) v.findViewById(R.id.operator_spinner);
        final ArrayAdapter<CharSequence> operatorAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.operators_array, R.layout.spinner_layout);
        operatorSpinner.setAdapter(operatorAdapter);
        operatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String operatorLabel = operatorAdapter.getItem(position).toString();

                switch(operatorLabel){
                    case "<":
                        rule.setOperator(NotifierRuleOperator.LESS_THAN);
                        break;
                    case ">":
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
                try {
                    rule.setValue(Double.parseDouble(s.toString()));
                } catch (NumberFormatException e){
                    rule.setValue(-1);
                }
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

    public void cleanFragment(){
        View v = getView();
        this.rule = new CurrencyOperatorValueNotifierRule();
        this.account = null;
        this.lastAccountNameCall = null;
        this.lastApiFunctionCall = null;
        this.rule = new CurrencyOperatorValueNotifierRule();
        this.account = null;
        this.lastAccountNameCall = null;
        this.lastApiFunctionCall = null;

        EditText accountNameEditText = (EditText) v.findViewById(R.id.account_name_edit_text);
        accountNameEditText.setText("");
        final Spinner baseCurrencySpinner = (Spinner) v.findViewById(R.id.base_currency_recycler_view);
        baseCurrencySpinner.setSelection(0);
        final Spinner quotedCurrencySpinner = (Spinner) v.findViewById(R.id.quoted_currency_recycler_view);
        quotedCurrencySpinner.setSelection(0);
        Spinner operatorSpinner = (Spinner) v.findViewById(R.id.operator_spinner);
        operatorSpinner.setSelection(0);
        EditText valueEditText = (EditText) v.findViewById(R.id.value_edit_text);
        valueEditText.setText("");
        Button okButton = (Button) v.findViewById(R.id.button_ok);
        okButton.setEnabled(false);

    }

    public void createNotifier(){
        if (this.rule.isValid()){
            Notifier newNotifier = new Notifier("");
            newNotifier.setRule(this.rule);
            cleanFragment();
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
}
