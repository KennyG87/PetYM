package petym.android.com.petym.VO;

import java.io.Serializable;

public class LetterType implements Serializable{
	
	private Integer letterTypeNo;
	private String letterTypeName;
	private String letterTypeText;
	
	
	public Integer getLetterTypeNo() {
		return letterTypeNo;
	}
	public void setLetterTypeNo(Integer letterTypeNo) {
		this.letterTypeNo = letterTypeNo;
	}
	public String getLetterTypeName() {
		return letterTypeName;
	}
	public void setLetterTypeName(String letterTypeName) {
		this.letterTypeName = letterTypeName;
	}
	public String getLetterTypeText() {
		return letterTypeText;
	}
	public void setLetterTypeText(String letterTypeText) {
		this.letterTypeText = letterTypeText;
	}
	
	
	
	

}
