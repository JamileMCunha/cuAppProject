package com.cuApp.features.dashboard.profile;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuApp.MainActivity;
import com.cuApp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser usern;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;
    //path that will store profile photos
    String storagePath = "Users_Profile_Cover_Imgs/ ";

    //views from fragment profile
    ImageView avatarp;
    TextView namep, emailp, collegep, coursep, aboutp, intsubp;
    FloatingActionButton editprof;

    ProgressDialog pd;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 400;
    //arrays permission to be requested
    String cameraPermissions[];
    String storagePermissions[];

    private static final int PICK_IMAGE_REQUEST = 1;


    //uri of picked image
    Uri image_uri;

    //checking profile
    String profilePhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        usern = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        //init arrays permission
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //init views
        avatarp = view.findViewById(R.id.avatarp);
        namep = view.findViewById(R.id.namep);
        emailp = view.findViewById(R.id.emailp);
        collegep = view.findViewById(R.id.collegep);
        coursep = view.findViewById(R.id.coursep);
        aboutp = view.findViewById(R.id.aboutp);
        intsubp = view.findViewById(R.id.intsub);
        editprof = view.findViewById(R.id.editprof);


        //init progress dialog

        pd = new ProgressDialog(getActivity());

        //retriving user data through email
        Query query = databaseReference.orderByChild("email").equalTo(usern.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check required data
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String college = "" + ds.child("college").getValue();
                    String course = "" + ds.child("course").getValue();
                    String about = "" + ds.child("about").getValue();
                    String intsub = "" + ds.child("intsub").getValue();
                    String image = "" + ds.child("image").getValue();


                    namep.setText(name);
                    emailp.setText(email);
                    collegep.setText(college);
                    coursep.setText(course);
                    aboutp.setText(about);
                    intsubp.setText(intsub);
                    if (!TextUtils.isEmpty(image)) {
                        Picasso.get().load(image).into(avatarp);
                    } else {
                        Picasso.get().load(R.drawable.ic_default_photo).into(avatarp);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //handle button click
        editprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        return view;

    }

    private boolean checkStoragePermission (){
        //check is storage is enabled/not
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
            }
    private void requestStoragePermission () {
        //request running time permission
                requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
            }

    private boolean checkCameraPermission (){
        //check is storage is enabled/not
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }
    private void requestCameraPermission () {
        //request running time permission
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {

        String options[] = {"Edit Profile Picture", "Edit Name", "Edit About you", "Edit College", "Update your Course", "Updating Interest Subjects" };
            //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set Title
        builder.setTitle("Edit Profile");
        //set items
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle clicks
                if (which == 0) {
                    //edit profile click
                    pd.setMessage("Updating Profile Picture");
                    profilePhoto = "image";
                    showImagePicDialog();
                } else if (which == 1) {
                    //edit name click
                    pd.setMessage("Updating Name");
                    showOtherUpdates  ("name");
                } else if (which == 2) {
                    //edit about click
                    pd.setMessage("Updating About You");
                    showOtherUpdates  ("about");
                } else if (which == 3) {
                    //edit college click
                    pd.setMessage("Updating College");
                    showOtherUpdates  ("college");
                } else if (which == 4) {
                    //edit course click
                    pd.setMessage("Updating Course");
                    showOtherUpdates("course");
                } else if (which == 5) {
                        //edit interest click
                    pd.setMessage("Updating Interest");
                    showOtherUpdates("intsub");
                    }
            }
        });

        //create builder and show dialog
                builder.create().show();
    }
    private void showOtherUpdates(final String key){
//custom dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Update "+ key);
    //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
    //add edit text
       final EditText editText = new EditText(getActivity());
        editText.setHint("Enter "+ key);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        //buttons to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();
                //validade if user has entered something
                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(usern.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //updated, dismiss progress
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed, dismiss progress and show error
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), " "+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                } else
                {
                    Toast.makeText(getActivity(), "Please enter "+ key, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add button to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
         builder.create().show();
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set Title
        builder.setTitle("Choose Image From");
        //set items
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle clicks
                if (which == 0) {
                    //camera clicked

                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                        else {
                        pickFromCamera(); }

                } else if (which == 1) {
                    //gallery clicked

                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });

        //create builder
        builder.create().show();

    }

   public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       //method is called when user press Allow/Deny from permission request

       switch (requestCode){
           case CAMERA_REQUEST_CODE: {
               //pick from camera, first check if camera/storage permission was allowed/deny
               if (grantResults.length >0){
                   boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                   boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                   if (cameraAccepted  && writeStorageAccepted) {
                       //permission accepted
                       pickFromCamera();
                   }
                   else {
                       //permission denied
                       Toast.makeText(getActivity(), "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                   }
               }
           } break;
           case STORAGE_REQUEST_CODE: {

               if (grantResults.length >0){
                   boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                   if (writeStorageAccepted) {
                       //permission accepted
                       pickFromGallery();
                   }
                   else {
                       //permission denied
                       Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_SHORT).show();
                   }

               }

           } break;
       }

}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //method will be used after getting image from camera or gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE_REQUEST) {
                //image is picked from gallery, get uri of image
                image_uri = data.getData();


                uploadProfileCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE) {
                //image is picked from camera, get uri from image
                uploadProfileCoverPhoto(image_uri);
            }
        }
    }

    private void pickFromCamera() {

        //picking image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE);
    }
    private void pickFromGallery (){
    //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

    }

    public void onCreate (@Nullable Bundle savedInstanceState){
        setHasOptionsMenu(true);  //will show menu option on fragment
        super.onCreate (savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //inflating menu
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    //handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        pd.show();

        String filePathAndName = storagePath+ "" + profilePhoto+ "_" + usern.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri). addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image is uploaded to storage
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                if (uriTask.isSuccessful()){
                    //image uploaded
                    HashMap<String, Object> results = new HashMap<>();
                    results.put(profilePhoto, downloadUri.toString());

                    databaseReference.child(usern.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //url in DB of user is added, dismiss progress bar
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Image Updated..", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //error adding url in db
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Error Updating Image", Toast.LENGTH_SHORT).show();


                        }
                    });


                } else {
                    //error
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //show errors
                    pd.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    }
