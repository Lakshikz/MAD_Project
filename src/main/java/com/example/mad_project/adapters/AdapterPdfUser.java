package com.example.mad_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.MyApplication;
import com.example.mad_project.activities.PdfDetailActivity;
import com.example.mad_project.databinding.RowPdfUserBinding;
import com.example.mad_project.filters.FilterPdfUser;
import com.example.mad_project.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterPdfUser extends RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser> implements Filterable {

    private Context context;
    public ArrayList<ModelPdf> pdfArrayList,filterList;
    private FilterPdfUser filter;

    private RowPdfUserBinding binding;

    private  static final String TAG = "ADAPTER_PDF_USER_TAG";

    public AdapterPdfUser(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind the view
        binding = RowPdfUserBinding.inflate(LayoutInflater.from(context), parent , false);
        return new HolderPdfUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfUser holder, int position) {
        //get data , set data , handle click

        //get data
        ModelPdf model = pdfArrayList.get(position);
        String bookId = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();
        String pdfUrl = model.getUrl();
        String categoryId = model.getCategoryId();
        long timestamp = model.getTimestamp();

        //convert time
        String date = MyApplication.formatTimestamp(timestamp);

        //set data
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(date);

        MyApplication.loadPdfFromUrlSinglePage(
                ""+pdfUrl ,
                ""+title ,
                holder.pdfView,
                holder.progressBar
        );
        MyApplication.loadCategory(
                ""+categoryId,
                holder.categoryTv
        );
        MyApplication.loadPdfSize(
                ""+pdfUrl,
                ""+title,
                holder.sizeTv
        );

        //handle click , show pdf details activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , PdfDetailActivity.class);
                intent.putExtra("bookId",bookId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size(); //return list size||number of records
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterPdfUser(filterList ,this);
        }
        return filter;
    }

    class HolderPdfUser  extends RecyclerView.ViewHolder{

        TextView titleTv,descriptionTv,categoryTv,sizeTv,dateTv;
        PDFView pdfView;
        ProgressBar progressBar;

        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);

            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            categoryTv = binding.categoryTv;
            sizeTv = binding.sizeTv;
            dateTv = binding.dateTv;
            pdfView = binding.pdfView;
            progressBar = binding.progresBar;
        }
    }
}
