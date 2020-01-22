package lcwu.fyp.asistio.director;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

            if(value == null || value.equals("*")){
                userFiles = null;
            }
            else{
                userFiles = gson.fromJson(value, ListUserFile.class);
            }
        }
        catch (Exception e){
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
