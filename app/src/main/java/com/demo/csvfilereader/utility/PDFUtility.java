package com.demo.csvfilereader.utility;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.CsvFileViewerActivity;
import com.demo.csvfilereader.activity.SavedPdflistActivity;
import com.demo.csvfilereader.commons.CustomProgressDialogue;
import com.demo.csvfilereader.model.Conversion;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class PDFUtility {
    public static void createPDF(CsvFileViewerActivity csvConversionsActivity, String str, ArrayList<List<String>> arrayList, Rectangle rectangle) {
        new CreatePDF(csvConversionsActivity, str, arrayList, rectangle).execute(new String[0]);
    }


    public static class CreatePDF extends AsyncTask<String, Integer, String> {
        CsvFileViewerActivity context;
        ArrayList<List<String>> csv_data;
        CustomProgressDialogue customProgressDialog;
        boolean pdf_file_created = false;
        String pdf_file_path;
        String pdf_name;
        Rectangle pdf_page_size;

        public CreatePDF(CsvFileViewerActivity csvConversionsActivity, String str, ArrayList<List<String>> arrayList, Rectangle rectangle) {
            this.context = null;
            this.csv_data = null;
            this.pdf_file_path = null;
            this.pdf_name = null;
            this.pdf_page_size = null;
            this.context = csvConversionsActivity;
            this.csv_data = arrayList;
            this.pdf_name = str;
            this.pdf_page_size = rectangle;
            String valueOf = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + File.separator + csvConversionsActivity.getString(R.string.app_name)));
            String fileName = getFileName(valueOf, str, ".pdf");
            this.pdf_name = fileName;
            if (!new File(valueOf).exists()) {
                new File(valueOf).mkdirs();
            }
            this.pdf_file_path = valueOf + "/" + fileName;
            this.customProgressDialog = new CustomProgressDialogue(this.context);
        }

        @Override
        public void onPreExecute() {
            this.customProgressDialog.show();
        }

        @Override
        public String doInBackground(String... strArr) {
            try {
                Document document = new Document(this.pdf_page_size);
                PdfWriter.getInstance(document, new FileOutputStream(this.pdf_file_path));
                document.open();
                PdfPTable pdfPTable = new PdfPTable(this.csv_data.get(0).size());
                pdfPTable.setWidthPercentage(100.0f);
                int size = this.csv_data.size();
                for (int i = 0; i < size; i++) {
                    List<String> list = this.csv_data.get(i);
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        PdfPCell pdfPCell = new PdfPCell(new Phrase(list.get(i2)));
                        pdfPCell.setPadding(5.0f);
                        pdfPCell.setHorizontalAlignment(1);
                        pdfPCell.setVerticalAlignment(5);
                        pdfPTable.addCell(pdfPCell);
                    }
                }
                document.add(pdfPTable);
                document.close();
                this.pdf_file_created = true;
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                this.pdf_file_created = false;
                return null;
            }
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            CustomProgressDialogue customProgressDialogue = this.customProgressDialog;
            if (customProgressDialogue != null && customProgressDialogue.isShowing()) {
                this.customProgressDialog.dismiss();
            }
            if (this.pdf_file_created) {
                Conversion conversion = new Conversion();
                conversion.CONVERTED_FILE_PATH = this.pdf_file_path;
                conversion.CONVERTED_FILE_NAME = this.pdf_name;
                ArrayList<Conversion> convertedFiles = CsvFileViewerActivity.getConvertedFiles(this.context);
                convertedFiles.add(conversion);
                CsvFileViewerActivity.saveConvertedFiles(this.context, convertedFiles);
                viewPDF(this.pdf_file_path);
            }
        }

        private void viewPDF(final String str) {
            final Dialog dialog = new Dialog(this.context, R.style.TransparentBackground);
            dialog.setContentView(R.layout.deletedialog);
            TextView textView = (TextView) dialog.findViewById(R.id.dialogtitletxt);
            TextView textView2 = (TextView) dialog.findViewById(R.id.txtYes);
            TextView textView3 = (TextView) dialog.findViewById(R.id.txtNo);
            textView.setText(((Object) this.context.getText(R.string.prompt_pdf_saved)) + "\n\n" + str);
            textView.setGravity(17);
            textView2.setText(this.context.getString(R.string.view));
            textView2.setGravity(17);
            textView3.setText(this.context.getString(R.string.okay));
            textView3.setGravity(17);
            ((LinearLayout) dialog.findViewById(R.id.Yes_del)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    final Dialog dialog2 = new Dialog(CreatePDF.this.context, R.style.TransparentBackground);
                    dialog2.setContentView(R.layout.deletedialog);
                    TextView textView4 = (TextView) dialog2.findViewById(R.id.dialogtitletxt);
                    textView4.setText(R.string.viewmsg);
                    textView4.setGravity(17);
                    ((LinearLayout) dialog2.findViewById(R.id.Yes_del)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            dialog2.dismiss();
                            MenuOptionsUtility.viewFile(CreatePDF.this.context, str);
                        }
                    });
                    ((LinearLayout) dialog2.findViewById(R.id.No_del)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            dialog2.dismiss();
                        }
                    });
                    dialog2.show();
                }
            });
            ((LinearLayout) dialog.findViewById(R.id.No_del)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    CreatePDF.this.context.startActivity(new Intent(CreatePDF.this.context, SavedPdflistActivity.class));
                }
            });
            dialog.show();
        }

        public String getFileName(String str, String str2, String str3) {
            String str4 = str2 + str3;
            String str5 = str + "/" + str2 + str3;
            boolean z = true;
            int i = 1;
            while (z) {
                if (new File(str5).exists()) {
                    String str6 = str + "/" + str2 + "(" + i + ")" + str3;
                    i++;
                    str5 = str6;
                    str4 = str2 + "(" + i + ")" + str3;
                } else {
                    z = false;
                }
            }
            return str4;
        }
    }
}
