package petym.android.com.petym.VO;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by k3nt on 2017/8/3.
 */

public class OrderVO implements Serializable {

    ArrayList<DateItemVO> memberisBuyer;
    ArrayList<Member> sellerInfo;
    ArrayList<Pet> sellerPet;
    ArrayList<Restaurant> sellerRestuarent;

    ArrayList<DateItemVO> memberisSeller;
    ArrayList<Member> buyerInfo;
    ArrayList<Pet> sellerPetO;
    ArrayList<Restaurant> buyerRestuarent;
    public OrderVO(){}

    public ArrayList<DateItemVO> getMemberisBuyer() {
        return memberisBuyer;
    }

    public void setMemberisBuyer(ArrayList<DateItemVO> memberisBuyer) {
        this.memberisBuyer = memberisBuyer;
    }

    public ArrayList<Member> getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(ArrayList<Member> sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public ArrayList<Pet> getSellerPet() {
        return sellerPet;
    }

    public void setSellerPet(ArrayList<Pet> sellerPet) {
        this.sellerPet = sellerPet;
    }

    public ArrayList<Restaurant> getSellerRestuarent() {
        return sellerRestuarent;
    }

    public void setSellerRestuarent(ArrayList<Restaurant> sellerRestuarent) {
        this.sellerRestuarent = sellerRestuarent;
    }

    public ArrayList<DateItemVO> getMemberisSeller() {
        return memberisSeller;
    }

    public void setMemberisSeller(ArrayList<DateItemVO> memberisSeller) {
        this.memberisSeller = memberisSeller;
    }

    public ArrayList<Member> getBuyerInfo() {
        return buyerInfo;
    }

    public void setBuyerInfo(ArrayList<Member> buyerInfo) {
        this.buyerInfo = buyerInfo;
    }

    public ArrayList<Pet> getSellerPetO() {
        return sellerPetO;
    }

    public void setSellerPetO(ArrayList<Pet> sellerPetO) {
        this.sellerPetO = sellerPetO;
    }

    public ArrayList<Restaurant> getBuyerRestuarent() {
        return buyerRestuarent;
    }

    public void setBuyerRestuarent(ArrayList<Restaurant> buyerRestuarent) {
        this.buyerRestuarent = buyerRestuarent;
    }

    public OrderVO(ArrayList<DateItemVO> memberisBuyer, ArrayList<Member> sellerInfo, ArrayList<Pet> sellerPet, ArrayList<Restaurant> sellerRestuarent, ArrayList<DateItemVO> memberisSeller, ArrayList<Member> buyerInfo, ArrayList<Pet> sellerPetO, ArrayList<Restaurant> buyerRestuarent) {
        this.memberisBuyer = memberisBuyer;
        this.sellerInfo = sellerInfo;
        this.sellerPet = sellerPet;
        this.sellerRestuarent = sellerRestuarent;
        this.memberisSeller = memberisSeller;
        this.buyerInfo = buyerInfo;
        this.sellerPetO = sellerPetO;
        this.buyerRestuarent = buyerRestuarent;

    }


}
