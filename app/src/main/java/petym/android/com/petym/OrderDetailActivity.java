package petym.android.com.petym;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import petym.android.com.petym.DataDownload.GetDateItemPic;
import petym.android.com.petym.DataDownload.GetMemberPetResturant;
import petym.android.com.petym.Tools.AlertDialogFragment;
import petym.android.com.petym.VO.DateItemVO;
import petym.android.com.petym.VO.MemberPetResturant;
import petym.android.com.petym.VO.Restaurant;
import petym.android.com.petym.aLogin.Common;

public class OrderDetailActivity extends AppCompatActivity {
    DateItemVO dateItemVO;
    @BindView(R.id.imageView)
    CircleImageView imageView;
    @BindView(R.id.dateItemNo)
    TextView dateItemNo;
    @BindView(R.id.dateItemPrice)
    TextView dateItemPrice;
    @BindView(R.id.sellerNo)
    TextView sellerNo;
    @BindView(R.id.dateItemTitle)
    TextView dateItemTitle;
    @BindView(R.id.dateMeetingTime)
    TextView dateMeetingTime;
    @BindView(R.id.petNo)
    TextView petNo;
    @BindView(R.id.restListNo)
    TextView restListNo;
    @BindView(R.id.hasMate)
    TextView hasMate;
    @BindView(R.id.dateItemLocate)
    TextView dateItemLocate;
    @BindView(R.id.buttonQRcode)
    Button buttonQRcode;
    @BindView(R.id.buttonbuy)
    Button buttonbuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        ButterKnife.bind(this);
        Restaurant restaurant;
        MemberPetResturant memberPetResturant = new MemberPetResturant();
        dateItemVO = (DateItemVO) getIntent().getExtras().getSerializable("dateItemVO");
        Log.d("dateItemVO", "dateItemVO:" + dateItemVO.getHasMate().toString());
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        int id = dateItemVO.getDateItemNo();
        int restId = dateItemVO.getRestListNo();
        int petId = dateItemVO.getPetNo();
        int sellerId = dateItemVO.getSellerNo();
//        Member member = new Member();
//        Pet pet = new Pet();
//        Restaurant restaurant = new Restaurant();
//        MemberPetResturant memberPetResturant = new MemberPetResturant(member,pet,restaurant);
        int imageSize = 2000;
        if (networkConnected()) {
            try {
                new GetDateItemPic(imageView, dateItemVO).execute(Common.URL + "MainActivity", id, imageSize);
//                restaurant =new GetReatuarent(this).execute(Common.URL+ "Restaurant",restId).get();
                memberPetResturant = new GetMemberPetResturant(this, "memberPetResturant").execute(Common.URL + "MemPetRestServer", sellerId, petId, restId).get();
                Log.d("QQ", "jsonOutQQ: " + memberPetResturant.getRestaurant().getRestName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "cool", Toast.LENGTH_SHORT);
        }

        dateItemLocate.setText("地點:" + dateItemVO.getDateItemLocate().toString());
        dateItemNo.setText("產品編號:" + dateItemVO.getDateItemNo().toString());
        hasMate.setText("商家攜帶人數:" + hasMate());


        dateItemPrice.setText("價錢:" + dateItemVO.getDateItemPrice().toString());
        dateItemTitle.setText("商品標題:" + dateItemVO.getDateItemTitle().toString());
        dateMeetingTime.setText("約會時間:" + dateItemVO.getDateMeetingTime().toString());
        restListNo.setText("約會餐廳:" + memberPetResturant.getRestaurant().getRestName());
        petNo.setText("寵物名稱:" + memberPetResturant.getPet().getPetName().toString());


    }


    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private String hasMate() {
        if (dateItemVO.getHasMate()) {
            return "有攜帶";
        } else {
            return "無攜帶";
        }
    }


    @OnClick({R.id.buttonQRcode, R.id.buttonbuy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonQRcode:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                View mView = getLayoutInflater().inflate(R.layout.buydialog, null);

                break;
            case R.id.buttonbuy:
                AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                alertDialogFragment.show(fragmentManager, "alert");
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.ButtonYesBuy)
        Button ButtonYesBuy;
        @BindView(R.id.ButtonCancelBuy)
        Button ButtonCancelBuy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
