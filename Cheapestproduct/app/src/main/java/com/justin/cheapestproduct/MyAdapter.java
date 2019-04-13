package com.justin.cheapestproduct;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//    private Context context;
    private List<String> priceData,storeData,storeName,remarkList,dateList;

    MyAdapter(List<String> priceData,List<String> storeData,List<String> remarkList,List<String> dateList){
        Log.d("holer:","ViewHolder執行1");
//        this.context = context;
        this.priceData =priceData;
        this.storeData = storeData;
        this.remarkList = remarkList;
        this.dateList = dateList;
        Log.d("holer_Mdata_price:",priceData.toString());
        Log.d("holer_Mdata_remarkList:",remarkList.toString());
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtStoreName,txtPrice,txtRemark,txtUpdDate;


        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("holer:","ViewHolder執行2");
            txtStoreName = (TextView)itemView.findViewById(R.id.txtStoreName);
            txtPrice = (TextView)itemView.findViewById(R.id.txtPrice);
            txtRemark = (TextView)itemView.findViewById(R.id.txtRemark);
//            txtUpdDate = (TextView)itemView.findViewById(R.id.txtUpdDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("holer:","ViewHolder執行3");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleviewlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        Log.d("holer:","ViewHolder執行4");
        holder.txtPrice.setText(priceData.get(position));
        holder.txtRemark.setText(remarkList.get(position)+','+dateList.get(position));
//        holder.txtUpdDate.setText(dateList.get(position));


        Log.d("holer_priceData:",priceData.get(position));
        Log.d("holer_storeData:",storeData.get(position));

        switch (storeData.get(position)){
            case "1":
                holder.txtStoreName.setText("風獅爺商店");
                Log.d("holer_storeName1:",storeData.get(position));
                break;
            case "2":
                holder.txtStoreName.setText("全聯");
                Log.d("holer_storeName2:",storeData.get(position));
                break;
            case "3":
                holder.txtStoreName.setText("寶雅");
                Log.d("holer_storeName3:",storeData.get(position));
                break;
            case "4":
                holder.txtStoreName.setText("家樂福");
                Log.d("holer_storeName3:",storeData.get(position));
                break;
            case "5":
                holder.txtStoreName.setText("日藥本舖");
                Log.d("holer_storeName3:",storeData.get(position));
                break;
            case "6":
                holder.txtStoreName.setText("康是美");
                Log.d("holer_storeName3:",storeData.get(position));
                break;
            case "7":
                holder.txtStoreName.setText("屈臣氏");
                Log.d("holer_storeName3:",storeData.get(position));
                break;
        }
        Log.d("holer_storeName:",storeData.get(position));

    }

    @Override
    public int getItemCount() {
        return priceData.size();
    }

    public void AddItem(String text){
        priceData.add(0,text);
        notifyItemInserted(0);
    }
}
