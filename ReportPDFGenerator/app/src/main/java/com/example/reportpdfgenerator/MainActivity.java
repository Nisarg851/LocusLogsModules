package com.example.reportpdfgenerator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button generatePdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generatePdf = findViewById(R.id.generatePdf);

//        if(checkPermission()){
//            Toast.makeText(MainActivity.this,"Permission to download pdf is already granted!",Toast.LENGTH_SHORT).show();
//        }else{
//            requestPermission();
//        }

        generatePdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String[][] table = {
                        {"1000","parkededed","02-03-2001T00:00:00","BablaAbcdefghijklmnop rstuvwxyzBablaAbcdefghijklmnopqrstuvwxyzBablaAbcdefghijklmnopqrstuvwxyz","5min","5 km/hr","3 km/hr","8000"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"},
                        {"1","parked","2/3/2001","Babla","5min","5 km/hr","3 km/hr","8"}
                };
                ReportPDFGenerator report = new ReportPDFGenerator(MainActivity.this);
                report.generateReportPDF(table);
//                createPDF(table);
//                Toast.makeText(MainActivity.this,"Report generated",Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void createPDF(String[][] table){
//        PdfDocument pdfDocument = new PdfDocument();
//        Paint writeInPDF = new Paint();
//
//        int pageWidth=3500, pageHeight=2000, pageNumber=1;
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
//        PdfDocument.Page page =pdfDocument.startPage(pageInfo);
//
//        Canvas pageCanvas = page.getCanvas();
//
//        writeInPDF.setTextAlign(Paint.Align.CENTER);
//        writeInPDF.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
//        writeInPDF.setTextSize(70);
//        pageCanvas.drawText("Status Report", pageWidth/2, 270, writeInPDF);
//
//        writeInPDF.setStyle(Paint.Style.STROKE);
//        writeInPDF.setStrokeWidth(8);
//        pageCanvas.drawRect(20,380,pageWidth-20,470,writeInPDF);
//
//        writeInPDF.setTextAlign(Paint.Align.LEFT);
//        writeInPDF.setStyle(Paint.Style.FILL);
//
//        pageCanvas.drawText("ID",40,450,writeInPDF);
//        pageCanvas.drawText("Status",140,450,writeInPDF);
//        pageCanvas.drawText("Date Time",450,450,writeInPDF);
//        pageCanvas.drawText("Address",1000,450,writeInPDF);
//        pageCanvas.drawText("Duration",1300,450,writeInPDF);
//        pageCanvas.drawText("Top Speed",1700,450,writeInPDF);
//        pageCanvas.drawText("Avg",2200,450,writeInPDF);
//        pageCanvas.drawText("No. of Data Calculated",2500,450,writeInPDF);
//
//        pageCanvas.drawLine(120,380,120,470,writeInPDF);
//        pageCanvas.drawLine(430,380,430,470,writeInPDF);
//        pageCanvas.drawLine(980,380,980,470,writeInPDF);
//        pageCanvas.drawLine(1280,380,1280,470,writeInPDF);
//        pageCanvas.drawLine(1680,380,1680,470,writeInPDF);
//        pageCanvas.drawLine(2180,380,2180,470,writeInPDF);
//        pageCanvas.drawLine(2480,380,2480,470,writeInPDF);
//
//        pdfDocument.finishPage(page);
//
//        File file = new File(Environment.getExternalStorageDirectory(), "/Download/LocusLogs_Status_Report.pdf");
//
//        try {
//            pdfDocument.writeTo(new FileOutputStream(file));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        pdfDocument.close();
//    }
//
//    private boolean checkPermission(){
//        int permissionToWriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permissionToReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
//        return ((permissionToReadExternalStorage == PackageManager.PERMISSION_GRANTED) && (permissionToWriteExternalStorage == PackageManager.PERMISSION_GRANTED));
//    }
//
//    private void requestPermission(){
//        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 200){
//            if(grantResults.length > 0){
//                boolean writeStoragePermissionGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
//                boolean readStoragePermissionGranted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);
//
//                if(writeStoragePermissionGranted && readStoragePermissionGranted){
//                    Toast.makeText(MainActivity.this,"Permissions Granted!",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this,"Permissions Denied "+writeStoragePermissionGranted+" "+readStoragePermissionGranted,Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }
//    }
}