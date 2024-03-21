package edu.fvtc.teams;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileIO {
    public static final String TAG = "FileIOMethods";

    public void writeFile(String filename,
                          AppCompatActivity activity,
                          String[] items)
    {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(activity.openFileOutput(filename, Context.MODE_PRIVATE));
            String line = "";

            for(int counter = 0; counter < items.length; counter++)
            {
                line = items[counter];
                if(counter < items.length - 1)
                    line += "\r\n";
                writer.write(line);
                Log.d(TAG, "writeFile: " + line);
            }
            writer.close();

        } catch (FileNotFoundException e) {
            Log.d(TAG, "writeFile: FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "writeFile: IOException: " + e.getMessage());
        } catch(Exception e)
        {
            Log.i(TAG, "writeFile: " + e.getMessage());
        }
    }

    public ArrayList<String> readFile(String filename,
                                      AppCompatActivity activity)
    {
        ArrayList<String> items = new ArrayList<String>();

        try{
            InputStream inputStream = activity.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                items.add(line);
            }

            bufferedReader = null;
            inputStreamReader.close();
            inputStream.close();


        }catch(Exception e) {
            Log.d(TAG, "readFile: " + e.getMessage());
        }
        return items;
    }

    public ArrayList<Team> ReadFromXMLFile(String filename,
                                           AppCompatActivity activity)
    {
        ArrayList<Team> teams = new ArrayList<Team>();
        Log.d(TAG, "ReadFromXMLFile: Start");

        try{
            InputStream inputStream = activity.openFileInput(filename);
            XmlPullParser xmlPullParser = Xml.newPullParser();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            xmlPullParser.setInput(inputStreamReader);
            Log.d(TAG, "ReadFromXMLFile: Pre While");
            while(xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                Log.d(TAG, "ReadFromXMLFile: END_DOCUMENT");
                if(xmlPullParser.getEventType() == XmlPullParser.START_TAG)
                {
                    Log.d(TAG, "ReadFromXMLFile: START_TAG");
                    if(xmlPullParser.getName().equals("Team"))
                    {
                        Log.d(TAG, "ReadFromXMLFile: Start Team Parsing");
                        int id = Integer.parseInt(xmlPullParser.getAttributeValue(null, "id"));
                        String name = xmlPullParser.getAttributeValue(null, "name");
                        String city = xmlPullParser.getAttributeValue(null, "city");
                        String cellphone = xmlPullParser.getAttributeValue(null, "cellphone");
                        Float rating = Float.valueOf(xmlPullParser.getAttributeValue(null, "rating"));
                        Team team = new Team();
                        teams.add(team);
                        Log.d(TAG, "ReadFromXMLFile: " + team.toString());
                    }
                }
                xmlPullParser.next();
            }
            inputStreamReader.close();
            inputStream.close();

        }
        catch(Exception e)
        {
            Log.d(TAG, "ReadFromXMLFile: " + e.getMessage());
        }

        Log.d(TAG, "ReadFromXMLFile: End");
        return teams;
    }

    public void WriteXMLFile(String filename,
                             AppCompatActivity activity,
                             ArrayList<Team> Teams)
    {
        try
        {
            Log.d(TAG, "WriteXMLFile: Start");
            XmlSerializer serializer = Xml.newSerializer();
            File file = new File(filename);
            OutputStreamWriter outputStreamWriter = null;

            outputStreamWriter = new OutputStreamWriter(activity.getApplicationContext().openFileOutput(filename,
                    Context.MODE_PRIVATE));

            serializer.setOutput(outputStreamWriter);

            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "Teams");
            serializer.attribute("", "number", String.valueOf(Teams.size()));

            for(Team Team : Teams)
            {
                serializer.startTag("", "Team");
                serializer.attribute("", "id", String.valueOf(Team.getId()));
                serializer.attribute("", "name", String.valueOf(Team.getName()));
                serializer.attribute("", "city", String.valueOf(Team.getCity()));
                serializer.attribute("", "cellphone", String.valueOf(Team.getCellPhone()));
                serializer.attribute("", "rating", String.valueOf(Team.getRating()));
                serializer.endTag("", "Team");
                Log.d(TAG, "WriteXMLFile: " + Team.toString());
            }

            serializer.endTag("", "Teams");
            serializer.endDocument();
            serializer.flush();
            outputStreamWriter.close();
            Log.d(TAG, "WriteXMLFile: Wrote " + Teams.size());
            Log.d(TAG, "WriteXMLFile: End");
        }
        catch(Exception e)
        {
            Log.d(TAG, "WriteXMLFile: " + e.getMessage());
        }


    }
}
