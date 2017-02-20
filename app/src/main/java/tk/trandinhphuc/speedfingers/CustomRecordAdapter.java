package tk.trandinhphuc.speedfingers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DinhPhuc on 20/02/2017.
 */

public class CustomRecordAdapter extends RecyclerView.Adapter<CustomRecordAdapter.ViewHolder> {

    private Context mContext;
    private List<CustomRecordActivity.CustomRecord> mRecords;

    public CustomRecordAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomRecordActivity.CustomRecord record = mRecords.get(position);
        holder.time.setText(record.time + "s");
        holder.record.setText(record.record + "");
    }

    @Override
    public int getItemCount() {
        if(mRecords != null)
            return mRecords.size();
        return 0;
    }

    public void setRecordList(List<CustomRecordActivity.CustomRecord> records){
        this.mRecords = records;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView record;
        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            record = (TextView) itemView.findViewById(R.id.record);
        }
    }
}
