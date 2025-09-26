package com.demo.csvfilereader.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.SavedPdflistActivity;
import com.demo.csvfilereader.model.Conversion;
import com.demo.csvfilereader.utility.FileUtility;
import com.demo.csvfilereader.utility.MenuOptionsUtility;

import java.util.ArrayList;


public class ConversionsAdapter extends RecyclerView.Adapter<ConversionsAdapter.ViewHolder> {
    Context context;
    ArrayList<Conversion> conversions;
    SavedPdflistActivity conversionsActivity;
    RecyclerView recyclerView;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView delete;
        ImageView file_image;
        TextView file_name;
        TextView file_path;
        TextView file_size;
        TextView print;
        ImageView share;
        ImageView view;

        ViewHolder(View view2) {
            super(view2);
            this.cardView = null;
            this.delete = null;
            this.file_image = null;
            this.file_name = null;
            this.file_path = null;
            this.file_size = null;
            this.print = null;
            this.share = null;
            this.view = null;
            this.cardView = (CardView) view2.findViewById(R.id.cv);
            this.file_image = (ImageView) view2.findViewById(R.id.file_image);
            this.file_name = (TextView) view2.findViewById(R.id.file_name);
            this.file_size = (TextView) view2.findViewById(R.id.file_size);
            this.file_path = (TextView) view2.findViewById(R.id.file_path);
            this.view = (ImageView) view2.findViewById(R.id.view);
            this.share = (ImageView) view2.findViewById(R.id.share);
            this.delete = (ImageView) view2.findViewById(R.id.delete);
            this.print = (TextView) view2.findViewById(R.id.print);
        }
    }

    public ConversionsAdapter(Context context, SavedPdflistActivity conversionsActivity, ArrayList<Conversion> arrayList) {
        this.conversionsActivity = null;
        this.context = null;
        this.conversions = null;
        this.conversionsActivity = conversionsActivity;
        this.conversions = arrayList;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_converted_file_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (i < this.conversions.size()) {
            viewHolder.cardView.setVisibility(View.VISIBLE);
            final String str = this.conversions.get(i).CONVERTED_FILE_PATH;
            final String str2 = this.conversions.get(i).CONVERTED_FILE_NAME;
            viewHolder.file_name.setText(str2);
            viewHolder.file_path.setText(str);
            viewHolder.file_size.setText(FileUtility.getFileSize(this.conversions.get(i).CONVERTED_FILE_PATH));
            viewHolder.view.setOnClickListener(new AnonymousClass1(str));
            viewHolder.print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    view.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setVisibility(View.VISIBLE);
                        }
                    }, 50L);
                }
            });
            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    view.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setVisibility(View.VISIBLE);
                            MenuOptionsUtility.shareFile(ConversionsAdapter.this.context, str);
                        }
                    }, 50L);
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    view.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setVisibility(View.VISIBLE);
                            ConversionsAdapter.this.openDeleteDialog(str, str2, i);
                        }
                    }, 50L);
                }
            });
            return;
        }
        viewHolder.cardView.setVisibility(View.INVISIBLE);
    }


    public class AnonymousClass1 implements View.OnClickListener {
        final String val$str;

        AnonymousClass1(final String val$str) {
            this.val$str = val$str;
        }

        @Override
        public void onClick(final View view) {
            view.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    final Dialog dialog = new Dialog(ConversionsAdapter.this.conversionsActivity, R.style.TransparentBackground);
                    dialog.setContentView(R.layout.deletedialog);
                    TextView textView = (TextView) dialog.findViewById(R.id.dialogtitletxt);
                    textView.setText(R.string.viewmsg);
                    textView.setGravity(17);
                    ((LinearLayout) dialog.findViewById(R.id.Yes_del)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            dialog.dismiss();
                            MenuOptionsUtility.viewFile(ConversionsAdapter.this.context, AnonymousClass1.this.val$str);
                        }
                    });
                    ((LinearLayout) dialog.findViewById(R.id.No_del)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }, 50L);
        }
    }

    public void openDeleteDialog(final String str, String str2, final int i) {
        final Dialog dialog = new Dialog(this.conversionsActivity, R.style.TransparentBackground);
        dialog.setContentView(R.layout.deletedialog);
        ((LinearLayout) dialog.findViewById(R.id.Yes_del)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (FileUtility.deleteFile(str)) {
                    Toast.makeText(ConversionsAdapter.this.context, ConversionsAdapter.this.context.getText(R.string.prompt_delete_successful), Toast.LENGTH_SHORT).show();
                    ConversionsAdapter.this.recyclerView.getLayoutManager().scrollToPosition(i - 1);
                    ConversionsAdapter.this.conversionsActivity.deleteSavedVideo(str);
                    ConversionsAdapter.this.conversionsActivity.refreshConversionList();
                }
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.No_del)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return this.conversions.size() + 1;
    }
}
