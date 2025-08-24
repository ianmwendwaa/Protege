//// Write C++ code here.
//#include <curl/curl.h>
//#include <string>
//#include <sstream>
//#include <cstring>
//#include <iomanip>
//#include <iostream>
//using namespace std;
//const string SMTP_HOST = "smtp.gmail.com";
//const long SMTP_PORT = 465;
//const string SENDER_EMAIL = "williamsian845@gmail.com";
//const string PASSWORD = "mznu fxge mwsr udpp";
//const string USERNAME = SENDER_EMAIL;
//const string RECEIVER_EMAIL = "ianmwendwa91e@gmail.com";
//const string SUBJECT = "Birthday tings";
//const string BODY = "Testing app_email_integration";
//
//struct upload_status{
//    int lines_read;
//    const string * email_data;
//};
//
//size_t  payload_src(void * ptr, size_t size, size_t  nmemb, void * userp){
//    auto upload_ctx = static_cast<upload_status*>(userp);
//    const char * data = upload_ctx->email_data->c_str();
//    size_t  chunk_size = size * nmemb;
//    size_t data_len = upload_ctx->email_data->length();
//    if (data_len == 0){
//        return 0;
//    }
//    size_t bytes_to_copy = min(chunk_size, data_len - upload_ctx->lines_read);
//    if (bytes_to_copy > 0){
//        memcpy(ptr, data + upload_ctx->lines_read, bytes_to_copy);
//        upload_ctx->lines_read += bytes_to_copy;
//    }
//    return bytes_to_copy;
//}
//
//string emailBuilder(const string &username, const string &to, const string &subject, const string &body){
//    ostringstream oss;
//
//    time_t now = time(nullptr);
//    tm * ltm = localtime(&now);
//
//    oss << "From: "<< username;
//    oss << "To: "<<to;
//    oss << "Subject: "<<subject;
//    oss << "Date: "<< put_time(ltm, "%a, %d %b %Y %H:%M:&S &z");
//
//    oss << "\r\n";
//
//    oss << body;
//
//    return oss.str();
//}
//int main(){
//    CURLcode res = CURLE_OK;
//    std::string full_email_content = emailBuilder(SENDER_EMAIL, RECEIVER_EMAIL, SUBJECT, BODY);
//
//    upload_status upload_ctx = {0, &full_email_content};
//    res = curl_global_init(CURL_GLOBAL_DEFAULT);
//
//    if(res != CURLE_OK){
//        cerr<<"curl_global_init() failed!"<<curl_easy_strerror(res);
//        return 1;//to signal error
//    }
//
//    //init curl
//    CURL * curl = curl_easy_init();
//
//    if(!curl){
//        cerr<<"curl_easy_init() failed!"<<curl_easy_strerror(res);
//    }else{
//        std::string smtp_url = "smtps://" + SMTP_HOST + ":" + std::to_string(465);
//
//        curl_easy_setopt(curl, CURLOPT_URL, smtp_url.c_str());
//
//        curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L);//for debugging
//
//        curl_easy_setopt(curl, CURLOPT_USERNAME, USERNAME.c_str());
//
//        curl_easy_setopt(curl, CURLOPT_PASSWORD, PASSWORD.c_str());
//
//        curl_easy_setopt(curl, CURLOPT_MAIL_FROM, SENDER_EMAIL.c_str());
//
//        curl_slist * recipients = nullptr;
//        recipients = curl_slist_append(recipients, RECEIVER_EMAIL.c_str());
//        curl_easy_setopt(curl, CURLOPT_MAIL_RCPT, recipients);
//
//        //signal libcurl we are uploading email data
//        curl_easy_setopt(curl, CURLOPT_UPLOAD, 1L);
//
//        curl_easy_setopt(curl, CURLOPT_READFUNCTION, payload_src);
//
//        curl_easy_setopt(curl, CURLOPT_READDATA, &upload_ctx);
//
//        curl_easy_setopt(curl, CURLOPT_INFILESIZE_LARGE, static_cast<curl_off_t>(full_email_content.length()));
//
//        curl_easy_setopt(curl, CURLOPT_TIMEOUT_MS, 30000L);
//
//        curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT_MS, 60000L);
//
//        res = curl_easy_perform(curl);
//
//        if(res != CURLE_OK){
//            cerr<<"curl_easy_perform() failed!"<<curl_easy_strerror(res);
//            return -1;
//        }
//
//        cerr<<"Email sent successfully!";
//
//        curl_slist_free_all(recipients);
//
//        curl_easy_cleanup(curl);
//    }
//    curl_global_cleanup();
//
//    return 0;
//}
//// Do not forget to dynamically load the C++ library into your application.
////
//// For instance,
////
//// In MainActivity.java:
////    static {
////       System.loadLibrary("notessqlite");
////    }
////
//// Or, in MainActivity.kt:
////    companion object {
////      init {
////         System.loadLibrary("notessqlite")
////      }
////    }