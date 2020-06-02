package com.umbr3114.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umbr3114.Main;
import spark.ResponseTransformer;

public class JsonResponse implements ResponseTransformer {

    @Override
    public String render(Object model) throws Exception {
        ObjectMapper mapper = Main.services.jsonMapper();
        return mapper.writeValueAsString(model);
    }
}
