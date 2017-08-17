package petym.android.com.petym.BottomEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import petym.android.com.petym.DataDownload.GetOrder;
import petym.android.com.petym.DataDownload.GetOrderAll;
import petym.android.com.petym.DataDownload.OrderGetImageTask;
import petym.android.com.petym.R;
import petym.android.com.petym.VO.DateItemVO;
import petym.android.com.petym.VO.Member;
import petym.android.com.petym.VO.OrderVO;
import petym.android.com.petym.VO.OrderVOAll;
import petym.android.com.petym.VO.Pet;
import petym.android.com.petym.VO.Restaurant;
import petym.android.com.petym.aLogin.Common;

/**
 * Created by k3nt on 2017/8/1.
 */

public class Order extends Fragment  {
    private OrderVO orderVO;
    private OrderVOAll orderAll;
    RadioButton radioSelection;
    private final static String TAG = "GetOrder";
    private RecyclerView orderListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    int memNo= 5004;
    String selectButton="0";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public Order(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        OrderVOAll orderAll = new OrderVOAll();
        try {
            orderAll =new GetOrderAll(getActivity(),"all").execute(Common.URL+"Order",memNo).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(R.layout.order, container, false);
        orderListView = (RecyclerView) view.findViewById(R.id.orderRecycleView);
        orderListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if("0".equals(selectButton)) {
                    showAllOrder();
                }else if("1".equals(selectButton)){
                    showAnotherOrder();
                }else {
                    showAnotherOrder();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        RadioGroup select = (RadioGroup) view.findViewById(R.id.selectGroup);
        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioSelection=(RadioButton)group.findViewById(checkedId);
                selectButton=radioSelection.getHint().toString();
                if ("0".equals(selectButton)){
                    showAllOrder();
                    Toast.makeText(getContext(),"全部",Toast.LENGTH_SHORT);
                }else if("1".equals(selectButton)){
                    selectButton=radioSelection.getHint().toString();
                    showAnotherOrder();
                    Toast.makeText(getContext(),"購買",Toast.LENGTH_SHORT);
                }else {
                    showAnotherOrder();
                    Toast.makeText(getContext(),"販賣",Toast.LENGTH_SHORT);
                }
            }
        });



        return view;
    }
    private void showAllOrder() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "Order";
            orderAll = new OrderVOAll();
            try {
                orderAll = new GetOrderAll(getActivity(),"all").execute(url,memNo).get();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (orderAll == null ) {
                Common.showToast(getActivity(), R.string.msg_NoOrderFound);
            } else {
                orderListView.setAdapter(new OrderRecyclerViewAdapter(getActivity(), orderAll));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    private void showAnotherOrder() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "Order";
           orderVO = new OrderVO();
            try {
                orderVO = new GetOrder(getActivity(),"notAll").execute(url,memNo).get();
                Log.d("orderVO","orderVO :"+orderVO.toString().length());
                Log.d("orderVO","orderVO :"+orderVO.getBuyerRestuarent().get(0).getRestName());
                Log.d("orderVO","orderVO :"+"0".equals(selectButton));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (orderVO == null ) {
                Common.showToast(getActivity(), R.string.msg_NoOrderFound);
            } else {
                orderListView.setAdapter(new OrderRecyclerViewAdapter(getActivity(), orderVO));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    @Override
    //show下拉動畫，onstart從新抓新資料
    public void onStart() {
        super.onStart();
        showAllOrder();
//        if("0".equals(all)) {
//            showAllOrder();
//        }else if(radioSelection.getText().toString().equals("購買")){
//            showAnotherOrder();
//        }else {
//            showAnotherOrder();
//        }
    }

    private class OrderRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private LayoutInflater layoutInflater;
        private OrderVO orderVO;
        private OrderVOAll orderVOAll;
        public OrderRecyclerViewAdapter(Context context, OrderVO orderVO) {
            layoutInflater = LayoutInflater.from(context);
            this.orderVO = orderVO;
        }
        public OrderRecyclerViewAdapter(Context context, OrderVOAll orderVOAll) {
            layoutInflater = LayoutInflater.from(context);
            this.orderVOAll = orderVOAll;
        }
        @Override
        public int getItemCount() {
            int forSize = 0;

            if("0".equals(selectButton)){
                forSize=orderVOAll.getAll().size();
            }else if("1".equals(selectButton)){
                forSize=orderVO.getMemberisBuyer().size();
            }else {
                forSize=orderVO.getMemberisSeller().size();
            }
            return forSize;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.order_item, parent, false);
            return new MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
            final DateItemVO dateItemVO;
            final Member buyerMember;
            final Member sellerMember;
            final Pet pet;
            final Restaurant restaurant;
            if("0".equals(selectButton)){
                 dateItemVO = orderVOAll.getAll().get(position);
                 buyerMember = orderVOAll.getBuyerInfo().get(position);
                 sellerMember = orderVOAll.getSellerInfo().get(position);
                 pet = orderVOAll.getSellerPetO().get(position);
                 restaurant =orderVOAll.getBuyerRestuarent().get(position);
            }else if("1".equals(selectButton)){
                 dateItemVO = orderVO.getMemberisBuyer().get(position);
                //買家是登入資訊取得
                buyerMember = null;
                sellerMember = orderVO.getSellerInfo().get(position);
                pet = orderVO.getSellerPet().get(position);
                restaurant = orderVO.getSellerRestuarent().get(position);

            }else {
                dateItemVO = orderVO.getMemberisBuyer().get(position);
                buyerMember = orderVO.getBuyerInfo().get(position);
                //賣家是登入資訊取得
                sellerMember = null;
                pet = orderVO.getSellerPetO().get(position);
                restaurant = orderVO.getBuyerRestuarent().get(position);
            }

            String url = Common.URL + "orderImage";
            int id = dateItemVO.getDateItemNo();
            int imageSize = 250;
//            new OrderGetImageTask(myViewHolder.imageView).execute(url, id, imageSize);
            if (buyerMember==null){
                myViewHolder.tvName.setText("購買人: "+"登入的人");
            }else {
                myViewHolder.tvName.setText("購買人: "+buyerMember.getMemName().toString());
            }
            if (sellerMember==null){
                myViewHolder.tvPhoneNo.setText("賣家: "+"登入的人");
            }else {
                myViewHolder.tvPhoneNo.setText("賣家: "+sellerMember.getMemName().toString());
            }

            myViewHolder.tvAddress.setText(dateItemVO.getDateItemTitle().toString());
            myViewHolder.tvAddress1.setText(pet.getPetName().toString());
            myViewHolder.tvAddress2.setText(restaurant.getRestName().toString());

//            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Fragment fragment = new SpotDetailFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("spot", spot);
//                    fragment.setArguments(bundle);
//                    switchFragment(fragment);
//                }
//            });
//            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);
//                    popupMenu.inflate(R.menu.popup_menu);
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            switch (item.getItemId()) {
//                                case R.id.update:
//                                    Intent updateIntent = new Intent(getActivity(),
//                                            SpotUpdateActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("spot", spot);
//                                    updateIntent.putExtras(bundle);
//                                    startActivity(updateIntent);
//                                    break;
//                                case R.id.delete:
//                                    if (Common.networkConnected(getActivity())) {
//                                        String url = Common.URL + "SpotServlet";
//                                        String action = "spotDelete";
//                                        int count = 0;
//                                        try {
//                                            count = new SpotUpdateTask().execute(url, action, spot, null).get();
//                                        } catch (Exception e) {
//                                            Log.e(TAG, e.toString());
//                                        }
//                                        if (count == 0) {
//                                            Common.showToast(getActivity(), R.string.msg_DeleteFail);
//                                        } else {
//                                            spots.remove(spot);
//                                            SpotsRecyclerViewAdapter.this.notifyDataSetChanged();
//                                            Common.showToast(getActivity(), R.string.msg_DeleteSuccess);
//                                        }
//                                    } else {
//                                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
//                                    }
//                            }
//                            return true;
//                        }
//                    });
//                    popupMenu.show();
//                    return true;
//                }
//            });

        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName, tvPhoneNo, tvAddress,tvAddress1,tvAddress2;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivSpot);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPhoneNo = (TextView) itemView.findViewById(R.id.tvPhoneNo);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvAddress1 = (TextView) itemView.findViewById(R.id.tvAddress1);
            tvAddress2= (TextView) itemView.findViewById(R.id.tvAddress2);
        }
    }
}
