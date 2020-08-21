#import "com_javieranton_ProgressUploaderNativeInterfaceImpl.h"
#include "com_javieranton_ProgressUploader.h" 
#import "AFNetworking.h"

@implementation com_javieranton_ProgressUploaderNativeInterfaceImpl

-(void)PostMultipart:(NSString*)param param1:(NSString*)param1{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    [manager.responseSerializer setAcceptableContentTypes:[NSSet setWithArray:@[@"application/json"]]];

    [manager setTaskDidSendBodyDataBlock:^(NSURLSession *session, NSURLSessionTask *task, int64_t bytesSent, int64_t totalBytesSent, int64_t totalBytesExpectedToSend)
     {
             double percentDone = (double)totalBytesSent / totalBytesExpectedToSend;
             NSString *percentDoneString = [NSString stringWithFormat:@"%.2f", percentDone];

             JAVA_OBJECT str = fromNSString(CN1_THREAD_GET_STATE_PASS_ARG percentDoneString);
             com_javieranton_ProgressUploader_progress___java_lang_String(CN1_THREAD_GET_STATE_PASS_ARG str);
     }];
    
    [manager POST:param parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        
        [formData appendPartWithFileData:[param1 dataUsingEncoding:NSUTF8StringEncoding] name:@"payload" fileName:@"temp.txt" mimeType:@"text/plain"]; // you file to upload
        //example of how to add text params
        /*[formData appendPartWithFormData:[@"some text" dataUsingEncoding:NSUTF8StringEncoding]
                                    name:@"title"];*/
        
    } progress:^(NSProgress * _Nonnull uploadProgress){

           } success:^(NSURLSessionDataTask *task, id responseObject) {
                if(!responseObject)
                {
                    com_javieranton_ProgressUploader_progressError__(CN1_THREAD_GET_STATE_PASS_SINGLE_ARG);
                }
                else
                {
                    //commented parsing of response {'result':'ok'}
                    //returning simple string, feel free to modify
                    //NSDictionary *jsonDict = (NSDictionary *) responseObject;
                    //NSString *res = [NSString stringWithFormat:@"%@", [jsonDict objectForKey:@"result"]];
                    //JAVA_OBJECT str = fromNSString(CN1_THREAD_GET_STATE_PASS_ARG [NSString stringWithFormat:@"%@%@%@",@"{'result':'",res,@"'}"]);
                    JAVA_OBJECT str = fromNSString(CN1_THREAD_GET_STATE_PASS_ARG @"success");
                    com_javieranton_ProgressUploader_progressDone___java_lang_String(CN1_THREAD_GET_STATE_PASS_ARG str);
                }
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
            com_javieranton_ProgressUploader_progressError__(CN1_THREAD_GET_STATE_PASS_SINGLE_ARG);
    }];
}

-(BOOL)isSupported{
    return YES;
}

@end
