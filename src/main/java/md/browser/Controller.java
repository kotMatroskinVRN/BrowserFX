package md.browser;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class Controller implements Initializable{

    @FXML
    private WebView webView;
    @FXML
    private TextField textField;

    private WebEngine engine;
    private WebHistory history;
    private double webZoom;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        engine = webView.getEngine();
        engine.setJavaScriptEnabled(false);

        String homePage = "./";
        textField.setText(homePage);
        webZoom = 1;
        loadPage();
    }

    public void loadPage() {

        String text = textField.getText();
        File file = new File( text );

        if(file.isFile()) loadFile(file);
        if(file.isDirectory()) loadDirectory(file);
        //else loadWeb(text);
        else load404(file);
    }

    private void load404(File file) {
        String content = resource2string("notFound.html");

        String filePath = String.valueOf(file.toURI()).replaceAll( "file:.*file:" , "file:" );
        content = content.replaceAll( "REPLACE_FILE_NAME" , filePath);
        engine.loadContent(content);
    }


    public void refreshPage() {
        engine.reload();
    }

    public void zoomIn() {
        webZoom+=0.25;
        webView.setZoom(webZoom);
    }

    public void zoomOut() {
        webZoom-=0.25;
        webView.setZoom(webZoom);
    }

    public void back() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        if(history.getCurrentIndex()>0 ) history.go(-1);
        textField.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

    public void forward() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        if(history.getCurrentIndex()<entries.size()-1){
            System.out.println("current : " + history.getCurrentIndex());
            System.out.println("history : " + entries.size());

            history.go(1);

        }
        textField.setText(entries.get(history.getCurrentIndex()).getUrl());
    }








    private void loadFile(File file){

        try {
            engine.load(String.valueOf(file.toURI().toURL()));
            System.out.println("file");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO 404
        }

    }
    private void loadWeb(String text){
        engine.load("http://"+text);
        System.out.println("web");
    }

    private void loadDirectory(File file){

        try {
            List<File> files;
            files = Arrays.asList(Objects.requireNonNull(file.listFiles(f -> f.getName().endsWith("html"))));
            if(files.size()>0) {
                String index = "index.html";
                File indexFile = new File(index);
                if (!files.contains(indexFile)) {
                    indexFile = files.stream().max(File::compareTo).get();
                }
                String text = String.valueOf(indexFile.toURI().toURL());
                engine.load( text );
                textField.setText( text );
            }

            System.out.println("dir");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO 404
        }
    }

    private String resource2string(String resourceName){
        StringBuilder result = new StringBuilder();

        try{
            InputStream is = ClassLoader.getSystemResourceAsStream(resourceName) ;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = reader.readLine();
            while(line != null){
                result.append(line).append("\n");
                line = reader.readLine();
            }
        }
        catch (IOException e) { System.out.printf("%s  - %s\n" , resourceName , "resource not found"  );
            System.exit(1);
            return "";}

        return result.toString() ;
    }

}
