package com.celeber.habitappandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class BanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ban> bansList;
    private Context mContext;

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    public BanAdapter(List<Ban> bansList) {
        this.bansList = bansList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_ban, parent, false);
            return new FooterViewHolder(itemView);

        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_ban, parent, false);


            return new ItemViewHolder(itemView);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;

            Button resetButton = (Button) footerHolder.itemView.findViewById(R.id.resetButton);

            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Reset Bans?");
                    builder.setMessage("Are you sure? This action can't be undone.");

                    // add the buttons
                    builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DatabaseHelper helper = new DatabaseHelper(mContext);

                            helper.deleteAllBans();
                            bansList.removeAll(bansList);
                            notifyDataSetChanged();

                        }
                    });

                    builder.setNegativeButton("Cancel", null);

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });



        } else if (holder instanceof  ItemViewHolder){

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            final Ban ban = bansList.get(position);

            if (ban.getTitle().length() == 0) {
                // Empty title
                String title = "Ban #" + String.valueOf(position + 1);
                itemViewHolder.banTitle.setText(title);

            } else {
                itemViewHolder.banTitle.setText(ban.getTitle());
            }

            itemViewHolder.resistLabel.setText(ban.getResist());
            itemViewHolder.submitLabel.setText(ban.getSubmit());


            itemViewHolder.banTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                    if (i == EditorInfo.IME_ACTION_DONE) {

                        ban.setTitle(textView.getText().toString());

                        DatabaseHelper dbHandler = new DatabaseHelper(mContext);
                        dbHandler.updateBan(ban);

                        notifyDataSetChanged();
                    }

                    return false;
                }
            });


            itemViewHolder.submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int currentSubmitCount = Integer.valueOf(ban.getSubmit());
                    currentSubmitCount = currentSubmitCount + 1;

                    ban.setSubmit(String.valueOf(currentSubmitCount));

                    DatabaseHelper dbHandler = new DatabaseHelper(mContext);
                    dbHandler.updateBan(ban);

                    notifyDataSetChanged();
                }
            });

            itemViewHolder.submitButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int currentSubmitCount = Integer.valueOf(ban.getSubmit());

                    if (currentSubmitCount > 0) {

                        currentSubmitCount = currentSubmitCount - 1;

                        ban.setSubmit(String.valueOf(currentSubmitCount));

                        DatabaseHelper dbHandler = new DatabaseHelper(mContext);
                        dbHandler.updateBan(ban);
                    }

                    notifyDataSetChanged();

                    return false;
                }
            });


            itemViewHolder.resistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int currentResistCount = Integer.valueOf(ban.getResist());
                    currentResistCount = currentResistCount + 1;

                    ban.setResist(String.valueOf(currentResistCount));

                    DatabaseHelper dbHandler = new DatabaseHelper(mContext);
                    dbHandler.updateBan(ban);

                    notifyDataSetChanged();
                }
            });

            itemViewHolder.resistButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int currentResistCount = Integer.valueOf(ban.getResist());

                    if (currentResistCount > 0) {

                        currentResistCount = currentResistCount - 1;

                        ban.setResist(String.valueOf(currentResistCount));

                        DatabaseHelper dbHandler = new DatabaseHelper(mContext);
                        dbHandler.updateBan(ban);
                    }

                    notifyDataSetChanged();

                    return false;
                }
            });

        }


    }


    @Override
    public int getItemViewType(int position) {

        if (position == bansList.size()) {
            return TYPE_FOOTER;

        } else {
            return TYPE_ITEM;
        }

    }

    @Override
    public int getItemCount() {
        return bansList.size() + 1;
    }

    public void removeItem(int position) {
        bansList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {
        Button resetButton;

        public FooterViewHolder(View view) {
            super(view);
            resetButton = (Button) view.findViewById(R.id.resetButton);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;

        public TextView submitLabel, resistLabel;
        public EditText banTitle;
        public Button submitButton, resistButton;
        public View viewForeground, viewBackground;

        public ItemViewHolder(View view) {
            super(view);
            banTitle = (EditText) view.findViewById(R.id.banTitle);
            submitLabel = (TextView) view.findViewById(R.id.submitLabel);
            resistLabel = (TextView) view.findViewById(R.id.resistLabel);

            submitButton = (Button) view.findViewById(R.id.submitButton);
            resistButton = (Button) view.findViewById(R.id.resistButton);

            viewBackground = (View) view.findViewById(R.id.viewBackground);
            viewForeground = (View) view.findViewById(R.id.viewForeground);
        }

    }

}
