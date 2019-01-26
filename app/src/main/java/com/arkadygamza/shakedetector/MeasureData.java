package com.arkadygamza.shakedetector;

import java.util.LinkedList;

public class MeasureData {
    // points from accelerometr
    private LinkedList accData;
    private LinkedList data;
    // timer interval of generating points
    private long interval;

    public MeasureData(long interval) {
        this.interval = interval;
        accData = new LinkedList ();
        data = new LinkedList ();
    }
    
    public void addPoint(Point p){
        accData.add(p);
    }
    
    public void process(){
        
        for(int i = 0; i < accData.size(); ++i){
            Point p = (Point) accData.get(i);
            float speed = 0;
            
            if(i > 0){
          //      speed = data.get(i-1).getSpeedAfter();
            }
            data.add(new MeasurePoint(p.getX(), p.getY(), p.getZ(), speed, interval));
        }
    }
           
   // public float getLastSpeed(){
        ///return data.getLast().getSpeedAfter();
  //  }
    
//    public float getLastSpeedKm(){
//        float ms = getLastSpeed();
//        return ms*3.6f;
//    }
    public void getSpeedAfter(){}
}