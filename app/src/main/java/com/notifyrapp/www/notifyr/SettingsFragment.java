package com.notifyrapp.www.notifyr;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.notifyrapp.www.notifyr.Business.Business;
import com.notifyrapp.www.notifyr.Business.CallbackInterface;
import com.notifyrapp.www.notifyr.Model.Article;
import com.notifyrapp.www.notifyr.Model.UserSetting;


public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context ctx;
    private boolean isDirtyServer = false;
    private boolean isDirtyLocal = false;
    private boolean truth;
    private int getMaxNotifications;
    private int swtchCounter;
    //private int seekbarValueFromUser;
    private int serverRadioButtonValue;
    private int userRadioButtonValue;
    private int articleReaderMode;
    private int checkStatus = 0;
    private WebViewFragment mWebViewFragment;
    TextView txtsettings, txtMaxNotification, txtMaxNotificationDescription, txtNotificationsPerDay, txtDownloadArticleImages, txtArticleReaderMode, txtArticleReaderModeDescription,
            txtAccountInformation, txtNetworkStatus, txtNetworkStatusGreen, txtAbout, txtVersion, txtVersionNumber;
    Button btnSendTestNotification, btnPrivacy, btnTerms, btnRateOnAppStore, btnSendFeedback;
    RadioButton RadioBtnNever, RadioBtnWifiOnly, RadioBtnAlways;
    RadioGroup radioGroup;
    Typeface openSansRegular, openSansLight;
    SeekBar seekBarMaxNotificationsPerDay;
    TextView txtSeekBarValue;
    Switch swtchArticleReaderMode;
    Business business;
    UserSetting settings;
    ProgressBar pbNetworkStatus;
    TableRow rowNetworkStatus;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //hardcode all the settings on the initial create
        //make the usersettings class global and instantiate it inside oncreate and set it to defaults
        //so when a person makes a change you should be able to test it against an initial flag
        //onleave if isdirty == true then save to the database or local
        //usersettings = new usersettings
        //save to database
        //load to database
        //UserSetting usersetting = new UserSetting();


        //set buttons equal to search
        //make a save funciton that is fake klein will take care of the actual saving




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        this.ctx = getContext();
        MainActivity act = (MainActivity) getActivity();
        act.abTitle.setText("Settings");
        act.btnEditDone.setVisibility(View.GONE);
        act.btnTrashCanDelete.setVisibility(View.GONE);

        //get settings info from the local database
        business = new Business(getActivity());
        settings = business.getUserSettings();
        rowNetworkStatus = (TableRow) view.findViewById(R.id.rowNetworkStatus);
        txtsettings = (TextView) view.findViewById(R.id.txtSettings);
        txtMaxNotification = (TextView) view.findViewById(R.id.txtMaxNotification);
        txtMaxNotificationDescription = (TextView) view.findViewById(R.id.txtMaxNotificationDescription);

        txtArticleReaderMode = (TextView) view.findViewById(R.id.txtArticleReaderMode);
        txtArticleReaderModeDescription = (TextView) view.findViewById(R.id.txtArticleReaderModeDescription);
        txtAccountInformation = (TextView) view.findViewById(R.id.txtAccountInformation);
        txtNetworkStatus = (TextView) view.findViewById(R.id.txtNetworkStatus);
        txtNetworkStatusGreen = (TextView) view.findViewById(R.id.txtNetworkStatusGreen);
        txtAbout = (TextView) view.findViewById(R.id.txtAbout);
        txtVersion = (TextView) view.findViewById(R.id.txtVersion);
        txtVersionNumber = (TextView) view.findViewById(R.id.txtVersionNumber);
        btnSendTestNotification = (Button) view.findViewById(R.id.btnSendTestNotification);
        btnPrivacy = (Button) view.findViewById(R.id.btnPrivacy);
        btnTerms = (Button) view.findViewById(R.id.btnTerms);
        btnRateOnAppStore = (Button) view.findViewById(R.id.btnRateOnAppStore);
        btnSendFeedback = (Button) view.findViewById(R.id.btnSendFeedback);
        txtNotificationsPerDay = (TextView) view.findViewById(R.id.txtSeekBarValue);
        //  txtDownloadArticleImages = (TextView) view.findViewById(R.id.txtDownloadArticleImages);
    //    radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
    //    RadioBtnNever = (RadioButton) view.findViewById(R.id.radioButtonNever);
    //    RadioBtnWifiOnly = (RadioButton) view.findViewById(R.id.radioButtonWifiOnly);
    //    RadioBtnAlways = (RadioButton) view.findViewById(R.id.radioButtonAlways);
        pbNetworkStatus = (ProgressBar) view.findViewById(R.id.pbNetworkStatus);
        openSansRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        openSansLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Light.ttf");

        txtsettings.setTypeface(openSansRegular);
        txtMaxNotification.setTypeface(openSansRegular);
        txtMaxNotificationDescription.setTypeface(openSansLight);
        txtNotificationsPerDay.setTypeface(openSansRegular);
       // txtDownloadArticleImages.setTypeface(openSansRegular);
       // txtArticleReaderMode.setTypeface(openSansRegular);
       // txtArticleReaderModeDescription.setTypeface(openSansLight);
        txtAccountInformation.setTypeface(openSansRegular);
        txtNetworkStatus.setTypeface(openSansRegular);
        txtNetworkStatusGreen.setTypeface(openSansRegular);
        txtAbout.setTypeface(openSansRegular);
        txtVersion.setTypeface(openSansRegular);
        txtVersionNumber.setTypeface(openSansRegular);
        btnSendTestNotification.setTypeface(openSansRegular);
        btnPrivacy.setTypeface(openSansRegular);
        btnTerms.setTypeface(openSansRegular);
        btnRateOnAppStore.setTypeface(openSansRegular);
        btnSendFeedback.setTypeface(openSansRegular);
        //RadioBtnNever.setTypeface(openSansRegular);
        //RadioBtnWifiOnly.setTypeface(openSansRegular);
        //RadioBtnAlways.setTypeface(openSansRegular);
        seekBarMaxNotificationsPerDay = (SeekBar) view.findViewById(R.id.seekBarMaxNotificationsPerDay);
        txtSeekBarValue = (TextView) view.findViewById(R.id.txtSeekBarValue);

      //  swtchArticleReaderMode = (Switch) view.findViewById(R.id.btnArticleReaderMode);


        //RADIO BUTTONS
        //need to change server value from 1-3 to 0-2 instead in order for this to work
                /*  userRadioButtonValue = settings.getArticleDisplayType();
  serverRadioButtonValue = settings.getArticleDisplayType();

        if (serverRadioButtonValue == 0)
        {
            RadioBtnNever.setChecked(true);
        }
        else if (serverRadioButtonValue == 1)
        {
            RadioBtnWifiOnly.setChecked(true);
        }
        else if(serverRadioButtonValue == 2)
        {
            RadioBtnAlways.setChecked(true);
        }



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (RadioBtnNever.isChecked())
                {
                    userRadioButtonValue = 0;
                }
                else if (RadioBtnWifiOnly.isChecked())
                {
                    userRadioButtonValue = 1;
                }
                else if (RadioBtnAlways.isChecked())
                {
                    userRadioButtonValue = 2;
                }
            }
        });


        // ARTICLE READER MODE SWITCH
        truth = settings.isArticleReaderMode();
        swtchArticleReaderMode.setChecked(truth);
        swtchArticleReaderMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {

                swtchCounter++;

            }
        });
*/
        //NETWORK STATUS
        getNetworkStatus();
        rowNetworkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus++;
                if(checkStatus > 3)
                {
                    checkStatus = 0;
                    getNetworkStatus();
                }
            }
        });

        // VERSION NUMBER
        txtVersionNumber.setText(getVersionNumber());

        //SEEKBAR FOR MAX NOTIFICATIONS
        seekBarMaxNotificationsPerDay.setMax(20);
        getMaxNotifications = settings.getMaxNotificaitons();
        seekBarMaxNotificationsPerDay.setProgress(getMaxNotifications); //set the progress on the maxnotifications to match db
        if (getMaxNotifications != 20) {
            txtSeekBarValue.setText(seekBarMaxNotificationsPerDay.getProgress() + " per day"); //set the text on the seekbar to match db
        }
        else  {
            txtSeekBarValue.setText("No Limit");
        }
        seekBarMaxNotificationsPerDay.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //seekbarValueFromUser = progress;
                        isDirtyLocal = true;
                        isDirtyServer = true;
                        if (progress != 20) {
                            txtSeekBarValue.setText(progress + " per day");
                        } else
                            txtSeekBarValue.setText("No Limit");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        settings.setMaxNotificaitons(seekBar.getProgress());
                        business.saveUserSettingsLocal(settings);
                        business.saveUserSettingsServer(settings,null);
                    }
                }
        );

        // RATE ON GOOGLE PLAY
        btnRateOnAppStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateOnAppStore();
            }
        });

        // Privacy
        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Article art = new Article();
                art.setUrl("http://www.notifyrapp.com/privacy.html");
                mWebViewFragment = new WebViewFragment().newInstance(art);
                fragmentTransaction.add(R.id.fragment_container, mWebViewFragment, "webview_frag");
                fragmentTransaction.addToBackStack("settings_frag");
                fragmentTransaction.commit();
            }
        });

        // Terms
        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Article art = new Article();
                art.setUrl("http://www.notifyrapp.com/terms.html");
                mWebViewFragment = new WebViewFragment().newInstance(art);
                fragmentTransaction.add(R.id.fragment_container, mWebViewFragment, "webview_frag");
                fragmentTransaction.addToBackStack("settings_frag");
                fragmentTransaction.commit();
            }
        });

        // SEND TEST
        btnSendTestNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Business business = new Business(ctx);
                business.sendTestNotification(new CallbackInterface() {
                    @Override
                    public void onCompleted(Object data) {

                    }
                });
                Toast.makeText(ctx, "Sent!", Toast.LENGTH_SHORT).show();
            }
        });

        // Send Feedback
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"feedback@notifyrapp.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Notifyr Android App Feedback");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            }
        });

        return view;
    }


    private void getNetworkStatus()
    {
        pbNetworkStatus.setVisibility(View.VISIBLE);
        txtNetworkStatusGreen.setVisibility(View.GONE);
        business.getNetworkStatus(new CallbackInterface() {
            @Override
            public void onCompleted(Object data) {
                if(isAdded()) {
                    String status = (String) data;
                    if (status.equals("true")) {
                        txtNetworkStatusGreen.setTextColor(getResources().getColor(R.color.switchGreen));
                        txtNetworkStatusGreen.setText("Online");
                    } else {
                        txtNetworkStatusGreen.setTextColor(getResources().getColor(R.color.switchRed));
                        txtNetworkStatusGreen.setText("Offline");
                    }
                    txtNetworkStatusGreen.setVisibility(View.VISIBLE);
                    pbNetworkStatus.setVisibility(View.GONE);
                }
            }
        });
    }

    private String getVersionNumber()
    {
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void rateOnAppStore()
    {
        Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + ctx.getPackageName())));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //business.saveusersetting()
        //UserSetting newUserSetting;
        //business.saveusersetting
        // ARTICLE READER MODE SWITCH
        if (swtchCounter%2 != 0) //checks if switch state is the same as when created
        {
            articleReaderMode = (truth) ? 0:1;
            settings.setArticleReaderMode(!(truth));
            business.saveUserSettingsLocal(settings);
            //saveLocal(); (save the opposite of the original boolean value in the server which is = articleReaderMode)
        }
        //SEEKBAR FOR MAX NOTIFICATIONS
        settings.setMaxNotificaitons(seekBarMaxNotificationsPerDay.getProgress());
        if (isDirtyLocal == true && isDirtyServer == true) //if original value from server is different from userseekbarvalue
        {
            business.saveUserSettingsLocal(settings);
            //saveLocal(); (save the seekbarValueFromUser to the local and server)
            business.saveUserSettingsServer(settings,null);
        }
        //ARTICLE DISPLAY TYPE (RADIOGROUP)
        settings.setArticleDisplayType(userRadioButtonValue);
        if (userRadioButtonValue != serverRadioButtonValue)
        {
            business.saveUserSettingsLocal(settings);
            //saveLocal()  (save the userRadioButtonValue in the local db)
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}