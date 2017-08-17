package petym.android.com.petym.VO;

import java.io.Serializable;
import java.util.ArrayList;

import petym.android.com.petym.VO.DateItemVO;
import petym.android.com.petym.VO.Member;
import petym.android.com.petym.VO.Pet;
import petym.android.com.petym.VO.Restaurant;

public class OrderVOAll implements Serializable {

	ArrayList<DateItemVO> all;
	ArrayList<Member> buyerInfo;
	ArrayList<Member> sellerInfo;
	ArrayList<Pet> sellerPetO;
	ArrayList<Restaurant> buyerRestuarent;
	public OrderVOAll(){}
	public ArrayList<DateItemVO> getAll() {
		return all;
	}
	public void setAll(ArrayList<DateItemVO> all) {
		this.all = all;
	}
	public ArrayList<Member> getBuyerInfo() {
		return buyerInfo;
	}
	public void setBuyerInfo(ArrayList<Member> buyerInfo) {
		this.buyerInfo = buyerInfo;
	}
	public ArrayList<Member> getSellerInfo() {
		return sellerInfo;
	}
	public void setSellerInfo(ArrayList<Member> sellerInfo) {
		this.sellerInfo = sellerInfo;
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
	public OrderVOAll(ArrayList<DateItemVO> all, ArrayList<Member> buyerInfo, ArrayList<Member> sellerInfo,
					  ArrayList<Pet> sellerPetO, ArrayList<Restaurant> buyerRestuarent) {
		super();
		this.all = all;
		this.buyerInfo = buyerInfo;
		this.sellerInfo = sellerInfo;
		this.sellerPetO = sellerPetO;
		this.buyerRestuarent = buyerRestuarent;
	}

}