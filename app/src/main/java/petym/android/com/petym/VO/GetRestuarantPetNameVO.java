package petym.android.com.petym.VO;

import java.util.ArrayList;

/**
 * Created by k3nt on 2017/8/8.
 */

public class GetRestuarantPetNameVO {
    ArrayList<String> petName;
    ArrayList<String> restuarant;

    public GetRestuarantPetNameVO(ArrayList<String> petName, ArrayList<String> restuarant) {
        this.petName = petName;
        this.restuarant = restuarant;
    }
    public GetRestuarantPetNameVO(){}
    public ArrayList<String> getPetName() {
        return petName;
    }

    public void setPetName(ArrayList<String> petName) {
        this.petName = petName;
    }

    public ArrayList<String> getRestuarant() {
        return restuarant;
    }

    public void setRestuarant(ArrayList<String> restuarant) {
        this.restuarant = restuarant;
    }
}
