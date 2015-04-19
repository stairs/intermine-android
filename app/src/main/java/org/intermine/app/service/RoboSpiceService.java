package org.intermine.app.service;

import android.app.Application;

import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.memory.LruCache;
import com.octo.android.robospice.persistence.memory.LruCacheObjectPersister;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

import static org.intermine.app.net.request.get.GetListsRequest.Lists;
import static org.intermine.app.net.request.get.GetTemplatesRequest.Templates;

public class RoboSpiceService extends SpringAndroidSpiceService {
    public final static int THREAD_COUNT = 3;

    public final static int DEFAULT_CACHE_SIZE = 5;

    public final static String CHARSET = "UTF-8";

    @Override
    public CacheManager createCacheManager(Application app) throws CacheCreationException {
        LruCache<String, Templates> templatesCache = new LruCache<>(DEFAULT_CACHE_SIZE);
        LruCache<String, Lists> listsCache = new LruCache<>(DEFAULT_CACHE_SIZE);

        LruCacheObjectPersister templatesPersister;
        LruCacheObjectPersister listsPersister;

        templatesPersister = new LruCacheObjectPersister(Templates.class, templatesCache);
        listsPersister = new LruCacheObjectPersister(Lists.class, listsCache);

        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(templatesPersister);
        cacheManager.addPersister(listsPersister);
        return cacheManager;
    }

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
    public int getCoreThreadCount() {
        return super.getCoreThreadCount();
    }

    @Override
    public int getThreadCount() {
        return THREAD_COUNT;
    }
}
