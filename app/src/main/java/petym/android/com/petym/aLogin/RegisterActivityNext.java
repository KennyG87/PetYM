package petym.android.com.petym.aLogin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import petym.android.com.petym.MainActivity;
import petym.android.com.petym.Test.MainActivity_Test;
import petym.android.com.petym.VO.Member;
import petym.android.com.petym.VO.Pet;
import petym.android.com.petym.R;

/**
 * Created by k3nt on 2017/7/17.
 */

public class RegisterActivityNext extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_TAKE_PICTURE =0 ;
    private byte[] petImg;
    private Bitmap picture;
    private ProgressDialog progressDialog;
    private EditText otherType, petName, petSpecies, petIntro, petBday;
    private String user_petName,user_otherType,user_petSpecies,user_petIntro,user_petKind="貓";
    private RadioGroup petKind,petGender;
    private TextView tvpetBday;
    private Button btRegisterFinish, btCancel, bdaybutton;
    private RadioButton RadioButtonpetKind,RadioButtonpetGender;
    private Integer  user_petGender = 0;
    private ImageView ivpetImg;
    private int mYear, mMonth, mDay;
    private Date user_memBday,user_petBday;
    private File file;
    private Boolean flagPet;
    private Member member;
    private Pet pet;


    private List<Object> all = new ArrayList<Object>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mem_regist_next);
        Bundle bundle = getIntent().getExtras();
        member = (Member) bundle.getSerializable("member");
        Log.d("member","member:"+ member.getMemId());
        Log.d("member","member:"+ member.getMemId());

        findViewById();
        showNow();



        btRegisterFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String petBday = new StringBuilder().append(mYear).append("-")
                        .append(pad(mMonth + 1)).append("-").append(pad(mDay)).toString();
                user_petBday = Date.valueOf(petBday);
                user_petName = petName.getText().toString().trim();
                user_otherType = otherType.getText().toString().trim();
                user_petSpecies = petSpecies.getText().toString().trim();
                user_petIntro = petIntro.getText().toString().trim();
                chickMethod();
                pet= new Pet(0,0,user_petName,user_petKind,user_petGender,user_petSpecies,user_petIntro,
                        user_petBday,petImg,0);
                Log.d("member","member:"+ pet.getPetName());


                if(flagPet){
                    if (networkConnected()) {
                        try {
                            String cool= new RegisterTask().execute(Common.URL+"PetRegister").get();
                            System.out.print(cool);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Common.showToast(v.getContext(), R.string.msg_NoNetwork);
                    }
                    Intent intent = new Intent(v.getContext(),MainActivity_Test.class);
                    startActivity(intent);

                }

            }

            private boolean networkConnected() {
                ConnectivityManager conManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }

            private void chickMethod() {
                flagPet=false;
                ArrayList<Boolean> valids = new ArrayList(Arrays.asList(false,false,false,false));
                //寵物名字驗證
                if (!valids.get(0)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)]{1,15}$";
                    if (user_petName == null || user_petName.length() == 0) {
                        petName.setError("寵物姓名: 請勿空白");
                        return;
                    } else if(!user_petName.trim().matches(enameReg)) {
                        petName.setError("寵物姓名: 只能是中文、英文字母、數字 , 且長度必需在1到15之間");
                        return;
                    }
                    valids.add(0,true);

                }
                //品種說明驗證
                if (!valids.get(1)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)]{2,15}$";
                    if (user_petSpecies == null || user_petSpecies.length() == 0) {
                        petSpecies.setError("寵物暱稱: 請勿空白");
                        return;
                    } else if(!user_petSpecies.trim().matches(enameReg)) {
                        petSpecies.setError("寵物暱稱: 只能是中文、英文字母、數字 , 且長度必需在2到15之間");
                        return;
                    }
                    valids.add(1,true);
                }

                //自介驗證
                if (!valids.get(2)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)(@!#$%^&*,.?(){}+_;)]{10,300}$";
                    if (user_petIntro == null || user_petIntro.length() == 0) {
                        petIntro.setError("寵物自介: 請勿空白");
                        return;
                    } else if(!user_petIntro.trim().matches(enameReg)) {
                        petIntro.setError("寵物自介: 長度必需在2到300之間");
                        return;
                    }
                    valids.add(2,true);

                }
                //品種種類驗證
                if (!valids.get(3)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)(@!#$%^&*,.?)]{10,300}$";
                    if (user_petIntro == null || user_petIntro.length() == 0) {
                        petIntro.setError("寵物自介: 請勿空白");
                        return;
                    } else if(!user_petIntro.trim().matches(enameReg)) {
                        petIntro.setError("寵物自介: 長度必需在2到300之間");
                        return;
                    }
                    valids.add(3,true);

                }
                flagPet=true;
            }

        });


    }


    //照片顯示在圖片上
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    Bitmap srcPicture = BitmapFactory.decodeFile(file.getPath());
                    picture = Common.downSize(srcPicture, 512);

                    ivpetImg.setImageBitmap(picture);
                    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out1);
                    petImg = out1.toByteArray();
                    break;
                case REQUEST_PICK_IMAGE:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivpetImg.setImageBitmap(bitmap);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        petImg = out2.toByteArray();
                    }
                    break;
            }
        }
    }


    public void allPetFill(View view) {
        petName.setText("狗");
        petSpecies.setText("馬爾祭司");
        petIntro.setText("梁成興與狗aaaaaaaaaaaaaaaa");

    }


    private void findViewById() {
        //btRegisterFinish要寫在最後
         btRegisterFinish = (Button) findViewById(R.id.btRegisterFinish);
         ivpetImg = (ImageView) findViewById(R.id.ivpetImg);

         petName = (EditText) findViewById(R.id.petName);
         petSpecies = (EditText) findViewById(R.id.petSpecies);
         petIntro = (EditText) findViewById(R.id.petIntro);
         otherType = (EditText) findViewById(R.id.otherType);
         petKind = (RadioGroup) findViewById(R.id.petKind);
         petGender = (RadioGroup) findViewById(R.id.petGender);
         tvpetBday = (TextView) findViewById(R.id.tvpetBday);
        petKind.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButtonpetKind = (RadioButton) group.findViewById(checkedId);
                Common.showToast(getApplicationContext(), (String) RadioButtonpetKind.getHint());
                user_petKind = RadioButtonpetKind.getText().toString();
                Log.d("user_petKind",user_petKind+"user_petKind");
//                //選其他有文字text出現
//                if(RadioButtonpetKind.getHint().equals("2")){
//                    otherType.setVisibility(VISIBLE);
//                }
            }
        });
        petGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButtonpetGender = (RadioButton) group.findViewById(checkedId);
                Common.showToast(getApplicationContext(), (String) RadioButtonpetGender.getHint());
                user_petGender = Integer.valueOf((String) RadioButtonpetGender.getHint());

            }
        });


//        bdaybutton = (Button) findViewById(R.id.bdaybutton);


    }
    //日期選單
    private void showNow() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDisplay();
    }

    private void updateDisplay() {
        tvpetBday.setText(new StringBuilder().append(mYear).append("-")
                .append(pad(mMonth + 1)).append("-").append(pad(mDay)));

    }

    private String pad(int number) {
        if (number >= 10)
            return String.valueOf(number);
        else
            return "0" + String.valueOf(number);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        updateDisplay();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }



    public static class DatePickerDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            RegisterActivityNext activity = (RegisterActivityNext) getActivity();
            return new DatePickerDialog(
                    activity, activity, activity.mYear, activity.mMonth, activity.mDay);
        }
    }

    public void onDateClick(View view) {
        DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        datePickerFragment.show(fm, "datePicker");
    }

    //選擇圖片
    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    //拍照與儲存路徑
    public void onTakePictureClick(View view) {
        takePicture();
    }
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
                this, getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQ_TAKE_PICTURE);
        } else {
            Common.showToast(this, R.string.msg_NoCameraApp);
        }
    }
    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private class RegisterTask extends AsyncTask<String, Object, String> {

        private static final String TAG = "RegisterActivity";

        @Override
        protected String doInBackground(String... params) {
            String url =  params[0];
            String outStr="";
            String jsonIn;
//           Gson gson = new Gson();
            Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            outStr = gson.toJson(pet);

            try {
                jsonIn = getRemoteData(url, outStr);
                Log.d(TAG, "jsonin=" + jsonIn);
            } catch (Exception e) {
                Log.e(TAG, e.toString());

                return null;
            }
            return null;
        }
        private String getRemoteData(String url, String jsonOut) throws IOException {
            StringBuilder jsonIn = new StringBuilder();
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                bw.write(jsonOut);
                Log.d(TAG, "jsonOut: " + jsonOut);
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    jsonIn.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
            connection.disconnect();
            Log.d(TAG, "jsonInqq: " + jsonIn);
            return jsonIn.toString();
        }
    }




//    // 點擊上傳按鈕
//    public void onUploadClick(View view) {
//        if (picture == null) {
//            Common.showToast(this, R.string.msg_NotUploadWithoutPicture);
//            return;
//        }
//        Intent loginIntent = new Intent(this, LoginDialogActivity.class);
//        startActivityForResult(loginIntent, REQ_LOGIN);
//    }


}

