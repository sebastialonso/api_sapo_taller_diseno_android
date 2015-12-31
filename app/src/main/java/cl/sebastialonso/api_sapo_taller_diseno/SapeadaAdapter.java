package cl.sebastialonso.api_sapo_taller_diseno;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by seba on 12/29/15.
 */
public class SapeadaAdapter extends RecyclerView.Adapter<SapeadaAdapter.SapeadaViewHolder> {

    private List<Sapeada> mSapeadas;
    private static OnItemClickListener mListener;

    SapeadaAdapter(List<Sapeada> inSapeadas){
        mSapeadas = inSapeadas;
    }

    // This is the method that allows the parent fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position);
    }

    @Override
    public int getItemCount(){
        return mSapeadas.size();
    }

    @Override
    public SapeadaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.each_sapeada, viewGroup, false);
        return new SapeadaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SapeadaViewHolder sapeadaViewHolder, int i){
        String title;
        if (mSapeadas.get(i).mDirection) title = "Hacia Valparaiso";
        else title = "Hacia Viña del Mar";
        sapeadaViewHolder.sapeadaDirection.setText(title);
        sapeadaViewHolder.sapeadaTime.setText(String.valueOf(mSapeadas.get(i).mTime));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class SapeadaViewHolder extends RecyclerView.ViewHolder {

        CardView sapeadaCardView;
        TextView sapeadaDirection;
        TextView sapeadaTime;

        public SapeadaViewHolder(View itemView){
            super(itemView);
            sapeadaCardView = (CardView) itemView.findViewById(R.id.sapeada_cardview);
            sapeadaDirection = (TextView) itemView.findViewById(R.id.sapeada_direction);
            sapeadaTime = (TextView) itemView.findViewById(R.id.sapeada_time);

            //Each element of the recycler will call mListener (the adapter's listener) when it's clicked
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // Triggers click upwards to the adapter on click
                    if (mListener != null){
                        mListener.onItemClick(SapeadaViewHolder.this.itemView, getLayoutPosition());
                    }
                }
            });
        }

    }
}
