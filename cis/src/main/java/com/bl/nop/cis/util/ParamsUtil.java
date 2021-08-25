package com.bl.nop.cis.util;

import java.util.ArrayList;
import java.util.List;

public class ParamsUtil {

    public static List<String> castList(Object obj) {
        List<String> result = new ArrayList<>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (ArrayList<?>) obj) {
                result.add(String.class.cast(o));
            }
            return result;
        }
        return null;
    }

}
