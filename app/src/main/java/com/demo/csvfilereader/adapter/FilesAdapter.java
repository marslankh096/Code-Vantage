package com.demo.csvfilereader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.xml.xmp.PdfSchema;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.CsvFileViewerActivity;
import com.demo.csvfilereader.model.FilesModelClass;

import io.paperdb.Book;
import io.paperdb.Paper;

import java.io.File;
import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;


public final class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {
    private static Animation push_animation;
    private final Context context;
    private ArrayList<FilesModelClass> exampleListFull;
    private final ArrayList<FilesModelClass> filesArrayList;
    private final String key;
    private ArrayList<String> favList = new ArrayList<>();
    private ArrayList<String> getlist = new ArrayList<>();

    public Context getContext() {
        return this.context;
    }

    public FilesAdapter(Context context, ArrayList<FilesModelClass> filesArrayList, String key) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(filesArrayList, "filesArrayList");
        Intrinsics.checkNotNullParameter(key, "key");
        this.context = context;
        this.filesArrayList = filesArrayList;
        this.key = key;
        this.exampleListFull = new ArrayList<>(filesArrayList);
        push_animation = AnimationUtils.loadAnimation(context, R.anim.button_push);
    }


    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView favBtn;
        private final TextView fileDate;
        private final ImageView fileIcon;
        private final TextView fileName;
        private final TextView fileSize;

        public ViewHolder(View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            View findViewById = view.findViewById(R.id.fileNameTV);
            Intrinsics.checkNotNullExpressionValue(findViewById, "view.findViewById(R.id.fileNameTV)");
            this.fileName = (TextView) findViewById;
            View findViewById2 = view.findViewById(R.id.fileIcon);
            Intrinsics.checkNotNullExpressionValue(findViewById2, "view.findViewById(R.id.fileIcon)");
            this.fileIcon = (ImageView) findViewById2;
            View findViewById3 = view.findViewById(R.id.fileDate);
            Intrinsics.checkNotNullExpressionValue(findViewById3, "view.findViewById(R.id.fileDate)");
            this.fileDate = (TextView) findViewById3;
            View findViewById4 = view.findViewById(R.id.fileSize);
            Intrinsics.checkNotNullExpressionValue(findViewById4, "view.findViewById(R.id.fileSize)");
            this.fileSize = (TextView) findViewById4;
            View findViewById5 = view.findViewById(R.id.favOp);
            Intrinsics.checkNotNullExpressionValue(findViewById5, "view.findViewById(R.id.favOp)");
            this.favBtn = (ImageView) findViewById5;
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

        public ImageView getFavBtn() {
            return this.favBtn;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.csv_files_item_view, parent, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "from(parent.context).inf…item_view, parent, false)");
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        Paper.init(this.context);
        File file = new File(this.filesArrayList.get(i).getFilePath());
        String filePath = this.filesArrayList.get(i).getFilePath();
        TextView fileName = holder.getFileName();
        fileName.setText(this.filesArrayList.get(i).getFileName() + ".csv");
        holder.getFileSize().setText(this.filesArrayList.get(i).getFileSize());
        holder.getFileDate().setText(this.filesArrayList.get(i).getFileDate());
        getFavData(this.context);
        ArrayList<String> arrayList = this.getlist;
        Intrinsics.checkNotNull(arrayList);
        if (arrayList.contains(filePath)) {
            holder.getFavBtn().setImageResource(R.drawable.fav_h_icn);
        } else {
            holder.getFavBtn().setImageResource(R.drawable.fav_icn);
        }
        holder.getFavBtn().setOnClickListener(new FilesAdapterfavclick(new Ref.BooleanRef(), holder, this, i, filePath));
        String name = file.getName();
        Intrinsics.checkNotNullExpressionValue(name, "file.name");
        if (StringsKt.contains((CharSequence) name, (CharSequence) ".csv", false)) {
            holder.getFileIcon().setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.file_icn));
        } else {
            String name2 = file.getName();
            Intrinsics.checkNotNullExpressionValue(name2, "file.name");
            if (StringsKt.contains((CharSequence) name2, (CharSequence) ".pdf", false)) {
                holder.getFileIcon().setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_pdf_file_icon));
            }
        }
        String name3 = file.getName();
        Intrinsics.checkNotNullExpressionValue(name3, "file.name");
        if (StringsKt.contains((CharSequence) name3, (CharSequence) "csv", false)) {
            holder.itemView.setOnClickListener(new FilesAdapteritemclick(this, filePath));
            return;
        }
        String name4 = file.getName();
        Intrinsics.checkNotNullExpressionValue(name4, "file.name");
        if (StringsKt.contains((CharSequence) name4, (CharSequence) PdfSchema.DEFAULT_XPATH_ID, false)) {
            holder.itemView.setOnClickListener(new FilesAdapterItemclicks(this, filePath, file));
        }
    }

    public static final void FilesAdapterfavclickperform(Ref.BooleanRef buttonOn, ViewHolder holder, FilesAdapter filesAdapter, int i, String path, View view) {
        Intrinsics.checkNotNullParameter(buttonOn, "buttonOn");
        Intrinsics.checkNotNullParameter(holder, "holder");
        Intrinsics.checkNotNullParameter(filesAdapter, "this");
        Intrinsics.checkNotNullParameter(path, "path");
        if (!buttonOn.element) {
            buttonOn.element = true;
            holder.getFavBtn().setImageResource(R.drawable.fav_h_icn);
            filesAdapter.addfavdata(filesAdapter.context, filesAdapter.filesArrayList.get(i).getFilePath());
            Toast.makeText(filesAdapter.context, "Add to the Favourite..", Toast.LENGTH_LONG).show();
            view.startAnimation(push_animation);
            return;
        }
        buttonOn.element = false;
        holder.getFavBtn().setImageResource(R.drawable.fav_icn);
        filesAdapter.deleteFav(filesAdapter.context, path);
        Toast.makeText(filesAdapter.context, "Remove from the Favourite..", Toast.LENGTH_LONG).show();
        ArrayList<String> arrayList = filesAdapter.getlist;
        Intrinsics.checkNotNull(arrayList);
        arrayList.remove(path);
        filesAdapter.notifyDataSetChanged();
        view.startAnimation(push_animation);
    }

    public static final void FilesAdapteritemclickpeeform(FilesAdapter filesAdapter, String path, View view) {
        Intrinsics.checkNotNullParameter(filesAdapter, "this");
        Intrinsics.checkNotNullParameter(path, "path");
        Context context = filesAdapter.context;
        filesAdapter.getRecentdata(context, path);
        context.startActivity(new Intent(context, CsvFileViewerActivity.class).putExtra(CsvFileViewerActivity.CSV_FILE, path));
    }

    public static void FilesAdapterItemclicksperform(FilesAdapter filesAdapter, String path, File file, View view) {
        Intrinsics.checkNotNullParameter(filesAdapter, "this");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(file, "file");
        filesAdapter.getRecentdata(filesAdapter.context, path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(FileProvider.getUriForFile(filesAdapter.context, "com.csv.file.viewer.reader.app.provider", file), "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        filesAdapter.context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return this.filesArrayList.size();
    }

    public void addfavdata(Context context, String path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        ArrayList arrayList = (ArrayList) Paper.book().read("fav", new ArrayList());
        ArrayList arrayList2 = new ArrayList();
        if (arrayList == null) {
            arrayList2.add(path);
            Paper.book().write("fav", arrayList2);
            return;
        }
        if (arrayList.contains(path)) {
            arrayList.remove(path);
        }
        arrayList.add(path);
        Paper.book().write("fav", arrayList);
    }

    public void getRecentdata(Context context, String path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        Paper.init(context);
        ArrayList arrayList = (ArrayList) Paper.book().read("recent", new ArrayList());
        ArrayList arrayList2 = new ArrayList();
        if (arrayList == null) {
            arrayList2.add(path);
            Paper.book().write("recent", arrayList2);
            return;
        }
        if (arrayList.contains(path)) {
            arrayList.remove(path);
        }
        arrayList.add(path);
        Paper.book().write("recent", arrayList);
    }

    public void deleteFav(Context context, String path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        Paper.init(context);
        ArrayList<String> arrayList = (ArrayList) Paper.book().read("fav", new ArrayList());
        this.favList = arrayList;
        Intrinsics.checkNotNull(arrayList);
        if (arrayList.contains(path)) {
            ArrayList<String> arrayList2 = this.favList;
            Intrinsics.checkNotNull(arrayList2);
            arrayList2.remove(path);
        }
        Book book = Paper.book();
        ArrayList<String> arrayList3 = this.favList;
        Intrinsics.checkNotNull(arrayList3);
        book.write("fav", arrayList3);
    }

    public void getFavData(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.getlist = (ArrayList) Paper.book().read("fav", new ArrayList());
    }
}
