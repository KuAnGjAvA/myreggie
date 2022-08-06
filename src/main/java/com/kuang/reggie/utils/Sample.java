//package com.kuang.reggie.utils;
//
//// This file is auto-generated, don't edit it. Thanks.
//
//import com.aliyun.tea.*;
//import com.aliyun.dysmsapi20170525.*;
//import com.aliyun.dysmsapi20170525.models.*;
//import com.aliyun.teaopenapi.*;
//import com.aliyun.teaopenapi.models.*;
//import com.aliyun.teautil.*;
//import com.aliyun.teautil.models.*;
//
//public class Sample {
//
//    /**
//     * 使用AK&SK初始化账号Client
//     * @param accessKeyId
//     * @param accessKeySecret
//     * @return Client
//     * @throws Exception
//     */
//    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
//        Config config = new Config()
//                // 您的 AccessKey ID
//                .setAccessKeyId(accessKeyId)
//                // 您的 AccessKey Secret
//                .setAccessKeySecret(accessKeySecret);
//        // 访问的域名
//        config.endpoint = "dysmsapi.aliyuncs.com";
//        return new com.aliyun.dysmsapi20170525.Client(config);
//    }
//
//    public static void main(String[] args_) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
//        com.aliyun.dysmsapi20170525.Client client = Sample.createClient("accessKeyId", "accessKeySecret");
//        SendSmsRequest sendSmsRequest = new SendSmsRequest()
//                .setSignName("阿里云短信测试")
//                .setTemplateCode("SMS_154950909")
//                .setPhoneNumbers("19128133772")
//                .setTemplateParam("{\"code\":\"1234\"}");
//        RuntimeOptions runtime = new RuntimeOptions();
//        try {
//            // 复制代码运行请自行打印 API 的返回值
//            client.sendSmsWithOptions(sendSmsRequest, runtime);
//        } catch (TeaException error) {
//            // 如有需要，请打印 error
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        } catch (Exception _error) {
//            TeaException error = new TeaException(_error.getMessage(), _error);
//            // 如有需要，请打印 error
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        }
//    }
//}
//
