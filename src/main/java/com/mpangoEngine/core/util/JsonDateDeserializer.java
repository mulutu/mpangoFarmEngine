package com.mpangoEngine.core.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDateDeserializer  extends JsonDeserializer<Date> {
	@Override
    public Date deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String dt = jp.getText();
        if (dt == null || dt.trim().length() == 0) {
            return null;
        }
        Date date = null;
        try {
            date = fmt.parse(dt);
        } catch (ParseException e) {
            throw new IOException(e);
        }

        return date;
    }
}
