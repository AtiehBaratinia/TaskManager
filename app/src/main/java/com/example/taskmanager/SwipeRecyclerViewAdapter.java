package com.example.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Task> tasks;
    private int image = R.drawable.task;

    private Task item;
    private DataBaseHelper db;
    SwipeRecyclerViewAdapter(Context context, ArrayList<Task> objects, DataBaseHelper db) {
        this.mContext = context;
        this.tasks = objects;
        this.db = db;
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list, parent, false);
        return new SimpleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        item = tasks.get(position);
        viewHolder.task = item;
        viewHolder.TitleList.setText(item.getTitle());
        viewHolder.AssignList.setText("Assigned to: " + item.getAssign());
        viewHolder.TypeList.setText(item.getTypeOfTask());
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String date = df.format(c);

        String[] split = date.split("/");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        String temp = item.getDate();
        String[]  taskDate = temp.split("/");
        int yearTask = Integer.parseInt(taskDate[0]);
        int monthTask = Integer.parseInt(taskDate[1]);
        int dayTask = Integer.parseInt(taskDate[2]);

        if (yearTask == year && month == monthTask && day <= dayTask && day + 2 >= dayTask) {
            viewHolder.close.setVisibility(View.VISIBLE);
        }
        if (yearTask == year && month == monthTask - 1 && day >= 28 && 2 > dayTask) {
            viewHolder.close.setVisibility(View.VISIBLE);
        }
        viewHolder.imageView.setImageResource(image);


        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //dari kiri
        //viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        //dari kanan
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wraper));



//        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
//            @Override
//            public void onStartOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onStartClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//
//            }
//
//            @Override
//            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//
//            }
//        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(this);




        viewHolder.Cancel.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     viewHolder.swipeLayout.close();
                                                 }
                                             });
        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent intent = new Intent(mContext, EditTask.class);
                                                   intent.putExtra("Task", viewHolder.task);
                                                   ((Activity)mContext).startActivityForResult(intent,navigationDrawer.REQUEST_CODE_1);
                                                   viewHolder.swipeLayout.close();
                                               }
                                           });

        viewHolder.Delete.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.deleteData(viewHolder.task.getId());
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                tasks.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, tasks.size());
                                mItemManger.closeAllItems();
                                Toast.makeText(v.getContext(), "Deleted " + viewHolder.TitleList.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.swipe:
                Toast.makeText(mContext, " Click : " + item.getTitle() + " \n" + item.getAssign(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    void filterList(ArrayList<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }



    static class SimpleViewHolder extends RecyclerView.ViewHolder{
        SwipeLayout swipeLayout;
        ImageView imageView;
        TextView AssignList;
        TextView TitleList;
        TextView TypeList;
        TextView close;
        ImageView Delete;
        ImageView Edit;
        ImageView Cancel;
        Task task;
        SimpleViewHolder(View itemView) {
            super(itemView);


            swipeLayout = itemView.findViewById(R.id.swipe);
            AssignList = itemView.findViewById(R.id.assign_list);
            TitleList = itemView.findViewById(R.id.title_list);
            TypeList = itemView.findViewById(R.id.type_task);
            close = itemView.findViewById(R.id.close);
            Delete = itemView.findViewById(R.id.Delete);
            Edit = itemView.findViewById(R.id.Edit);
            Cancel = itemView.findViewById(R.id.Cancel);
            imageView = itemView.findViewById(R.id.image_list);
        }
    }

}
