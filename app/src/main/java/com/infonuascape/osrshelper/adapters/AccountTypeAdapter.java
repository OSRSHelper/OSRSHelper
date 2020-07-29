package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by marc_ on 2018-01-20.
 */

public class AccountTypeAdapter extends ArrayAdapter<AccountType> {
    private ArrayList<AccountType> accountTypes;

    public AccountTypeAdapter(@NonNull Context context) {
        super(context, R.layout.account_type_list_item);
        accountTypes = new ArrayList<>();
        accountTypes.add(AccountType.REGULAR);
        accountTypes.add(AccountType.IRONMAN);
        accountTypes.add(AccountType.HARDCORE_IRONMAN);
        accountTypes.add(AccountType.ULTIMATE_IRONMAN);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = View.inflate(getContext(), R.layout.account_type_list_item, null);

        AccountType accountType = accountTypes.get(position);
        ((ImageView) view.findViewById(R.id.account_type_icon)).setImageResource(Utils.getAccountTypeResource(accountType));
        ((TextView) view.findViewById(R.id.account_type_name)).setText(accountType.displayName);

        return view;
    }

    @Nullable
    @Override
    public AccountType getItem(int position) {
        return accountTypes.get(position);
    }

    @Override
    public int getCount() {
        return accountTypes.size();
    }
}
