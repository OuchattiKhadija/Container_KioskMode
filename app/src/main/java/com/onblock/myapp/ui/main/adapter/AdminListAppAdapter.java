package com.onblock.myapp.ui.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.view.AdminHomeActivity;
import com.onblock.myapp.ui.main.view.MainActivity;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class AdminListAppAdapter extends RecyclerView.Adapter<AdminListAppAdapter.AdminViewHolder> {

    List<AppInfo> apps = new ArrayList();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_app_item, null, false);
        AdminViewHolder viewHolder = new AdminViewHolder(vi);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminViewHolder holder, int position) {
        final AppInfo currentAppInfo = apps.get(position);
        holder.appName.setText(currentAppInfo.getName());
        holder.appPackage.setText(currentAppInfo.getPackageName());
        holder.appIcon.setImageDrawable(AppInfoController.bytes2Drawable(currentAppInfo.getIcon()));
        /** holder.checked.setChecked(apps.get(position).getIsNormalUserAllowed());
         holder.checked.setTag(apps.get(position));
         //holder.cheked.setTag(position);
         holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
        //  currentAppInfo.setNormalUserAllowed(true);
        //update the app info in DB
        onItemCheckListener.onItemCheck(currentAppInfo);

        out.println("lapplication " + currentAppInfo.getName() + "est autorisé " + currentAppInfo.getIsNormalUserAllowed());
        }else {
        //currentAppInfo.setNormalUserAllowed(false);
        onItemCheckListener.onItemUncheck(currentAppInfo);
        out.println("lapplication " + currentAppInfo.getName() + " not allowed " + currentAppInfo.getIsNormalUserAllowed());

        }
        }
        });**/


    }

    public AppInfo getAppAt(int position) {
        return apps.get(position);
    }


    @Override
    public int getItemCount() {
        return apps == null ? 0 : apps.size();
    }

    public void setApps(List<AppInfo> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView appName, appPackage;
        ImageView appIcon;
        // CheckBox checked;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.nameApp);
            appPackage = itemView.findViewById(R.id.packageName);
            appIcon = itemView.findViewById(R.id.iconApp);
            //    checked = itemView.findViewById(R.id.is_Permit);
            //this.setIsRecyclable(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //get adapter position of the cerd clicked
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(apps.get(position));
                    }

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AppInfo appInfo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * //----------------------
     * Context context;
     * OnItemCheckListener onItemCheckListener;
     * <p>
     * public interface OnItemCheckListener {
     * void onItemCheck(AppInfo appInfo);
     * void onItemUncheck(AppInfo appInfo);
     * }
     * <p>
     * public AdminListAppAdapter(Context context ,OnItemCheckListener onItemCheckListener) {
     * this.context = context;
     * this.onItemCheckListener = onItemCheckListener;
     * }
     **/
//-------------------------

    /**
     * private LiveData<ArrayList<AppInfo>> apps;
     * private LayoutInflater inflater;
     * <p>
     * <p>
     * public AdminListAppAdapter(Context context, int resource, LiveData<ArrayList<AppInfo>> objects) {
     * super(context, resource);
     * this.apps = objects;
     * inflater = LayoutInflater.from(context);
     * }
     *
     * @Override public int getCount() {
     * return apps.size();
     * }
     * @Override public AppInfo getItem(int i) {
     * return apps.get(i);
     * }
     * @Override public long getItemId(int i) {
     * return i;
     * }
     * @Override public View getView(int position, View view, ViewGroup viewGroup) {
     * View vi = view;
     * if (view == null)
     * vi = inflater.inflate(R.layout.list_app_item, null);
     * final AppInfo appInfo = apps.get(position);
     * TextView appName = vi.findViewById(R.id.nameApp);
     * TextView appVersion = vi.findViewById(R.id.versionApp);
     * ImageView appIcon = vi.findViewById(R.id.iconApp);
     * CheckBox cheked = vi.findViewById(R.id.is_Permit);
     * cheked.setTag(position);
     * cheked.setOnCheckedChangeListener(checkListener);
     * /*  cheked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
     * @Override public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
     * if (isChecked){
     * appInfo.setNormalUserAllowed(true);
     * out.println(appInfo);
     * }
     * }
     * });
     * appIcon.setImageDrawable(appInfo.getIcon());
     * appVersion.setText(appInfo.getVersionName());
     * appName.setText(appInfo.getName());
     * return vi;
     * }



    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    AppInfo currentApp;
    int position = (int) buttonView.getTag();
    currentApp = apps.get(position);
    //Affichage de longue durée
    currentApp.setNormalUserAllowed(true);

    out.println("appSelectionner : " + currentApp.getName());

    }
    };*/
}


