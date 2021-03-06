package com.payu.sdk;

import com.payu.sdk.exceptions.PayuSdkException;
import com.payu.sdk.messages.converters.RequestSerializer;
import com.payu.sdk.messages.converters.ResponseDeserializer;
import com.payu.sdk.messages.converters.ResponseType;
import com.payu.sdk.messages.request.OrderCreateRequest;
import com.payu.sdk.messages.request.OrderStatusUpdateRequest;
import com.payu.sdk.messages.request.RefundCreateRequest;
import com.payu.sdk.messages.response.OpenPayUResponse;
import com.payu.sdk.messages.response.OrderCreateResponse;
import com.payu.sdk.messages.response.OrderRetrieveResponse;
import com.payu.sdk.messages.response.RefundCreateResponse;
import com.payu.sdk.network.senders.HttpGetSender;
import com.payu.sdk.network.senders.HttpPostSender;
import com.payu.sdk.network.senders.PayUHttpResponse;
import com.payu.sdk.properties.PropertyNames;
import com.payu.sdk.properties.SDKProperties;

import java.io.IOException;
import java.text.MessageFormat;

public class PayUSDKImpl implements PayUSDK {
    private String login = SDKProperties.getProperty(PropertyNames.POS);
    private String password = SDKProperties.getProperty(PropertyNames.SECOND_KEY_MD5);

    @Override
    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest) throws PayuSdkException {
        try {
            String url = SDKProperties.getProperty(PropertyNames.URL);

            HttpPostSender httpPostSender = new HttpPostSender();
            PayUHttpResponse result = httpPostSender.sendRequest(url, login, password, RequestSerializer.serializeRequest(orderCreateRequest));

            OrderCreateResponse orderCreateResponse = (OrderCreateResponse) ResponseDeserializer.parseResponse(result.getPayload(), ResponseType.ORDER_CREATE_RESPONSE);
            return orderCreateResponse;
        } catch (Exception e) {
            throw new PayuSdkException(e.getMessage(), e);
        }
    }

    @Override
    public OrderRetrieveResponse retrieveOrder(String orderId) throws PayuSdkException {
        try {
            String orderRetrieveUrl = SDKProperties.getProperty(PropertyNames.ORDER_RETRIEVE_URL);
            MessageFormat format = new MessageFormat(orderRetrieveUrl);
            String[] args = new String[]{orderId};
            String url = format.format(args);

            HttpGetSender httpGetSender = new HttpGetSender();

            PayUHttpResponse result = httpGetSender.sendRequest(url, login, password);
            OrderRetrieveResponse orderRetrieveResponse = (OrderRetrieveResponse) ResponseDeserializer.parseResponse(result.getPayload(), ResponseType.ORDER_RETRIEVE_RESPONSE);
            return orderRetrieveResponse;
        } catch (IOException e) {
            throw new PayuSdkException(e.getMessage(), e);
        }
    }

    @Override
    public OpenPayUResponse updateOrder(OrderStatusUpdateRequest orderStatusUpdateRequest) {
        return null;
    }

    @Override
    public OpenPayUResponse cancelOrder(String orderId) {
        return null;
    }

    @Override
    public RefundCreateResponse createRefund(RefundCreateRequest refundCreateRequest) {
        return null;
    }
}
