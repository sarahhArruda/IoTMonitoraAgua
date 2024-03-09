package br.superMonitoraAgua;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String>
{
    private Context mContext;
    private String[] mAlertas;
    private int[] mImagens;
    private String[] mTempos;

    public CustomListAdapter(Context context, String[] alertas, int[] imagens, String[] tempos)
    {
        super(context, R.layout.list_item_layout, alertas);
        mContext = context;
        mAlertas = alertas;
        mImagens = imagens;
        mTempos = tempos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, parent, false);
        }

        // Configurar os elementos do layout com base nos dados da lista
        ImageView itemImage = listItem.findViewById(R.id.item_image);
        TextView itemName = listItem.findViewById(R.id.item_name);
        TextView itemTime = listItem.findViewById(R.id.item_time);

        // Definir a imagem, nome e tempo com base nos dados da lista
        itemImage.setImageResource(mImagens[position]);
        itemName.setText(mAlertas[position]);
        itemTime.setText(mTempos[position]); // Defina o tempo correspondente

        // Retornar o layout configurado
        return listItem;
    }
}
