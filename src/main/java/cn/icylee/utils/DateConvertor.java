package cn.icylee.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateConvertor implements Converter<String, Date>{

    @Override
    public Date convert(String arg0) {
        // TODO Auto-generated method stub
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sim.parse(arg0);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
