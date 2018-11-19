package com.infonuascape.osrshelper.bubble;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.listeners.HiscoresFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.HiscoresFetcherTask;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.views.RSView;

import io.mattcarroll.hover.Content;

public class HiscoreContent implements Content, TextWatcher, HiscoresFetcherListener, RecyclerItemClickListener {
    private static final String TAG = "HiscoreContent";

    private Context context;
    private RSView rsView;
    private EditText usernameEdit;
    private Handler handler;
    private HiscoresFetcherTask asyncTask;
    private View view;

    public HiscoreContent(final Context context) {
        this.context = context;
        handler = new Handler();
    }

    @Override
    public View getView() {
        view = View.inflate(context, R.layout.bubble_hiscore, null);
        rsView = view.findViewById(R.id.rs_view);
        usernameEdit = view.findViewById(R.id.edit_text);
        usernameEdit.addTextChangedListener(this);
        return view;
    }

    @Override
    public boolean isFullscreen() {
        return false;
    }

    public static View createTabView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.drawable.circle_white);
        imageView.setImageResource(R.drawable.hiscore);
        int padding = (int) Utils.convertDpToPixel(11f, context);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }

    @Override
    public void onShown() {
        Logger.add(TAG, ": onShown");
        if(view != null) {
            view.invalidate();
        }
    }

    @Override
    public void onHidden() {
        Logger.add(TAG, ": onHidden");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadHiscores();
            }
        }, 400);
    }

    private void loadHiscores() {
        Logger.add(TAG, ": loadHiscores");
        if(asyncTask != null) {
            asyncTask.cancel(true);
        }

        final String username = usernameEdit.getText().toString();
        Account account = DBController.getAccountByUsername(context, username);
        if(account == null) {
            account = new Account(username, AccountType.REGULAR);
            DBController.addOrUpdateAccount(context, account);
        }
        asyncTask = new HiscoresFetcherTask(context, this, account);
        asyncTask.execute();
    }

    @Override
    public void onHiscoresFetched(PlayerSkills playerSkills) {
        Logger.add(TAG, ": onHiscoresFetched");
        rsView.populateView(playerSkills, this);
    }

    @Override
    public void onHiscoresError(String errorMessage) {
        Logger.add(TAG, ": onHiscoresError");
        rsView.populateViewWithZeros(this);
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onItemLongClicked(int position) {

    }
}
