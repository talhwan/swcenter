package com.thc.sprbasic2025.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NowDate {
    public String getNow(){
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //현재시간
        return simpleDateFormat.format(nowDate);
    }
    public String due(int minute){
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        //시간 더하기
        cal.add(Calendar.MINUTE, minute);
        //현재시간 + 시간 더한 값
        return simpleDateFormat.format(cal.getTime());
    }
}
