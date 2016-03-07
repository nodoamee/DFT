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
    private byte[] ddata=new byte[2848];
    private NumberAxis x=new NumberAxis();
    private NumberAxis y=new NumberAxis();
    private XYChart.Series Series=new XYChart.Series();
    private XYChart.Series appSeries=new XYChart.Series();
    private LineChart<Number,Number> lineChart=new LineChart<Number,Number>(x,y);
    private int c=0;
    private FileOutputStream output;
    private FileInputStream input;
    private FileInputStream din;
    //DFT d=new DFT(ddata.length,ddata);
    public final int smpf=1000;
    double [] s=new double[smpf];
    DFT d=new DFT(s.length,s);

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
        /*for(int i=5;i>=0;i--) {
            System.out.println(i);
            try{
                Thread.sleep(1000);
            }catch(Exception e){}
        }*/
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

        for(int i=0;i<s.length;i++)
        {
            s[i]=DFT.F((double)i/smpf);
        }
        
        /*for(double i=0;i<(double)1/DFT.f;i+=(double)1/(smpf*DFT.f)) {
            s[j] =  DFT.F(i);
            j++;
            //System.out.println(s[j]);
        }*/
        System.out.println(j);
        //System.out.println("強度"+DFT.ck(voice2.length,voice2,330));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                /*for (int i = 0; i < s.length; i++)
                    appSeries.getData().add(new XYChart.Data(i, s[i]));*/
                for(int i=0;i<s.length/2;i++) {
                    Series.getData().add(new XYChart.Data(i, sqrt(d.an(i) * d.an(i) + d.bn(i) * d.bn(i))));
                }
                    /*Series.getData().add(new XYChart.Data(i, d.an(i)));
                    appSeries.getData().add(new XYChart.Data(i, d.bn(i)));*/
                /*for(int i=0;i<voice2.length;i++)
                    Series.getData().add(new XYChart.Data(i,voice2[i]));*/
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
}
