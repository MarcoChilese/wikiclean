import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.wikiclean.WikiClean;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// clean compile assembly:single


class CleanTask implements Runnable{
    private File jsonfile;
    public CleanTask(File jsonfile) {
        this.jsonfile = jsonfile;
    }

    public void run(){
        try (FileReader reader = new FileReader(jsonfile)){
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonPage = (JSONObject)  jsonParser.parse(reader);

            JSONArray revisions = (JSONArray) jsonPage.get("Revision");
            WikiClean cleaner = new WikiClean.Builder().build();

            if (revisions != null) {
                revisions.stream().forEach((x) -> {
                    JSONObject rev = (JSONObject) x;
                    String content = cleaner.clean((String) rev.get("Text")).replaceAll("[^a-zA-Z ]", "").toLowerCase();

                    if (content == "")
                        jsonfile.delete();
                    else{
                        rev.remove("Text");
                        rev.put("Text", content);
                    }
                });
            }
            else {
                jsonfile.delete();
                return;
            }

            try (FileWriter file = new FileWriter(jsonfile.getParent()+"/W"+jsonfile.getName())) {
                jsonfile.delete();
                file.write(jsonPage.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

public class WikipediaMarkupRemover {
    public static void main(String[] args) {
        File dir = new File(args[0]);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));

        ExecutorService myExe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (File jsonfile : files) {
            myExe.execute(new CleanTask(jsonfile));


        }
        myExe.shutdown();
    }
}
