package vis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PConstants;
import scsd.MainPApplet;
import util.Assets;

/* This is just the data */ 
//-----------------------------------------------------------------
 class EnergyUser {
  int 		dist_id; // district id 
  String   	name; // nickname 
  float   	electro; // electricity bill value 
  float   	gas;     // gas bill value
  float   	energy; // kwh
  
  EnergyUser(int _id, String _name, float _electro, float _gas, float _energy){
    dist_id = _id; 
    name 	= _name; 
    electro = _electro; 
    gas 	= _gas; 
    energy 	= _energy;
  }
  
  public void printme(){
    System.out.println(dist_id + " " + name + " " + electro + " " + gas + " " + energy);
  }
}

//-----------------------------------------------------------------
//Loaded from a configuration file
 class DistrictInfo{
  int id; 
  String name; 
  String code;
  int col; 
  
   DistrictInfo(int _id, String _name, String _code, int _colorInt){
    id = _id;   
    name= _name;
    code = _code;
    col = _colorInt;   //IN HEX 
    //int hi = unhex("FF006699"/*"3399ffff"*/);
    //col = color(hi);
    printme();
  }
  void printme(){
    System.out.println("DistrictInfo " + id + " Name " + name + " Code " + code + " Color" + " " + col);
  }
}



//-----------------------------------------------------------------
public class EnergyUserData
{
  // all energy users from all districts 
  // each element is a list of the energy users of one district 
    
  //ArrayList allEnergyUsers;
  
  HashMap<Integer, ArrayList<EnergyUser>> 		mapUsers; // 
  HashMap<Integer, DistrictInfo> 				districtInfos; 

    
  int numUsers; // current number of user that have revealed their data 
  int numDistricts; 
  boolean debug = false; 
  
  GenericVis genericVis;  //generic vis
 
  public EnergyUserData(){
	mapUsers 		= new HashMap<Integer, ArrayList<EnergyUser>>();
    districtInfos 	= new HashMap<Integer, DistrictInfo>();
    System.out.println("EnergyUsage initialized...");
    numUsers = 0; 
    numDistricts = 0; 
    
    loadData(Assets.districtConfig);
  }    
  
  int getDistrictColor(int _id){
    if (!districtInfos.containsKey(_id)) return 255;
    else return ((DistrictInfo)districtInfos.get(_id)).col;  
  }

  String getDistrictName(int _id){
    if (!districtInfos.containsKey(_id)) { 
   //   println("District " + _id); return "UNKNOWN_NAME"; 
   return "";
    }
    else return ((DistrictInfo)districtInfos.get(_id)).name; 
  }

  String getDistrictCode(int _id){
    if (!districtInfos.containsKey(_id)) return "";
    else return ((DistrictInfo)districtInfos.get(_id)).code; 
  }  
  
    //Loads info from config file and converts it into bunch of districtInfoEntities
    void loadData(String filename) {
    
      // Load CSV-File
      String[]lines = MainPApplet.getInstance().loadStrings(filename);
      //data = new data[lines.length];
      
      for(int i = 0; i < lines.length; i++) {
        String[]pieces = PApplet.split(lines[i], ',');
       // districtInfos.add(new DistrictInfo(Integer.parseInt(pieces[0]), pieces[1], pieces[2], Integer.parseInt(pieces[3]) ) );
        //districtInfos.put(Integer.parseInt(pieces[0]), new DistrictInfo(Integer.parseInt(pieces[0]), pieces[1], pieces[2], unhex(pieces[3])));
        
         MainPApplet.getInstance().colorMode(PConstants.HSB, 360, 100, 100);
         int id 		= Integer.parseInt(pieces[0]);
         String name	= pieces[1];
         String code	= pieces[2];
         int colorInt	= PApplet.unhex(pieces[3]);
        		 
         districtInfos.put(Integer.parseInt(pieces[0]), new DistrictInfo(id, name, code, colorInt));
         MainPApplet.getInstance().colorMode(PConstants.RGB, 255);
      }
      
    }  

  public void registerVis(GenericVis _vis){
    genericVis = _vis;
  }
  
  public void addEnergyUser(int _id, String _name, float _electro, float _gas, float _energy){
    if (debug)
      System.out.println("addEnergyUser: (" + _id +","+ _name +","+ _electro +","+ _gas + ","+ _energy +")");
    
    // if this district is already in the hashmap
    if (mapUsers.containsKey(_id)){
      ArrayList<EnergyUser> userList = (ArrayList<EnergyUser>) mapUsers.get(_id); 
      userList.add(new EnergyUser(_id, _name, _electro, _gas, _energy));
    } 
    else {
      // otherwise create a new list (for a new district) and add this user to it; update the hash
      ArrayList<EnergyUser> userList = new ArrayList<EnergyUser>(); 
      userList.add(new EnergyUser(_id, _name, _electro, _gas, _energy));
      mapUsers.put(_id, userList);
      numDistricts++;
    }
    genericVis.notifyNewUser(_id, _name, _electro, _gas, _energy); // notify vis that sth has changed 
    numUsers++;
  }
  
  void printme(){
      // Make an iterator to look at all the things in the HashMap
      Iterator<ArrayList<EnergyUser>> i = mapUsers.values().iterator();
      while (i.hasNext()) {
        // Look at each word
        ArrayList<?> userList = (ArrayList<?>) i.next();
          for (int p = userList.size()-1; p >= 0; p--) { 
            EnergyUser u = (EnergyUser)userList.get(p);
            u.printme();
          }
      }
  }
  
  int getNumUsers(){ return numUsers; }
  int getNumDistricts(){ return numDistricts; }
  
  public void keyPressed(int _key){
    switch(_key){
      case 'd': debug = !debug; 
    }
  }
}



//-----------------------------------------------------------------

