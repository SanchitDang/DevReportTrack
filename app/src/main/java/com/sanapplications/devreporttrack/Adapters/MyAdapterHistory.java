package com.sanapplications.devreporttrack.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanapplications.devreporttrack.Activities.DetailActivity;
import com.sanapplications.devreporttrack.Models.ReportWorkModel;
import com.sanapplications.devreporttrack.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterHistory extends RecyclerView.Adapter<MyViewHolderHistory> {

    private Context context;
    private List<ReportWorkModel> dataList;

    public MyAdapterHistory(Context context, List<ReportWorkModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_downlaod_item, parent, false);
        return new MyViewHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderHistory holder, int position) {
        //Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.recLang.setText(dataList.get(position).getDataLang());

        //String pdfUrl = dataList.get(holder.getAdapterPosition()).getDataImage();
        //Log.d("PICCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", pdfUrl);
        //String pdfUrl = "https://firebasestorage.googleapis.com/v0/b/devreporttrack.appspot.com/o/resumes%2Fe4424c4f-8682-4c7e-838e-3a2e6fefb6d3?alt=media&token=d5f45442-6ee4-489f-b468-8f79a8177d93";

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
//                        pdfUrl
//                ));
//
//                String title = URLUtil.guessFileName(
//                        pdfUrl
//                        , null, "application/pdf");
//                request.setTitle(title);
//                request.setDescription("Downloading pls wait");
//                String cookie = CookieManager.getInstance().getCookie(pdfUrl);
//                request.addRequestHeader("cookie",cookie);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
//
//                DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(DOWNLOAD_SERVICE);
//                downloadManager.enqueue(request);
//
//                Toast.makeText(view.getContext(), "Downloading Started", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Language", dataList.get(holder.getAdapterPosition()).getDataLang());
                context.startActivity(intent);


            }
       });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<ReportWorkModel> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolderHistory extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recDesc, recLang;
    CardView recCard;

    public MyViewHolderHistory(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recLang = itemView.findViewById(R.id.recPriority);
        recTitle = itemView.findViewById(R.id.recTitle);
    }
}