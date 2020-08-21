# ProgressUploader
Multipart Upload of text file. Tracks upload progress

This lib will work as is but it's more of an example of how this can be achieved so that you can implement your own code. For example you might want to upload non-text (binary) files instead of text files. Or you might want to upload files + parameters (there is a commented example in the code)

The lib posts your form data with a form tag 'payload' and a file name 'temp.txt'

This lib is meant for iOS and Android

This lib uses OKHTTP and CoreProgress Gradle dependecies as well as AFNetworking 3.0 Pod
# Usage
    OnComplete<CustomNetworkEvent> progressListener = CustomNetworkEvent -> {   
      Display.getInstance().callSerially(()->{
      //track your progress here with CustomNetworkEvent.progress
      });
    };
    OnComplete<CustomNetworkEvent> doneListener = CustomNetworkEvent -> {   
      Display.getInstance().callSerially(()->{
        //upload finished. response in CustomNetworkEvent.response
      });
    };
    OnComplete<CustomNetworkEvent> errorListener = CustomNetworkEvent -> {  
      Display.getInstance().callSerially(()->{
        //upload error. You might want to re-try
      });
    };
    com.javieranton.ProgressUploader.progressDoneCallback = doneListener;
    com.javieranton.ProgressUploader.progressCallback = progressListener;
    com.javieranton.ProgressUploader.progressErrorCallback = errorListener;
    com.javieranton.ProgressUploader.PostMultipart(url, fileContentString);
# Server-side example (Node.js + express)
You have to serve a multipart HTTP endpoint (MIME type "multipart/form-data"). This example posts one single text file but your server code will also be different if you decide to go ahead and modify this lib to post multiple files, string params, etc

    const Multer = require('multer');
    const multer = Multer({
    storage: Multer.memoryStorage(),
    limits: {
            fileSize: 100 * 1000 * 1000, // no larger than 100mb, you can change as needed.
        }
    });
    app.post('/YourEndpoint', multerNoMemory.single('payload'), (req, res) => {
        console.log(req.file.originalname);
        res.send({result:"ok"});
    });
