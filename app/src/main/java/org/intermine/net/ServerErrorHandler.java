package org.intermine.net;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Custom RestTemplate error handler
 * <p/>
 * Using this wrapper we can handle custom server exceptions (see specs.) and send HTTP
 * status/headers to RoboSpice error handler. Our exception will be as cause in SpiceExeption.
 * <p/>
 * <code>
 * SpiceExpeption exception; // Argument from our callback
 * HttpNetworkException networkEx = (HttpNetworkException)exception.getCause();
 * </code>
 *
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public class ServerErrorHandler implements ResponseErrorHandler {
    public final static String ENCODING = "UTF-8";
    public final static String ERROR_SUFFIX = "_error";

    private Gson mMapper;

    private DefaultResponseErrorHandler mHander;

    public ServerErrorHandler() {
        mHander = new DefaultResponseErrorHandler();
        mMapper = new Gson();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
//        ErrorProtos.Error error = null;
//        String httpMsg = Strs.nullToEmpty(null);
//        InputStream in = response.getBody();
//
//        MediaType contentType = response.getHeaders().getContentType();
//        if (null != in) {
//            if (HttpStatus.UNPROCESSABLE_ENTITY == response.getStatusCode()) {
//                if (PROTOBUF_TYPE.equals(contentType.toString())) {
//                    error = ErrorProtos.Error.parseFrom(in);
//                } else {
//                    StringWriter writer = new StringWriter();
//                    IOUtils.copy(in, writer, ENCODING);
//                    httpMsg = Strs.nullToEmpty(writer.toString());
//
//                    ErrorMessageEntity errorMessage =
//                            mMapper.fromJson(httpMsg, ErrorMessageEntity.model);
//
//                    if (null != errorMessage && !Strs.isNullOrEmpty(errorMessage.getError())) {
//                        httpMsg = errorMessage.getError();
//                    }
//                }
//            } else {
//                StringWriter writer = new StringWriter();
//                IOUtils.copy(in, writer, ENCODING);
//                httpMsg = Strs.nullToEmpty(writer.toString());
//
//                if (null != contentType && APPLICATION_JSON_VALUE.equals(contentType.toString())) {
//                    if (HttpStatus.BAD_REQUEST == response.getStatusCode()) {
//                        ErrorMessageEntity errorMessage =
//                                mMapper.fromJson(httpMsg, ErrorMessageEntity.model);
//
//                        if (null != errorMessage && !Strs.isNullOrEmpty(errorMessage.getError())) {
//                            httpMsg = errorMessage.getError() + ERROR_SUFFIX;
//                        }
//                    } else if (HttpStatus.INTERNAL_SERVER_ERROR == response.getStatusCode()) {
//                        CardBindingErrorEntity errorMessage =
//                                mMapper.fromJson(httpMsg, CardBindingErrorEntity.model);
//
//                        if (null != errorMessage) {
//                            httpMsg = errorMessage.getStatusCode() + ERROR_SUFFIX;
//                        }
//                    }
//                }
//            }
//        }
//
//        HttpNetworkException e = new HttpNetworkException(response.getStatusText());
//        e.setHeaders(response.getHeaders());
//        e.setStatus(response.getStatusCode());
//        e.setHttpMessage(httpMsg);
//        e.setError(error);
//
//        Logs.netEx(e);
//
//        throw e;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return mHander.hasError(response);
    }
}
