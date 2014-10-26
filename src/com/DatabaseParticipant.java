package com;
public class DatabaseParticipant{
	String 		cardID;
	int 		devID; 
    int 		catID;
    int 		prefID;
    long 		tstamp;
    int 		condition;
    
    public DatabaseParticipant(String _cardID, int _devID, int _catID, int _prefID, long _tstamp) {
    	cardID 	= _cardID; 
    	devID	= _devID; 
    	catID	= _catID; 
    	prefID	= _prefID; 
    	tstamp  = _tstamp; 
    }
    
    public void print(){
    	System.out.println("DatabaseParticipant: " + getCardID() + " " + getDevID() + " " + getCatID() + " " + getPrefID());
    }
    
    public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public int getDevID() {
		return devID;
	}

	public void setDevID(int devID) {
		this.devID = devID;
	}

	public int getCatID() {
		return catID;
	}

	public void setCatID(int catID) {
		this.catID = catID;
	}

	public int getPrefID() {
		return prefID;
	}

	public void setPrefID(int prefID) {
		this.prefID = prefID;
	}
	
	public long getTstamp() {
		return tstamp;
	}

	public void setTstamp(long tstamp) {
		this.tstamp = tstamp;
	}  
}
