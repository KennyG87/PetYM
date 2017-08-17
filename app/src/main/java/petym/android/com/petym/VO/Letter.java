package petym.android.com.petym.VO;

import java.io.Serializable;
import java.sql.Timestamp;

public class Letter implements Serializable{
	
	private Integer letterNo;
	private Integer letterTypeNo;
	private Integer memNo;
	private Timestamp letterTime;
	private Integer letterState;
	private Integer letterTag;
	private String letterText;
	
	
	public Integer getLetterNo() {
		return letterNo;
	}
	public void setLetterNo(Integer letterNo) {
		this.letterNo = letterNo;
	}
	public Integer getLetterTypeNo() {
		return letterTypeNo;
	}
	public void setLetterTypeNo(Integer letterTypeNo) {
		this.letterTypeNo = letterTypeNo;
	}
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public Timestamp getLetterTime() {
		return letterTime;
	}
	public void setLetterTime(Timestamp letterTime) {
		this.letterTime = letterTime;
	}
	public Integer getLetterState() {
		return letterState;
	}
	public void setLetterState(Integer letterState) {
		this.letterState = letterState;
	}
	public Integer getLetterTag() {
		return letterTag;
	}
	public void setLetterTag(Integer letterTag) {
		this.letterTag = letterTag;
	}
	public String getLetterText() {
		return letterText;
	}
	public void setLetterText(String letterText) {
		this.letterText = letterText;
	}
	
	

}
