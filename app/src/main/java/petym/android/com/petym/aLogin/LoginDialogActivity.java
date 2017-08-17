package petym.android.com.petym.aLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import petym.android.com.petym.BottomEvent.Order;
import petym.android.com.petym.DataDownload.GetMember;
import petym.android.com.petym.DataDownload.GetOrder;
import petym.android.com.petym.R;
import petym.android.com.petym.Test.MainActivity_Test;
import petym.android.com.petym.VO.Member;

// 此Activity將會以對話視窗模式顯示，呼叫setResult()設定回傳結果
public class LoginDialogActivity extends AppCompatActivity {
    private EditText etUser;
    private EditText etPassword;
    private TextView tvMessage;
    private Member member;
    private Context context;
    private final static String FILE_NAME1 = "member.txt";
    private ObjectInputStream ois = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViews();
        setResult(RESULT_CANCELED);
        context= getApplicationContext();
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                MODE_PRIVATE);
        pref.edit()
                .clear()
                .commit();

        //Result_cancel會丟下去
        //研究一下onstaryActivityResult，有回傳處理完的機制 ForResult!!!
    }

    private void findViews() {
        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        btLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String user = etUser.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (user.length() <= 0 || password.length() <= 0) {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                    return;
                }


                if (isUserValid(user, password)) {
                    //丟掉篇設定檔案裏面，利用key-Value，web端讀取偏好設定檔來確定已經登入帳號成功的資訊，包含帳號密碼
                    //仍需利用偏好設定進行驗證，怕不同手機驗證時已經修改密碼，仍需再次驗證
                    SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                            MODE_PRIVATE);
//                    pref.edit()
//                            .putBoolean("login", true)
//                            .putString("user", user)
//                            .putString("password", password)
//                            .apply();
                    setResult(RESULT_OK);


                    String path = context.getFilesDir().getPath();

                    try {
                        FileOutputStream file = openFileOutput(FILE_NAME1,Context.MODE_PRIVATE);
                        ObjectOutputStream oos = new ObjectOutputStream(file);
                        oos.writeObject(member);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(context,MainActivity_Test.class);
                    startActivity(intent);
                    finish();
                } else {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //重新檢查一次偏好設定檔去server端檢查，如果檢查ok的話就會finish掉 看不到登入畫面。
        //如果是True Logout才會顯示出來
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        if (login) {
            String name = pref.getString("user", "");
            String password = pref.getString("password", "");
            if (isUserValid(name, password)) {
                setResult(RESULT_OK);
                finish();
            } else {
                showMessage(R.string.msg_InvalidUserOrPassword);
            }
        }
    }

    private void showMessage(int msgResId) {
        tvMessage.setText(msgResId);
    }

    private boolean isUserValid(String name, String password) {
        // 應該連線至server端檢查帳號密碼是否正確
        String url = Common.URL + "Login";
        try {
            member = new GetMember(this,"login").execute(url,name,password).get();
            Log.d("orderVO","orderVO :"+member.toString().length());
            Log.d("orderVO","orderVO :"+member.getMemName().toString());

        } catch (Exception e) {
            Log.e("Login", e.toString());
        }

        if(member==null){
            return false;
        }else {
            return true;
        }

    }

}
