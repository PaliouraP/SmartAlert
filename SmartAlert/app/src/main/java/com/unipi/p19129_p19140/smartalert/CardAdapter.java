package com.unipi.p19129_p19140.smartalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<CardModel> {
    public CardAdapter(Context context, ArrayList<CardModel> cardModelArrayList){
        super(context,0,cardModelArrayList);

    }
@NonNull
@Override
public View getView(int position, View convertView, ViewGroup parent){

    View listitemView=convertView;
    if(listitemView==null){
        listitemView= LayoutInflater.from(getContext()).inflate(R.layout.layout,parent,false);

    }
     CardModel cardModel=getItem(position);
    TextView title= listitemView.findViewById(R.id.TexV);
    ImageView img=listitemView.findViewById(R.id.ImV);
    title.setText(cardModel.getTitle());
    img.setImageResource(cardModel.getImgid());
    return listitemView;
}
}
