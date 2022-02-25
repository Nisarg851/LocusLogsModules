package com.example.reportpdfgenerator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;

public class ReportPDFGenerator extends AppCompatActivity {

    MainActivity parentActivity;

    // A4 size in pixel 2480x3500
    private int pageWidth=2480, pageHeight=3500, pageNumber=1;

    private PdfDocument pdfDocument;
    private Canvas pageCanvas;
    private PdfDocument.Page page;
    private Paint writeInPDF;

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getDefaultCellHeight() {
        return defaultCellHeight;
    }

    public void setDefaultCellHeight(int defaultCellHeight) {
        this.defaultCellHeight = defaultCellHeight;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    private int textSize=60, titleSize=70;

    private int cellHeight=100,defaultCellHeight=100;
    private int marginLeft=10;
    private int cellWidthForId= (int) Math.round((pageWidth*1.25)/100), //1.25
            cellWidthForStatus= (int) Math.round((pageWidth*5)/100), //5
            cellWidthForDateTime= (int) Math.round((pageWidth*14.583)/100), //14.583
            cellWidthForAddress= (int) Math.round((pageWidth*28.75)/100), //28.75
            cellWidthForDuration= (int) Math.round((pageWidth*58.75)/100), //58.75
            cellWidthTopSpeed= (int) Math.round((pageWidth*70.4167)/100), //70.4167
            cellWidthForAverage= (int) Math.round((pageWidth*83.33)/100), //83.33
            cellWidthForDataCal= (int) Math.round((pageWidth*93.33)/100); //93.33

    private int textPositionForID=20,
            textPositionForStatus=cellWidthForStatus+marginLeft,
            textPositionForDateTime=cellWidthForDateTime+marginLeft,
            textPositionForAddress=cellWidthForAddress+marginLeft,
            textPositionForDuration=cellWidthForDuration+marginLeft,
            textPositionForTopSpeed=cellWidthTopSpeed+marginLeft,
            textPositionForAverage=cellWidthForAverage+marginLeft,
            textPositionForDataCollected=cellWidthForDataCal+marginLeft;

    private int cellPadding=10;

    private int rowPositionLeft,
            rowPositionTop,
            rowPositionRigth,
            rowPositionBottom;

    public ReportPDFGenerator(MainActivity mainActivity) {
        this.parentActivity = mainActivity;
        if(checkPermission()){
            Toast.makeText(parentActivity,"Permission to download pdf is already granted!",Toast.LENGTH_SHORT).show();
        }else{
            requestPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void generateReportPDF(String[][] reportData){
        pdfDocument = new PdfDocument();
        writeInPDF = new Paint();

        page = createPage(pageWidth,pageHeight,pageNumber);

        int totalReportData = reportData.length;
        if(totalReportData>0){
            for(int record=0; record<totalReportData; record++){
                writeInRowCells(reportData[record][0],
                        reportData[record][1],
                        reportData[record][2],
                        reportData[record][3],
                        reportData[record][4],
                        reportData[record][5],
                        reportData[record][6],
                        reportData[record][7]);
                drawRow();
                cellHeight=defaultCellHeight;
            }
        }else{
            Toast.makeText(parentActivity,"No record of data found",Toast.LENGTH_LONG).show();
        }
        pdfDocument.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory(), "/Download/LocusLogs_Status_Report.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(parentActivity,"Report Generated",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private PdfDocument.Page createPage(int pageWidth, int pageHeight, int pageNumber){
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        pageCanvas = page.getCanvas();

        writeInPDF.setTextAlign(Paint.Align.CENTER);
        writeInPDF.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        writeInPDF.setTextSize(titleSize);
        pageCanvas.drawText("Locus Logs: Status Report", pageWidth/2, 270, writeInPDF);

        writeInPDF.setStrokeWidth(8);
        rowPositionLeft = 20;
        rowPositionTop = 350;
        rowPositionRigth = pageWidth-20;
        rowPositionBottom = 450;

        writeInPDF.setTextAlign(Paint.Align.LEFT);
        writeInPDF.setStyle(Paint.Style.FILL);

        writeInRowCells("ID","Status","Date Time","Address","Duration","Top Speed","Avg","TDC");
        drawRow();

        return page;
    }

    private void drawRow(){
        writeInPDF.setStyle(Paint.Style.STROKE);
        writeInPDF.setStrokeWidth(8);
        pageCanvas.drawRect(rowPositionLeft,rowPositionTop,rowPositionRigth,rowPositionBottom,writeInPDF);
        rowPositionTop = rowPositionBottom;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeInRowCells(String id, String status, String dateTime, String address, String duration, String topSpeed, String average, String dataCollected){

        if(rowPositionTop>(pageHeight-200)){
            pageNumber += 1;
            pdfDocument.finishPage(page);
            page = createPage(pageWidth,pageHeight,pageNumber);
        }

        writeInPDF.setTextSize(textSize);
        writeInPDF.setTextAlign(Paint.Align.LEFT);
        writeInPDF.setStyle(Paint.Style.FILL);

        adjustRowHeightAndWrite("ID",id,(cellWidthForStatus-cellWidthForId)/33);
        adjustRowHeightAndWrite("Status",status,(cellWidthForDateTime-cellWidthForStatus)/33);
        adjustRowHeightAndWrite("Date Time",dateTime,(cellWidthForAddress-cellWidthForDateTime)/33);
        adjustRowHeightAndWrite("Address",address,(cellWidthForDuration-cellWidthForAddress)/33);
        adjustRowHeightAndWrite("Duration",duration,(cellWidthTopSpeed-cellWidthForDuration)/33);
        adjustRowHeightAndWrite("Top Speed",topSpeed,(cellWidthForAverage-cellWidthTopSpeed)/33);
        adjustRowHeightAndWrite("Avg",average,(cellWidthForDataCal-cellWidthForAverage)/33);
        adjustRowHeightAndWrite("TDC",dataCollected,(rowPositionRigth-cellWidthForDataCal)/33);

        pageCanvas.drawLine(cellWidthForStatus, rowPositionTop, cellWidthForStatus, rowPositionBottom, writeInPDF);
        pageCanvas.drawLine(cellWidthForDateTime, rowPositionTop, cellWidthForDateTime, rowPositionBottom, writeInPDF);
        pageCanvas.drawLine(cellWidthForAddress, rowPositionTop, cellWidthForAddress, rowPositionBottom, writeInPDF);
        pageCanvas.drawLine(cellWidthForDuration, rowPositionTop, cellWidthForDuration, rowPositionBottom, writeInPDF);
        pageCanvas.drawLine(cellWidthTopSpeed, rowPositionTop, cellWidthTopSpeed, rowPositionBottom, writeInPDF);
        pageCanvas.drawLine(cellWidthForAverage, rowPositionTop, cellWidthForAverage, rowPositionBottom, writeInPDF);
        pageCanvas.drawLine(cellWidthForDataCal, rowPositionTop, cellWidthForDataCal, rowPositionBottom, writeInPDF);
    }

    private void adjustRowHeightAndWrite(String field,String string,int charCapacityForCell){
        int cellYPosition = 6;
        int sizeOfString = string.length();
        for(int subString=0; subString<=sizeOfString; subString+=charCapacityForCell){
            if(subString<sizeOfString && subString+charCapacityForCell>=sizeOfString){
                pageCanvas.drawText(string.substring(subString,sizeOfString),getTextPosition(field),rowPositionTop+cellYPosition*cellPadding,writeInPDF);
                break;
            }
            String str = string.substring(subString,subString+charCapacityForCell);
            String hiphen = string.charAt(subString+charCapacityForCell)==' '||string.charAt(subString+charCapacityForCell)==',' ? "" : "-";
            pageCanvas.drawText(str+hiphen,getTextPosition(field),rowPositionTop+cellYPosition*cellPadding,writeInPDF);
            cellHeight = Math.max(cellHeight,cellHeight+60);
            cellYPosition += 6;
        }
        rowPositionBottom = rowPositionTop + cellHeight;
    }

    private int getTextPosition(String field){
        if(field=="ID") return textPositionForID;
        if(field=="Status") return textPositionForStatus;
        if(field=="Date Time") return textPositionForDateTime;
        if(field=="Address") return textPositionForAddress;
        if(field=="Duration") return textPositionForDuration;
        if(field=="Top Speed") return textPositionForTopSpeed;
        if(field=="Avg") return textPositionForAverage;
        return textPositionForDataCollected;
    }

    private boolean checkPermission(){
        int permissionToWriteExternalStorage = ContextCompat.checkSelfPermission(parentActivity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionToReadExternalStorage = ContextCompat.checkSelfPermission(parentActivity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return ((permissionToReadExternalStorage == PackageManager.PERMISSION_GRANTED) && (permissionToWriteExternalStorage == PackageManager.PERMISSION_GRANTED));
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(parentActivity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200){
            if(grantResults.length > 0){
                boolean writeStoragePermissionGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                boolean readStoragePermissionGranted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);

                if(writeStoragePermissionGranted && readStoragePermissionGranted){
                    Toast.makeText(parentActivity,"Permissions Granted!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(parentActivity,"Permissions Denied "+writeStoragePermissionGranted+" "+readStoragePermissionGranted,Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
