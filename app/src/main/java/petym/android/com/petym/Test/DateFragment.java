package petym.android.com.petym.Test;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import petym.android.com.petym.DataDownload.GetDateItemUpload;
import petym.android.com.petym.DataDownload.GetPetRestuarantNoItemUpload;
import petym.android.com.petym.DataDownload.GetRestuarantPetName;
import petym.android.com.petym.R;
import petym.android.com.petym.VO.DateItemVO;
import petym.android.com.petym.VO.GetRestuarantPetNameVO;
import petym.android.com.petym.VO.Member;
import petym.android.com.petym.aLogin.Common;

import static android.app.Activity.RESULT_OK;

public class DateFragment extends Fragment {
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.btTakePicture)
    Button btTakePicture;
    @BindView(R.id.btTakePicture1)
    Button btTakePicture1;
    @BindView(R.id.uploadDateItem)
    ImageView uploadDateItem;
    @BindView(R.id.itemtitle)
    EditText itemtitle;
    @BindView(R.id.textInputLayout1)
    TextInputLayout textInputLayout1;
    @BindView(R.id.itemprice)
    EditText itemprice;
    @BindView(R.id.textInputLayout2)
    TextInputLayout textInputLayout2;
    @BindView(R.id.textInputLayout3)
    TextView textInputLayout3;
    @BindView(R.id.textInputLayout4)
    TextView textInputLayout4;
    @BindView(R.id.textInputLayout5)
    TextView textInputLayout5;
    @BindView(R.id.textInputLayout6)
    TextView textInputLayout6;
    Unbinder unbinder;
    @BindView(R.id.uploadButton)
    Button uploadButton;
    @BindView(R.id.memSelfintro)
    EditText memSelfintro;

    private GetRestuarantPetNameVO getRestuarantPetNameVO = new GetRestuarantPetNameVO();
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_TAKE_PICTURE = 0;
    private final static String FILE_NAME1 = "member.txt";

    private ObjectInputStream ois = null;
    private Common common;
    private int mYear=1990, mMonth=11, mDay=31, mHour=12, mMinute=12;
    private File file;
    private Bitmap picture;
    private byte[] dateImg;
    private Activity activity;
    private Boolean hasMate,speed;
    private String dateMeetingTime;
    RadioButton radioSelection;
    private Integer selectButton = 0;
    String restuarantSelected, petSelected, buySelected, sellSelected;
    List<Integer> PetRestNoArrayList = new ArrayList<Integer>();

    public DateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //----------------------------------------------------------上傳按鈕+驗證
    @OnClick(R.id.uploadButton)
    public void onViewClicked() {
        String url = Common.URL + "GetPetRestuarantNoItemUpload";

        try {
            PetRestNoArrayList = new GetPetRestuarantNoItemUpload(getActivity(), "GetNo").execute(url, restuarantSelected, petSelected).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("PetRestNoArrayList::",PetRestNoArrayList.get(0).toString());
        Log.d("PetRestNoArrayList::",PetRestNoArrayList.get(1).toString());

        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format( "yyyy-MM-dd kk:mm:ss" , mCal.getTime());
        java.sql.Timestamp s1 = java.sql.Timestamp.valueOf(s.toString());

         dateMeetingTime = new StringBuilder().append(mYear).append("-")
                .append(pad(mMonth + 1)).append("-").append(pad(mDay))
                .append(" ").append(pad(mHour)).append(":")
                .append(pad(mMinute)).append(":").append("00").toString();
        Log.d("dateQQTime::",dateMeetingTime);
        java.sql.Timestamp user_dateMeetingTime =java.sql.Timestamp.valueOf(dateMeetingTime);

        if("有".equals(sellSelected)){
            hasMate=true;
        }else {
            hasMate=false;
        }
        if(selectButton.toString().equals("0")){
            speed = false;
        }else {
            speed = true;
        }
        DateItemVO dateItemVO = new DateItemVO(0,MainActivity_Test.member.getMemNo(), PetRestNoArrayList.get(0), itemtitle.getText().toString(), dateImg,
                memSelfintro.getText().toString(), s1, user_dateMeetingTime, "123", Integer.valueOf(buySelected),
               hasMate,Integer.valueOf(itemprice.getText().toString()), 0, 0, 0, 5001,
                false, 0, 0, speed, PetRestNoArrayList.get(1));
        Log.d("PetRestNoArrayList::",dateItemVO.toString());

        url = Common.URL + "GetDateItemUpload";

              new GetDateItemUpload(getActivity(), "GetDateItemUpload").execute(url,dateItemVO);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_dateitem, container, false);
        showList();
        Log.d("GetNameList", "GetNameList:" + MainActivity_Test.member.getMemNo());
        //restuarant List
        String[] restuarant = new String[getRestuarantPetNameVO.getRestuarant().size()];
        String[] pet = new String[getRestuarantPetNameVO.getPetName().size()];
        for (int i = 0; i < getRestuarantPetNameVO.getRestuarant().size(); i++) {
            restuarant[i] = getRestuarantPetNameVO.getRestuarant().get(i).toString();
        }
        for (int i = 0; i < getRestuarantPetNameVO.getPetName().size(); i++) {
            pet[i] = getRestuarantPetNameVO.getPetName().get(i).toString();
        }

        RadioGroup select = (RadioGroup) view.findViewById(R.id.selectDateGroup);
        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioSelection = (RadioButton) group.findViewById(checkedId);
                selectButton = Integer.valueOf(radioSelection.getHint().toString());
                Common.showToast(getContext(), selectButton.toString());
            }
        });
        Button setFakeData = (Button) view.findViewById(R.id.setFakeData);
        Button datebutton = (Button) view.findViewById(R.id.datebutton);

        Spinner spPet = (Spinner) view.findViewById(R.id.spPet);
        Spinner spRestuarant = (Spinner) view.findViewById(R.id.spRestuarant);
        Spinner spBuyerPeopleCt = (Spinner) view.findViewById(R.id.spBuyerPeopleCt);
        Spinner sellerPeopleCT = (Spinner) view.findViewById(R.id.sellerPeopleCT);
        //Rest
        ArrayAdapter<String> adapterRestuarant = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, restuarant);
        adapterRestuarant
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRestuarant.setAdapter(adapterRestuarant);
        spRestuarant.setSelection(0, true);
        restuarantSelected= spRestuarant.getItemAtPosition(0).toString();
        spRestuarant.setOnItemSelectedListener(Restuarantlistener);

        //Pet
        ArrayAdapter<String> adapterPet = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, pet);
        adapterPet
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPet.setAdapter(adapterPet);
        spPet.setSelection(0, true);
        petSelected=spPet.getItemAtPosition(0).toString();
        spPet.setOnItemSelectedListener(Petlistener);
        spBuyerPeopleCt.setSelection(0, true);
        buySelected=spBuyerPeopleCt.getItemAtPosition(0).toString();
        spBuyerPeopleCt.setOnItemSelectedListener(BuyerPeopleCtlistener);
        sellerPeopleCT.setSelection(0, true);
        sellSelected=sellerPeopleCT.getItemAtPosition(0).toString();
        sellerPeopleCT.setOnItemSelectedListener(sellerPeopleCTlistener);
        setFakeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemtitle.setText("來場激烈的約會吧");
                itemprice.setText("999999999");
                textView7.setText("2017-08-25 15:30:00");

            }
        });


//        ArrayAdapter<String> adapterbuypeople = new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_spinner_item, buypeople);
//        adapterbuypeople
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spBuyerPeopleCt.setAdapter(adapterbuypeople);

//
//
//        ArrayAdapter<String> adaptersellpeople = new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_spinner_item, buypeople);
//        adaptersellpeople
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spBuyerPeopleCt.setAdapter(adaptersellpeople);
//

        //DatePicker In fragment
        class DatePickerFragment extends DialogFragment
                implements DatePickerDialog.OnDateSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), this, year, month, day);
            }

            public void onDateSet(DatePicker view, int year, int month, int day) {
                mYear = year;
                mMonth = month;
                mDay = day;

                // Do something with the date chosen by the user
            }
        }
        //Timepicker In fragment
        class TimePickerFragment extends DialogFragment
                implements TimePickerDialog.OnTimeSetListener {


            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                return new TimePickerDialog(getActivity(), this, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Do something with the time chosen by the user
                mHour = hourOfDay;
                mMinute = minute;
                textView7.setText(new StringBuilder().append(mYear).append("-")
                        .append(pad(mMonth + 1)).append("-").append(pad(mDay))
                        .append(" ").append(pad(mHour)).append(":")
                        .append(pad(mMinute)));

            }
        }

        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
                DialogFragment newFragment1 = new DatePickerFragment();
                newFragment1.show(getActivity().getSupportFragmentManager(), "datePicker");

            }
        });


        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    Spinner.OnItemSelectedListener Restuarantlistener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(
                AdapterView<?> parent, View view, int pos, long id) {

            restuarantSelected = (parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    Spinner.OnItemSelectedListener Petlistener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(
                AdapterView<?> parent, View view, int pos, long id) {

            petSelected = (parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    Spinner.OnItemSelectedListener BuyerPeopleCtlistener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(
                AdapterView<?> parent, View view, int pos, long id) {
            buySelected = (parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    Spinner.OnItemSelectedListener sellerPeopleCTlistener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(
                AdapterView<?> parent, View view, int pos, long id) {
            sellSelected = (parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    //選擇圖片
    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    //拍照與儲存路徑
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定存檔路徑，差在哪裡
//        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        // targeting Android 7.0 (API level 24) and higher,
        // storing images using a FileProvider.
        // passing a file:// URI across a package boundary causes a FileUriExposedException.
        Uri contentUri = FileProvider.getUriForFile(
                getContext(), getActivity().getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        if (isIntentAvailable(getContext(), intent)) {
            startActivityForResult(intent, REQ_TAKE_PICTURE);
        } else {
            Common.showToast(getContext(), R.string.msg_NoCameraApp);
        }
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    //照片顯示在圖片上
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    Bitmap srcPicture = BitmapFactory.decodeFile(file.getPath());
                    picture = Common.downSize(srcPicture, 512);

                    uploadDateItem.setImageBitmap(picture);
                    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out1);
                    dateImg = out1.toByteArray();
                    break;
                case REQUEST_PICK_IMAGE:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        uploadDateItem.setImageBitmap(bitmap);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        dateImg = out2.toByteArray();
                    }
                    break;
            }
        }
    }


    void showList() {
        if (networkConnected()) {
            try {
                getRestuarantPetNameVO = new GetRestuarantPetName()
                        .execute(Common.URL + "GetRestPetName", MainActivity_Test.member.getMemNo()).get();
//                Log.d("GetNameList","GetNameList"+getRestuarantPetNameVO.getRestuarant().get(0).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "cool", Toast.LENGTH_SHORT);
        }
    }

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String pad(int number) {
        if (number >= 10)
            return String.valueOf(number);
        else
            return "0" + String.valueOf(number);
    }


    @OnClick({R.id.btTakePicture, R.id.btTakePicture1,})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btTakePicture:
                onPickPictureClick(view);

                break;
            case R.id.btTakePicture1:
                takePicture();
                break;
        }
    }
}
