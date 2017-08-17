package petym.android.com.petym.BottomEvent;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import petym.android.com.petym.DataDownload.GetMemPet;
import petym.android.com.petym.R;
import petym.android.com.petym.VO.MemberPet;
import petym.android.com.petym.aLogin.Common;

/**
 * Created by k3nt on 2017/8/1.
 */

public class Personal extends Fragment {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.memName)
    TextView memName;
    @BindView(R.id.memBday)
    TextView memBday;
    @BindView(R.id.editMem)
    Button editMem;
    @BindView(R.id.petKind)
    TextView petKind;
    @BindView(R.id.constellation)
    TextView constellation;
    @BindView(R.id.memPoint)
    TextView memPoint;
    @BindView(R.id.memSelfintro)
    TextView memSelfintro;
    Unbinder unbinder;
    MemberPet memberPet = new MemberPet();
    Calendar cal =  Calendar.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Personal() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        int memberId = 5001;

        if (networkConnected()) {
            try {
                memberPet =new GetMemPet(this.getActivity(),"memberPet").execute(Common.URL+ "MemPetRestServer",memberId).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this.getContext(), "cool", Toast.LENGTH_SHORT);
        }
        if((memberPet.getMember()!=null)){
            //------------------------------------------------------------
            Log.d("MPR",memberPet.getMember().getMemAddress());
            cal.setTime(memberPet.getMember().getMemBday());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            month = month +1; //month 從0開始
            year = year - 1911;
            memName.setText(""+memberPet.getMember().getMemName().toString());
            memPoint.setText(memberPet.getMember().getMemPoint().toString());
            memSelfintro.setText(memberPet.getMember().getMemSelfintro().toString());
            memBday.setText(String.valueOf(year));

        }
        if(memberPet.getPet()!=null) {
            Log.d("MPR", memberPet.getPet().toString());
            petKind.setText(memberPet.getPet().getPetKind().toString());
        }



        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.editMem)
    public void onViewClicked() {
    }
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
