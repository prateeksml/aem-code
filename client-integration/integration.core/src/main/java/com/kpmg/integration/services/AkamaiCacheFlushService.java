package com.kpmg.integration.services;

import com.google.api.client.http.HttpResponse;
import java.io.IOException;
import java.util.List;

public interface AkamaiCacheFlushService {

  HttpResponse akamaiFastPurgeClient(List<String> flushUrlJson) throws IOException;
}
