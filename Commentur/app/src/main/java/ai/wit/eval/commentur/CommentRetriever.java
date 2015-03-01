package ai.wit.eval.commentur;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rmvl on 3/1/2015.
 */
public class CommentRetriever extends AsyncTask<Void, Void, JSONObject[]> {

    public static String AUTHORIZATION = "Authorization";
    public static String CLIENT_ID = "Client-ID abc48193b42c886";
    public static String DATA = "data";
    public static String BASE_URL_IMAGE = "https://api.imgur.com/3/gallery/";
    public static String END_URL_IMAGE ="/comments/ids";
    public static String BASE_URL_COMMENTS = "https://api.imgur.com/3/comment/";
    public static String AUTHOR = "author";
    public static String COMMENT = "comment";
    public static final String ip = "172.20.10.2";
    public static final int port = 13337;
    public static final int N = 50;

    private Context mContext;
    private CustomArrayAdapter mAdapter;
    private ListView mComments;

    public CommentRetriever (Context context, CustomArrayAdapter adapter, ListView commentsList){
        mContext = context;
        mAdapter = adapter;
        mComments = commentsList;
    }

    @Override
    protected JSONObject[] doInBackground(Void... params) {
        String id = "";
        try {
            Socket clientSocket = new Socket(ip, port);
            //Input stream from socket
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String url = inFromServer.readLine();
            id = url;
        }
        catch(IOException e) {System.out.println("IO Exception, socket could not be created");}

        try {
            //Fetch the list of all comment ids from the gallery
            //What if there are no comments?
            JSONArray commentIds = getCommentIds(id);
            //For each comment id, fetch its information
            JSONObject[] comments = getCommentsAsJson(commentIds);
            //Publish to listview via onPostExecute
            return comments;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONArray getCommentIds(String id) {
        try {
            System.out.println(BASE_URL_IMAGE+id+END_URL_IMAGE);
            URL url = new URL(BASE_URL_IMAGE+id+END_URL_IMAGE);
            HttpsURLConnection connection = getHttpsURLConnection(url);
            String jsonString = getUrlStreamString(connection);
            JSONObject commentsJson = new JSONObject(jsonString);
            return commentsJson.getJSONArray(DATA);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject[] getCommentsAsJson(JSONArray ids) {
        try {
            JSONObject[] comments = new JSONObject[ids.length()];
            for (int i = 0; i < N; i++) {
                URL url = new URL(BASE_URL_COMMENTS + ids.optInt(i)); //should this be getInt?
                HttpsURLConnection connection = getHttpsURLConnection(url);
                String jsonComment = getUrlStreamString(connection);
                comments[i] = new JSONObject(jsonComment);
            }
            return comments;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpsURLConnection getHttpsURLConnection(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty(AUTHORIZATION, CLIENT_ID);
        return connection;
    }

    private String getUrlStreamString(HttpsURLConnection connection) {
        try {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            /*inputStream.close();
            bufferedReader.close();*/
            return sb.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void onPostExecute(JSONObject[] commentsJson) {
        //Update the UI (listview)
        System.out.println("DLS "+commentsJson.length);
        List<String> authors = new ArrayList<String>();
        List<String> comments = new ArrayList<String>();
        try {
            for (int i = 0; i < commentsJson.length; i++) {
                if(commentsJson[i] == null) System.out.println("null error");
                //authors.add(commentsJson[i].getString(AUTHOR));
                //System.out.println(commentsJson[i].getString(AUTHOR));
                else {comments.add(commentsJson[i].getString(COMMENT));
                System.out.println(commentsJson[i].getString(COMMENT));}
            }
            mAdapter = new CustomArrayAdapter(mContext, authors, comments);
            mComments.setAdapter(mAdapter);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
