package lcwu.fyp.asistio.director;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.LastLocation;

public class Helpers {
    private ValueEventListener listener;

    public boolean isConnected(Context c) {
        boolean connected = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo
                (ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

    public void showError(Activity a, String title, String message) {
        new FancyGifDialog.Builder(a)
                .setTitle(title)
                .setMessage(message)
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.failure)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .build();
    }

    public void showSuccess(Activity a, String title, String message) {
        new FancyGifDialog.Builder(a)
                .setTitle(title)
                .setMessage(message)
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.success)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        a.finish();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        a.finish();
                    }
                })
                .build();
    }

    public void saveLastLocation(LastLocation lastLocation, String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location").child(id);
        Log.e("SaveLastLocation", "User Id: " + id);
        Log.e("SaveLastLocation", "Time Stamps: " + lastLocation.getTimeStamps());

        List<LastLocation> locations = new ArrayList<>();
        boolean flag = false;
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listener != null)
                    reference.removeEventListener(listener);
                Log.e("SaveLastLocation", "Database Success: " + dataSnapshot.toString());
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Log.e("SaveLastLocation", "Location is: " + d.toString());
                    LastLocation location = d.getValue(LastLocation.class);
                    if (location != null)
                        locations.add(location);
                }
                locations.add(lastLocation);
                if (locations.size() > 15) {
                    List<LastLocation> updatedList = new ArrayList<>();
                    for (int i = locations.size() - 1; i >= locations.size() - 15; i--) {
                        updatedList.add(locations.get(i));
                    }
                    Collections.reverse(updatedList);
                    reference.setValue(updatedList);
                } else {
                    reference.setValue(locations);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SaveLastLocation", "Database Error: " + databaseError.getMessage());

                if (listener != null)
                    reference.removeEventListener(listener);
            }
        };

        reference.addValueEventListener(listener);
    }
}
