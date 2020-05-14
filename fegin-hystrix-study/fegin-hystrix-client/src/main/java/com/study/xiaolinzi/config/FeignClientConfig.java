//package com.study.xiaolinzi.config;
//
//
//
//
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import feign.*;
//
//@Component
//public class FeignClientConfig {
//
//
//
//    private Map<String, Object> apiMap = new ConcurrentHashMap<>(64);
//
//    private <T> T getApi(Class<T> apiClass, String apiBaseUrl) {
//        String apiKey = apiBaseUrl + apiClass.getName();
//        if (apiMap.containsKey(apiKey)) {
//            return (T)(apiMap.get(apiKey));
//        }
//        T api = Feign.builder().client(new ApacheHttpClient(HttpClientUtils.getHttpClient()))
//                .options(new Request.Options((int) 30 * 1000, (int) 30 * 1000))
//                .encoder(new JacksonEncoder())
//                .decoder(new JacksonDecoder())
//                .requestInterceptor(new InsertHeaderInterceptor())
//                .retryer(Retryer.NEVER_RETRY).target(apiClass, apiBaseUrl);
//        apiMap.put(apiKey, api);
//        return api;
//    }
////    static class InsertHeaderInterceptor implements RequestInterceptor {
////
////        @Override
////        public void apply( RequestTemplate template ) {
////            template.header(RequestConstants.TX_ID, MDC.get(RequestConstants.TX_ID));
////        }
////    }
//
//
////    @Bean
////    QueryRepaymentProductApi getQueryRepaymentProductApi() {
////        return getApi(QueryRepaymentProductApi.class,
////                cuIbasProviderProperties.getUrl());
////    }
//
//}
