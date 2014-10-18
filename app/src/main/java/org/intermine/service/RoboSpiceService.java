package org.intermine.service;

import android.app.Application;

import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

public class RoboSpiceService extends SpringAndroidSpiceService {
    public final static int THREAD_COUNT = 3;

    public final static String CHARSET = "UTF-8";

    @Override
    public RestTemplate createRestTemplate() {
        RestTemplate rtp = new RestTemplate();
        Charset utf8 = Charset.forName(CHARSET);

        ByteArrayHttpMessageConverter byteConv = new ByteArrayHttpMessageConverter();
        StringHttpMessageConverter stringConv = new StringHttpMessageConverter(utf8);

        FormHttpMessageConverter formConv = new FormHttpMessageConverter();
        formConv.setCharset(utf8);

        List<HttpMessageConverter<?>> converters = rtp.getMessageConverters();

        converters.add(byteConv);
        converters.add(stringConv);
        converters.add(formConv);

        rtp.setMessageConverters(converters);
        return rtp;
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        return new CacheManager();
    }

    @Override
    public int getThreadCount() {
        return THREAD_COUNT;
    }
}
