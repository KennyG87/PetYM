package petym.android.com.petym.aLogin;

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
import android.text.format.DateFormat;
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
import java.util.regex.Matcher;

import petym.android.com.petym.VO.Member;
import petym.android.com.petym.R;

/**
 * Created by k3nt on 2017/7/17.
 */

public class RegisterActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_TAKE_PICTURE =0 ;
    private byte[] memImg;
    private Bitmap picture;
    private EditText memId, memPwd, memName, memSname, memIdNo, memPhone, memAddress, memEmail, memSelfintro;
    private EditText otherType, petName, petSpecies, petIntro, petBday, membday111;
    private String user_memID, user_memPwd, user_memName, user_memIdNo, user_memAddress, user_memPhone, user_memEmail, user_memSname, user_memSelfintro;
    private Integer user_memGender = 0, user_memRelation = 0, user_mempetornot = 0;
    private TextView memBday,TextView123;
    private RadioGroup memGender, petornot, memRelation,petKind,petGender;
    private Button btFinishInsert, btCancel, bdaybutton;
    private RadioButton RadioButtonGender, RadioButtonRelation, RadioButtonpetornot;
    private ImageView ivMemImg;
    private Matcher matcher;
    private int mYear, mMonth, mDay;
    private File file;
    private Boolean flag;
    private Member member;
    private Date user_memBday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mem_regist);
        findViewById();
        showNow();

        btFinishInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String memBirthday = new StringBuilder().append(mYear).append("-")
                        .append(pad(mMonth + 1)).append("-").append(pad(mDay)).toString();
                user_memBday = Date.valueOf(memBirthday);
                user_memID = memId.getText().toString().trim();
                user_memPwd = memPwd.getText().toString().trim();
                user_memName = memName.getText().toString().trim();
                user_memIdNo = memIdNo.getText().toString().trim();
                user_memAddress = memAddress.getText().toString().trim();
                user_memPhone = memPhone.getText().toString().trim();
                user_memEmail = memEmail.getText().toString().trim();
                user_memSname = memSname.getText().toString().trim();
                user_memSelfintro = memSelfintro.getText().toString().trim();
                chickMethod();
                Calendar mCal = Calendar.getInstance();
                CharSequence s = DateFormat.format( "yyyy-MM-dd kk:mm:ss" , mCal.getTime());
                java.sql.Timestamp s1 = java.sql.Timestamp.valueOf(s.toString());

                    member = new Member(0,user_memID,user_memPwd,user_memName,user_memSname,user_memGender,
                            user_memIdNo,user_memBday,user_memPhone,user_memAddress,user_memEmail,memImg,0,0,user_memRelation,
                            user_memSelfintro,0,0,0,0.0,0.0,s1,0);
                if (networkConnected()) {
                    try {
                        String cool= new RegisterActivity.RegisterTask1().execute(Common.URL+"MemberRegister").get();
                        System.out.print(cool);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    Common.showToast(v.getContext(), R.string.msg_NoNetwork);
                }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("member",member);
                    Intent intent = new Intent(v.getContext(),RegisterActivityNext.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

            private boolean networkConnected() {
                ConnectivityManager conManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }




            private void chickMethod() {
                flag = false;
                ArrayList<Boolean> valids = new ArrayList(Arrays.asList(false,false,false,false,false,false,false,false,false,true));
                Log.d("flag","flag::::::"+valids.contains(false));
                //圖片驗證
//                if(picture==null){
//                    TextView123.setError("照片不能空白");
//                    valids.add(9,false);
//                    return;}
                //帳號驗證
                if (!valids.get(0)){
                    String enameReg = "^[(a-zA-Z0-9_)]{3,10}$";
                    if (user_memID == null || user_memID.length() == 0) {
                        memId.setError("帳號: 請勿空白");
                        return;
                    } else if(!user_memID.trim().matches(enameReg)) {
                        memId.setError("帳號: 英文字母、數字和_ , 且長度必需在3到10之間");
                        return;
                    }
                    valids.add(0,true);
                }
                //密碼驗證
                if (!valids.get(1)){
                    String enameReg = "^[(a-zA-Z0-9)]{6,15}$";
                    if (user_memPwd == null || user_memPwd.length() == 0) {
                        memPwd.setError("密碼: 請勿空白");
                        return;
                    } else if(!user_memPwd.trim().matches(enameReg)) {
                        memPwd.setError("密碼: 只能是英文字母、數字 , 且長度必需在6到15之間");
                        return;
                    }
                    valids.add(1,true);
                }
                //名字驗證
                if (!valids.get(2)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)]{2,15}$";
                    if (user_memName == null || user_memName.length() == 0) {
                        memName.setError("姓名: 請勿空白");
                        return;
                    } else if(!user_memName.trim().matches(enameReg)) {
                        memName.setError("姓名: 只能是中文、英文字母、數字 , 且長度必需在2到15之間");
                        return;
                    }
                    valids.add(2,true);

                }
                //暱稱驗證
                if (!valids.get(3)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)]{2,15}$";
                    if (user_memSname == null || user_memSname.length() == 0) {
                        memSname.setError("暱稱: 請勿空白");
                        return;
                    } else if(!user_memSname.trim().matches(enameReg)) {
                        memSname.setError("暱稱: 只能是中文、英文字母、數字 , 且長度必需在2到15之間");
                        return;
                    }
                    valids.add(3,true);
                }
                //身分證驗證
                if (!valids.get(4)){
                    PID(user_memIdNo,valids);
                }
                //電話驗證
                if (!valids.get(5)){
                    String enameReg = "^[(0-9)]{10}$";
                    if (user_memPhone == null || user_memPhone.length() == 0) {
                        memPhone.setError("電話: 請勿空白");
                        return;
                    } else if(!user_memPhone.trim().matches(enameReg)) {
                        memPhone.setError("手機號碼: 只能數字 , 且長度為10");
                        return;
                    }
                    valids.add(5,true);
                }
                //地址驗證
                if (!valids.get(6)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9-)]{2,50}$";
                    if (user_memAddress == null || user_memAddress.length() == 0) {
                        memAddress.setError("地址: 請勿空白");
                        return;
                    } else if(!user_memAddress.trim().matches(enameReg)) {
                        memAddress.setError("地址: 只能是中文、英文字母、數字 , 且長度必需在2到50之間");
                        return;
                    }
                    valids.add(6,true);
                }
                //email驗證
                if (!valids.get(7)){
                    String enameReg = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
                    if (user_memEmail == null || user_memEmail.length() == 0) {
                        memEmail.setError("Email: 請勿空白");
                        return;
                    } else if(!user_memEmail.trim().matches(enameReg)) {
                        memEmail.setError("Email: 格式錯誤");
                        return;
                    }
                    valids.add(7,true);
                }
                //自介驗證
                if (!valids.get(8)){
                    String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)(,.;')]{10,300}$";
                    if (user_memSelfintro == null || user_memSelfintro.length() == 0) {
                        memSelfintro.setError("自介: 請勿空白");
                        return;
                    } else if(!user_memSelfintro.trim().matches(enameReg)) {
                        memSelfintro.setError("自介: 長度必需在2到300之間");
                        return;
                    }
                    valids.add(8,true);

                }

                    flag=true;


                Log.d("flag","flag:"+flag);

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

                    ivMemImg.setImageBitmap(picture);
                    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out1);
                    memImg = out1.toByteArray();
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
                        ivMemImg.setImageBitmap(bitmap);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        memImg = out2.toByteArray();
                    }
                    break;
            }
        }
    }


    public void setAllfill(View view) {
        memId.setText("haappyy");
        memPwd.setText("123456");
        memName.setText("梁成興");
        memSname.setText("補教名師");
        memIdNo.setText("A165265055");
        memPhone.setText("0912345672");
        memAddress.setText("Myhome");
        memEmail.setText("haappyy@gmail.com");
        memSelfintro.setText("我叫梁陳星,我會教國文");


    }


    private void findViewById() {
        //btfinichInerst要寫在最後
        btFinishInsert = (Button) findViewById(R.id.btRegisterNextStep);
        ivMemImg = (ImageView) findViewById(R.id.ivMemImg);

        memId = (EditText) findViewById(R.id.memId);
        memPwd = (EditText) findViewById(R.id.memPwd);
        memName = (EditText) findViewById(R.id.memName);
        memSname = (EditText) findViewById(R.id.memSname);
        memIdNo = (EditText) findViewById(R.id.memIdNo);
        memBday = (TextView) findViewById(R.id.memBday);
        memPhone = (EditText) findViewById(R.id.memPhone);
        memAddress = (EditText) findViewById(R.id.memAddress);
        memEmail = (EditText) findViewById(R.id.memEmail);
        memSelfintro = (EditText) findViewById(R.id.memSelfintro);
        TextView123 = (TextView) findViewById(R.id.TextView123);

        memGender = (RadioGroup) findViewById(R.id.memGender);
        memRelation = (RadioGroup) findViewById(R.id.memRelation);
        petornot = (RadioGroup) findViewById(R.id.petornot);
        memGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButtonGender = (RadioButton) group.findViewById(checkedId);
                Common.showToast(getApplicationContext(), (String) RadioButtonGender.getText());
                user_memGender = Integer.valueOf((String) RadioButtonGender.getHint());
            }
        });
        memRelation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButtonRelation = (RadioButton) group.findViewById(checkedId);
                Common.showToast(getApplicationContext(), (String) RadioButtonRelation.getHint());
                user_memRelation = Integer.valueOf((String) RadioButtonRelation.getHint());

            }
        });
        petornot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButtonpetornot = (RadioButton) group.findViewById(checkedId);
                Common.showToast(getApplicationContext(), (String) RadioButtonpetornot.getText());
                user_mempetornot = Integer.valueOf((String) RadioButtonpetornot.getHint());
                if(RadioButtonpetornot.getHint().equals("1")){
                    btFinishInsert.setText("下一步");
                }else {
                    btFinishInsert.setText("註冊");
                }
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
        memBday.setText(new StringBuilder().append(mYear).append("-")
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
            RegisterActivity activity = (RegisterActivity) getActivity();
            return new DatePickerDialog(
                    activity, activity, activity.mYear, activity.mMonth, activity.mDay);
        }
    }

    public void onDateClick(View view) {
        DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        datePickerFragment.show(fm, "datePicker");
    }
    public void PID(String id,ArrayList<Boolean> valids){
        int[] num=new int[10];
        int[] rdd={10,11,12,13,14,15,16,17,34,18,19,20,21,22,35,23,24,25,26,27,28,29,32,30,31,33};
        id=id.toUpperCase();
        if(!((id.trim().length()) ==10)){
            memIdNo.setError("身分證字號總長度為10!!");return;
        }
        if(id.charAt(0)<'A'||id.charAt(0)>'Z'){
            memIdNo.setError("第一個字錯誤!!");return;
        }
        if(id.charAt(1)!='1' && id.charAt(1)!='2'){
            memIdNo.setError("第二個字錯誤!!");return;
        }
        for(int i=1;i<10;i++){
            if(id.charAt(i)<'0'||id.charAt(i)>'9'){
                memIdNo.setError("輸入錯誤");return;
            }
        }
        for(int i=1;i<10;i++){
            num[i]=(id.charAt(i)-'0');
        }
        num[0]=rdd[id.charAt(0)-'A'];
        int sum=((int)num[0]/10+(num[0]%10)*9);
        for(int i=0;i<8;i++){
            sum+=num[i+1]*(8-i);
        }
        if(!(10-sum%10==num[9])) {
            memIdNo.setError("身分證號錯誤");}
        valids.add(4,true);
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


    private class RegisterTask1 extends AsyncTask<String, Integer, String> {

        private static final String TAG = "RegisterActivity";

        @Override
        protected String doInBackground(String... params) {
            String url =  params[0];
            String outStr="";
            String jsonIn;
//            Gson gson = new Gson();
            Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            outStr = gson.toJson(member);

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

