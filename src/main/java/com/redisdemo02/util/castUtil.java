package com.redisdemo02.util;

// import java.nio.channels.UnsupportedAddressTypeException;

import org.springframework.stereotype.Component;

@Component
public final class castUtil {
    
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj){
        return (T)obj;
    }

}
