package lcwu.fyp.asistio.director;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.User;
import lcwu.fyp.asistio.model.UserFile;

public class Session {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public Session(Context c){
        context = c;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        gson = new Gson();
    }

    public void setSession(User user){
        String value = gson.toJson(user);
        editor.putString("user", value);
        editor.commit();
    }

    public void setFiles(ListUserFile userFiles){
        Log.e("ListUserFile", "List Size from JSON: " + userFiles.getUserFiles().size());
        String value = gson.toJson(userFiles);
        editor.putString("userFiles", value);
        editor.commit();
    }

    public void destroySession(){
        editor.remove("user");
        editor.commit();
    }

    public User getUser(){
        User user;
        try{

            String value = preferences.getString("user", "*");

            if(value == null || value.equals("*")){
                user = null;
            }
            else{
                user = gson.fromJson(value, User.class);
            }
        }
        catch (Exception e){
            user = null;
        }
        return user;
    }

    public ListUserFile getUserFiles(){
        ListUserFile userFiles;
        try{
            String value = preferences.getString("userFiles", "*");
            Log.e("ListUserFile", "List Size from Session inside try");

            if(value == null || value.equals("*")){
                Log.e("ListUserFile", "Value is null");
                userFiles = null;
            }
            else{
                Log.e("ListUserFile", "InSide If");
                userFiles = gson.fromJson(value, ListUserFile.class);
                Log.e("ListUserFile", "List Size from JSON: " + userFiles.getUserFiles().size());
            }
        }
        catch (Exception e){
            Log.e("ListUserFile", "Exception occur: " + e.getMessage());
            userFiles = null;
        }
        return userFiles;
    }

    public void setSync(boolean flag){
        editor.putBoolean("sync", flag);
        editor.commit();
    }

    public boolean getSync(){
        return preferences.getBoolean("sync", false);
    }




}
