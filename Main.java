package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static java.lang.StrictMath.*;

public class Main extends Application {
    private recorder r=new recorder();
    private byte[][] voice=new byte[10][recorder.hz*recorder.bit/8*recorder.STEREO];
    private byte[] voice2=new byte[recorder.hz*recorder.bit/8*recorder.STEREO];
    private byte[] ddata=new byte[2000];
    private NumberAxis x=new NumberAxis();
    private NumberAxis y=new NumberAxis();
    private XYChart.Series Series=new XYChart.Series();
    private XYChart.Series appSeries=new XYChart.Series();
    private LineChart<Number,Number> lineChart=new LineChart<Number,Number>(x,y);
    private int c=0;
    private FileOutputStream output;
    private FileInputStream input;
    private FileInputStream din;

    DFT d=new DFT(ddata.length,ddata);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(lineChart, 1900, 1800));
        lineChart.getData().add(Series);
        lineChart.getData().add(appSeries);
        Series.setName("an");appSeries.setName("bn");
        primaryStage.show();
        try
        {
            output = new FileOutputStream("output.dat");
            input=new FileInputStream("output.dat");
            din=new FileInputStream("am49.mp3");//32kbps
            din.skip(4);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        System.out.println("開始");
        for (int i = 0; i < 1; i++) {
            voice[i] = r.getVoice();

            /*for (byte b : voice[i])
                System.out.println(b);*/

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        r.stop();
        output.write(voice[0]);
        input.read(voice2);
        din.read(ddata);
        int j=0;

        System.out.println(j);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<ddata.length;i++) {
                    appSeries.getData().add(new XYChart.Data(i, ddata[i]));
                    System.out.println(ddata[i]);
                }

                /*for(int i=0;i<ddata.length/2;i++)
                    appSeries.getData().add(new XYChart.Data(i,sqrt(sq(d.an(i))+sq(d.bn(i)))));*/
            }
        });
        input.close();
        output.flush();
        output.close();
        System.out.println("終了");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop()
    {
        r.stop();
    }

    private double sq(double i)
    {
        return i*i;
    }
}
