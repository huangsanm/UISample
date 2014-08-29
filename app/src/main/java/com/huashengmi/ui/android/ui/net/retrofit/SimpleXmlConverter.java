package com.huashengmi.ui.android.ui.net.retrofit;

import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by huangsm on 2014/8/29 0029.
 * Email:huangsanm@foxmail.com
 */
public class SimpleXmlConverter implements Converter {


    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        return body.length();
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }

}
