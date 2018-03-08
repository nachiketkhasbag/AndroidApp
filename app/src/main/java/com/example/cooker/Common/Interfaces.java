package com.example.cooker.Common;



/**
 * Created by nachiket on 7/3/2017.
 */

public class Interfaces {

    public interface UserInterface {

        void UserSignedIn();

        void UserSignedOut();
    }

    public interface CartDataUpload{
        void DataUploadFinished();
    }
}
