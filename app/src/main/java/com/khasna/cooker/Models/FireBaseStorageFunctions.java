package com.khasna.cooker.Models;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.khasna.cooker.Common.Interfaces;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.ChefsListForGuest;

import java.io.File;

public class FireBaseStorageFunctions<G extends Collection> {

    private G mFireBaseStorageFunctionsGeneric;
    //Firebase
    private FirebaseStorage storage;
    private StorageReference storageReference;
    final String dp_path = "/images/dp/dp";


    FireBaseStorageFunctions(G g){
        mFireBaseStorageFunctionsGeneric = g;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private static final String className = "FireBaseStorageFunctions";

    //    public void DownloadDP(Uri filepath, final Context context)
    public void DownloadDP(final File storageDir, final Interfaces.DownloadDP downloadDP)
    {
        for (final ChefsListForGuest chefsListForGuest: mFireBaseStorageFunctionsGeneric.GetChefsListForGuest())
        {
            String uid = chefsListForGuest.getuID();
            StorageReference pathReference = storageReference.child(uid + dp_path);

            try {
                final File localFile = File.createTempFile("image", "jpg", storageDir);

                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        chefsListForGuest.setUriProfilePic(Uri.parse(localFile.getAbsolutePath()));
                        downloadDP.TaskComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        System.out.print("Failed");
                        System.out.print("Image does not exist");
                        downloadDP.TaskFailed(exception.getMessage());
                    }
                });
            }
            catch ( Exception e){
                e.printStackTrace();
                System.out.print("Image does not exist");
            }
        }
    }

}