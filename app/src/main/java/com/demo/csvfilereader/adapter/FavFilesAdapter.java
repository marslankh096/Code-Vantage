package com.demo.csvfilereader.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.xml.xmp.PdfSchema;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.CsvFileViewerActivity;
import com.demo.csvfilereader.model.FilesModelClass;

import io.paperdb.Paper;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;


public final class FavFilesAdapter extends RecyclerView.Adapter<FavFilesAdapter.ViewHolder> {
    private static Animation push_animation;
    private final ArrayList<FilesModelClass> arrayList;
    private final Context context;
    private ArrayList<FilesModelClass> exampleListFull;
    private final String key;
    private ArrayList<String> arrayList2 = new ArrayList<>();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public Context getContext() {
        return this.context;
    }

    public ArrayList<FilesModelClass> getArrayList() {
        return this.arrayList;
    }

    public FavFilesAdapter(Context context2, ArrayList<FilesModelClass> arrayList3, String str) {
        this.context = context2;
        this.arrayList = arrayList3;
        this.key = str;
        this.exampleListFull = new ArrayList<>(arrayList3);
        push_animation = AnimationUtils.loadAnimation(context2, R.anim.button_push);
    }


    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView btnUnFav;
        private final TextView fileDate;
        private final ImageView fileIcon;
        private final TextView fileName;
        private final TextView fileSize;

        public ViewHolder(View view) {
            super(view);
            this.fileName = (TextView) view.findViewById(R.id.fileNameTV);
            this.fileIcon = (ImageView) view.findViewById(R.id.fileIcon);
            this.fileDate = (TextView) view.findViewById(R.id.fileDate);
            this.fileSize = (TextView) view.findViewById(R.id.fileSize);
            this.btnUnFav = (ImageView) view.findViewById(R.id.btnUnfav);
        }

        public TextView getFileName() {
            return this.fileName;
        }

        public ImageView getFileIcon() {
            return this.fileIcon;
        }

        public TextView getFileDate() {
            return this.fileDate;
        }

        public TextView getFileSize() {
            return this.fileSize;
        }

        public ImageView getFavOption() {
            return this.btnUnFav;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_files_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final File file = new File(this.arrayList.get(i).getFilePath());
        final String filePath = this.arrayList.get(i).getFilePath();
        viewHolder.getFileName().setText(this.arrayList.get(i).getFileName());
        viewHolder.getFileSize().setText(this.arrayList.get(i).getFileSize());
        viewHolder.getFileDate().setText(this.arrayList.get(i).getFileDate());
        viewHolder.getFavOption().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                View inflate = LayoutInflater.from(FavFilesAdapter.this.context).inflate(R.layout.dialog_delete, (ViewGroup) null);
                final AlertDialog show = new AlertDialog.Builder(FavFilesAdapter.this.context, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
                Window window = show.getWindow();
                window.setLayout(-1, -2);
                window.setGravity(17);
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.dimAmount = 0.7f;
                attributes.flags = 2;
                show.getWindow().setBackgroundDrawableResource(17170445);
                ((ImageView) inflate.findViewById(R.id.btn_Cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(FavFilesAdapter.push_animation);
                        show.dismiss();
                    }
                });
                ((ImageView) inflate.findViewById(R.id.btn_Done)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(FavFilesAdapter.push_animation);
                        show.dismiss();
                        FavFilesAdapter.dialogbtnclickyesperformdel(FavFilesAdapter.this, filePath, i);
                    }
                });
                show.show();
            }
        });
        if (StringsKt.contains((CharSequence) file.getName(), (CharSequence) ".csv", false)) {
            viewHolder.getFileIcon().setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.file_icn));
        } else if (StringsKt.contains((CharSequence) file.getName(), (CharSequence) ".pdf", false)) {
            viewHolder.getFileIcon().setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_pdf_file_icon));
        }
        if (StringsKt.contains((CharSequence) file.getName(), (CharSequence) "csv", false)) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavFilesAdapter.opencsvinview(FavFilesAdapter.this, filePath);
                }
            });
        } else if (StringsKt.contains((CharSequence) file.getName(), (CharSequence) PdfSchema.DEFAULT_XPATH_ID, false)) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavFilesAdapter.openfile(FavFilesAdapter.this, filePath, file);
                }
            });
        }
    }

    public static void dialogbtnclickyesperformdel(FavFilesAdapter favFilesAdapter, String str, int i) {
        favFilesAdapter.deleteFav(favFilesAdapter.context, str);
        ArrayList<FilesModelClass> arrayList = favFilesAdapter.arrayList;
        arrayList.remove(arrayList.get(i));
        favFilesAdapter.notifyDataSetChanged();
    }

    public static void opencsvinview(FavFilesAdapter favFilesAdapter, String str) {
        favFilesAdapter.getRecentData(favFilesAdapter.context, str);
        favFilesAdapter.context.startActivity(new Intent(favFilesAdapter.context, CsvFileViewerActivity.class).putExtra(CsvFileViewerActivity.CSV_FILE, str));
    }

    public static void openfile(FavFilesAdapter favFilesAdapter, String str, File file) {
        favFilesAdapter.getRecentData(favFilesAdapter.context, str);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(FileProvider.getUriForFile(favFilesAdapter.context, favFilesAdapter.context.getPackageName() + ".provider", file), "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        favFilesAdapter.context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }


    public void shareFile(File file, Context context2) {
        Uri uriForFile = FileProvider.getUriForFile(context2, context.getPackageName() + context.getPackageName() + ".provider", file);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("*/*");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.putExtra("android.intent.extra.SUBJECT", Intrinsics.stringPlus("Sharing File from", context2.getString(R.string.app_name)));
        context2.startActivity(Intent.createChooser(intent, context2.getString(R.string.app_name)));
    }

    public void deleteFav(Context context2, String str) {
        Paper.init(context2);
        ArrayList arrayList = (ArrayList) Paper.book().read("fav", new ArrayList());
        Intrinsics.checkNotNull(arrayList);
        if (arrayList.contains(str)) {
            arrayList.remove(str);
        }
        Paper.book().write("fav", arrayList);
    }

    public void getRecentData(Context context2, String str) {
        Paper.init(context2);
        ArrayList arrayList = (ArrayList) Paper.book().read("recent", new ArrayList());
        ArrayList arrayList2 = new ArrayList();
        if (arrayList == null) {
            arrayList2.add(str);
            Paper.book().write("recent", arrayList2);
            return;
        }
        if (arrayList.contains(str)) {
            arrayList.remove(str);
        }
        arrayList.add(str);
        Paper.book().write("recent", arrayList);
    }
}
