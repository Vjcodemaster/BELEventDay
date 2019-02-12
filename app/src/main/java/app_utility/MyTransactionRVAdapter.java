package app_utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bel.antimatter.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyTransactionRVAdapter extends RecyclerView.Adapter<MyTransactionRVAdapter.TransactionTabHolder> {

    Context context;
    private RecyclerView recyclerView;
    ArrayList<DataBaseHelper> dbData;

    public MyTransactionRVAdapter(Context context, RecyclerView recyclerView, ArrayList<DataBaseHelper> dbData) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.dbData = dbData;
    }

    @NonNull
    @Override
    public TransactionTabHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_transaction_layout, parent, false);

        return new TransactionTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionTabHolder holder, final int position) {

        holder.tvTime.setText(dbData.get(position).get_time());
        holder.tvEmployeeID.setText(dbData.get(position).get_emp_id());
        holder.tvAmount.setText(dbData.get(position).get_amount());
    }

    @Override
    public int getItemCount() {
        return dbData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class TransactionTabHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvEmployeeID;
        TextView tvAmount;

        TransactionTabHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_rv_time);
            tvEmployeeID = itemView.findViewById(R.id.tv_rv_employee_id);
            tvAmount = itemView.findViewById(R.id.tv_rv_amount);
        }
    }

}