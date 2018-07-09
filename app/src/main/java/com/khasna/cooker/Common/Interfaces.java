package com.khasna.cooker.Common;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


/**
 * Created by nachiket on 7/3/2017.
 */

public class Interfaces {

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

    public interface UpdateEmailInterface{
        void UpdateEmailComplete();

        void UpdateEmailFailed(String error);
    }

    public interface UpdatePasswordInterface{
        void UpdatePasswordSuccessful();

        void UpdatePasswordFailed(String error);
    }

    public interface ReadGuestDataInterface{
        void ReadComplete();

        void ReadFailed(DatabaseError databaseError);
    }

    public interface ReadActiveChefsInterface{
        void ReadComplete();

        void ReadFailed(String error);
    }

    public interface ReadChefItemsInterface{
        void ReadComplete();

        void ReadFailed(String error);
    }

    public interface DataUploadInterface{
        void UploadComplete();

        void UploadFailed(String error);
    }

    public interface ReadGuestHistoryInterface{
        void ReadComplete();

        void ReadFailed(String error);
    }

    public interface SignUpUserInterface{
        void SignUpComplete();

        void SignUpFailed(String error);
    }
}
