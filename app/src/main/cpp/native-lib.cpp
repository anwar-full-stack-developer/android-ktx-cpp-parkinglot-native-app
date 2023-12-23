#include <jni.h>
#include <string>
#include <sstream>
#include <iostream>
#include <android/log.h>
#include "HTTPRequest.hpp"
//#include "HTTPRequest/include/HTTPRequest.hpp"

#include "rapidjson/document.h"
#include "rapidjson/writer.h"
#include "rapidjson/stringbuffer.h"

using namespace rapidjson;

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_parkinglot_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_parkinglot_MainActivity_boolFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    jboolean tf = true;
    return tf;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_example_parkinglot_MainActivity_numberFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    jlong jl = 100001;
    return jl;
}


extern "C" JNIEXPORT jobject JNICALL
Java_com_example_parkinglot_MainActivity_objectFromJNI(
        JNIEnv *env,
        jobject state /* this */,
        jobject jobject1
) {
    jobject ob = jobject1;
    return ob;
}


extern "C" JNIEXPORT jobject JNICALL
Java_com_example_parkinglot_MainActivity_httpGetRequestFromJNI(
        JNIEnv *env,
        jobject state /* this */,
        jobject jobject1
) {

//    const char json[] = " { \"hello\" : \"world\", \"t\" : true , \"f\" : false, \"n\": null, \"i\":123, \"pi\": 3.1416, \"a\":[1, 2, 3, 4] } ";
//    printf("Original JSON:\n %s\n", json);

//            using namespace rapidjson;


    try {
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Get Request Started");

        // you can pass http::InternetProtocol::V6 to Request to make an IPv6 request
        http::Request request{"http://192.168.4.101:8000/api/parkinglot"};

        // send a get request
        const auto response = request.send("GET", "", {
                {"Content-Type", "application/json"}
        });
        std::string responseBody = std::string{response.body.begin(), response.body.end()};

//                const char* responseBodyStr = responseBody.c_str();
        const char *responseBodyStr = responseBody.data();

//                rapidjson::Document responseJson;
        Document responseJson;
        responseJson.Parse(responseBodyStr);

        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Response: %s",
                            responseBody.c_str());
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Response Status Code: %d",
                            responseJson["status"].GetInt());
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Response d Count: %d",
                            responseJson["data"].GetArray().Size());

        std::cout << "CPP HTTP Response: "
                  << std::string{response.body.begin(), response.body.end()}
                  << '\n'; // print the result
    }
    catch (const std::exception &e) {
        std::cerr << "Request failed, error: " << e.what() << '\n';
    }
    jobject ob = jobject1;


    return ob;
}


extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_parkinglot_ParkinglotListFragment_getParkinglotListFromJNI(
        JNIEnv *env,
        jobject state /* this */
) {

    std::string defaultStr = "{}";

    jbyteArray arrD = env->NewByteArray(defaultStr.length());
    env->SetByteArrayRegion(arrD, 0, defaultStr.length(), (jbyte *) defaultStr.c_str());
//        return arrD;
    try {
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Get Request Started");

        // you can pass http::InternetProtocol::V6 to Request to make an IPv6 request
        http::Request request{"http://192.168.4.101:8000/api/parkinglot"};

        // send a get request
        const auto response = request.send("GET", "", {
                {"Content-Type", "application/json"}
        });
        std::string responseBody = std::string{response.body.begin(), response.body.end()};

//                const char* responseBodyStr = responseBody.c_str();
        const char *responseBodyStr = responseBody.data();

//                rapidjson::Document responseJson;
        Document responseJson;
        responseJson.Parse(responseBodyStr);

        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Response: %s",
                            responseBody.c_str());
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Response Status Code: %d",
                            responseJson["status"].GetInt());
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Response d Count: %d",
                            responseJson["data"].GetArray().Size());


        if (200 == responseJson["status"].GetInt()) {
            jbyteArray arr = env->NewByteArray(responseBody.length());
            env->SetByteArrayRegion(arr, 0, responseBody.length(), (jbyte *) responseBody.c_str());
            return arr;
        } else {
            return arrD;
        }
    }
    catch (const std::exception &e) {
        std::cerr << "Request failed, error: " << e.what() << '\n';
    }
    return arrD;
}


extern "C" JNIEXPORT jint JNICALL
Java_com_example_parkinglot_ParkinglotListFragment_deleteParkinglotFromJNI(
        JNIEnv *env,
        jobject state /* this */,
        jstring jid
) {
    try {
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Delete Request Started");

        //// convert jstring to cstring
        const char *cidstr = env->GetStringUTFChars(jid, NULL);
        std::string cid = std::string(cidstr);
        env->ReleaseStringUTFChars(jid, cidstr);

        // concat url
        std::stringstream ss;
        ss << "http://192.168.4.101:8000/api/parkinglot/" << cid ;
        std::string url = ss.str();

        //convert c/c++ string to (jstring)
//        https://docs.oracle.com/javase/1.5.0/docs/guide/jni/spec/functions.html#wp2556
//       jstring jurl_ =  env ->NewStringUTF(url.data());


        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP DELETE URL: %s",
                            url.c_str());

        // you can pass http::InternetProtocol::V6 to Request to make an IPv6 request
        http::Request request{ url.c_str() };
        // send a get request
        const auto response = request.send("DELETE", "", {
                {"Content-Type", "application/json"}
        });
        std::string responseBody = std::string{response.body.begin(), response.body.end()};

//                const char* responseBodyStr = responseBody.c_str();
        const char *responseBodyStr = responseBody.data();

        Document responseJson;
        responseJson.Parse(responseBodyStr);

        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP DELETE Response: %s",
                            responseBody.c_str());
        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP DELETE Response Status Code: %d",
                            responseJson["status"].GetInt());
        if (200 == responseJson["status"].GetInt()) {
            return 1;
        } else {
            return -1;
        }
    }
    catch (const std::exception &e) {
        std::cerr << "Request failed, error: " << e.what() << '\n';
    }
    return -1;
}



extern "C" JNIEXPORT jstring JNICALL
Java_com_example_parkinglot_ParkinglotEditFragment_saveParkinglotFromJNI(
        JNIEnv *env,
        jobject state /* this */,
        jstring bodyJsonData
) {
    try {
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Save Request Started");

        //// convert jstring to cstring
        const char *cidstr = env->GetStringUTFChars(bodyJsonData, NULL);
        std::string bodyJsonDataC = std::string(cidstr);
        env->ReleaseStringUTFChars(bodyJsonData, cidstr);

        // concat url
        std::stringstream ss;
        ss << "http://192.168.4.101:8000/api/parkinglot" ;
        std::string url = ss.str();

        //convert c/c++ string to (jstring)
//        https://docs.oracle.com/javase/1.5.0/docs/guide/jni/spec/functions.html#wp2556
//       jstring jurl_ =  env ->NewStringUTF(url.data());


        const std::string body = bodyJsonDataC;

        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP Save URL: %s",
                            url.c_str());

        // you can pass http::InternetProtocol::V6 to Request to make an IPv6 request
        http::Request request{ url.c_str() };
        // send a get request
        const auto response = request.send("POST", body, {
                {"Content-Type", "application/json"}
        });
        std::string responseBody = std::string{response.body.begin(), response.body.end()};

//                const char* responseBodyStr = responseBody.c_str();
        const char *responseBodyStr = responseBody.data();

        Document responseJson;
        responseJson.Parse(responseBodyStr);

        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP Save Response: %s",
                            responseBody.c_str());
        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP Save Response Status Code: %d",
                            responseJson["status"].GetInt());
//        if (200 == responseJson["status"].GetInt()) {
            return env-> NewStringUTF(responseBody.c_str());
//        }
//        else {
//            return -1;
//        }
    }
    catch (const std::exception &e) {
        std::cerr << "Request failed, error: " << e.what() << '\n';
    }
    return env-> NewStringUTF(std::string {"{}" }.c_str());
}




extern "C" JNIEXPORT jstring JNICALL
Java_com_example_parkinglot_ParkinglotEditFragment_updateParkinglotFromJNI(
        JNIEnv *env,
        jobject state /* this */,
        jstring jid,
        jstring bodyJsonData
) {
    try {
        __android_log_print(ANDROID_LOG_DEBUG, "Main", "CPP HTTP Update Request Started");

        //// convert jstring to cstring
        const char *cidstr = env->GetStringUTFChars(jid, NULL);
        std::string idC = std::string(cidstr);
        env->ReleaseStringUTFChars(jid, cidstr);


        //// convert jstring to cstring
        const char *cbodystr = env->GetStringUTFChars(bodyJsonData, NULL);
        std::string bodyJsonDataC = std::string(cbodystr);
        env->ReleaseStringUTFChars(bodyJsonData, cbodystr);

        // concat url
        std::stringstream ss;
        ss << "http://192.168.4.101:8000/api/parkinglot/" << idC;
        std::string url = ss.str();

        //convert c/c++ string to (jstring)
//        https://docs.oracle.com/javase/1.5.0/docs/guide/jni/spec/functions.html#wp2556
//       jstring jurl_ =  env ->NewStringUTF(url.data());

        const std::string body = bodyJsonDataC;

        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP Update URL: %s",
                            url.c_str());

        // you can pass http::InternetProtocol::V6 to Request to make an IPv6 request
        http::Request request{ url.c_str() };
        // send a get request
        const auto response = request.send("PUT", body, {
                {"Content-Type", "application/json"}
        });
        std::string responseBody = std::string{response.body.begin(), response.body.end()};

//                const char* responseBodyStr = responseBody.c_str();
        const char *responseBodyStr = responseBody.data();

        Document responseJson;
        responseJson.Parse(responseBodyStr);

        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP Update Response: %s",
                            responseBody.c_str());
        __android_log_print(ANDROID_LOG_DEBUG, "Main",
                            "CPP HTTP Update Update Status Code: %d",
                            responseJson["status"].GetInt());
//        if (200 == responseJson["status"].GetInt()) {
        return env-> NewStringUTF(responseBody.c_str());
//        }
//        else {
//            return -1;
//        }
    }
    catch (const std::exception &e) {
        std::cerr << "Request failed, error: " << e.what() << '\n';
    }
    return env-> NewStringUTF(std::string {"{}" }.c_str());
}



// convert c string to j string
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_parkinglot_MainActivity_getJniString(JNIEnv *env, jclass, std::string &message
) {
//std::string message = "Would you prefer €20 once "
//                      "or ₹10 every day for a year?";

    int byteCount = message.length();
    jbyte *pNativeMessage = const_cast<jbyte *>(reinterpret_cast<const jbyte *>(message.c_str()));
    jbyteArray bytes = env->NewByteArray(byteCount);
    env->SetByteArrayRegion(bytes, 0, byteCount, pNativeMessage);

// find the Charset.forName method:
//   javap -s java.nio.charset.Charset | egrep -A2 "forName"
    jclass charsetClass = env->FindClass("java/nio/charset/Charset");
    jmethodID forName = env->GetStaticMethodID(
            charsetClass, "forName", "(Ljava/lang/String;)Ljava/nio/charset/Charset;");
    jstring utf8 = env->NewStringUTF("UTF-8");
    jobject charset = env->CallStaticObjectMethod(charsetClass, forName, utf8);

// find a String constructor that takes a Charset:
//   javap -s java.lang.String | egrep -A2 "String\(.*charset"
    jclass stringClass = env->FindClass("java/lang/String");
    jmethodID ctor = env->GetMethodID(
            stringClass, "<init>", "([BLjava/nio/charset/Charset;)V");

    jstring jMessage = reinterpret_cast<jstring>(
            env->NewObject(stringClass, ctor, bytes, charset));

    return jMessage;
}

