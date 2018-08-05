package com.khasna.cooker.Common;


import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


/**
 * Created by nachiket on 7/3/2017.
 */

public class Interfaces {

    public interface DownloadDP{
        void TaskComplete();

        void TaskFailed( String error );
    }

    public interface SignOutInterface{
        void TaskComplete();

        void TaskFailed( String error );
    }

    public interface UserInterface {

        void UserSignedIn();

        void UserSignedOut();
    }

    public interface AppUserInterface{

        void UserSignedIn();

        void UserSignedOut();
    }

    public interface DataBaseReadInterface{
        void ReadSucceeded(DataSnapshot dataSnapshot);

        void ReadFailed(DatabaseError databaseError);
    }

    public interface ReadChefItemsInterface{
        void ReadComplete();

        void ReadFailed(String error);
    }

    public interface DataUploadInterface{
        void UploadComplete();

        void UploadFailed(String error);
    }
}
