package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DtabaseHelper extends SQLiteOpenHelper {

    private static final String database_name="200407H.db";
    private static final int version = 1;
    private final Context context;
    public DtabaseHelper(@Nullable Context context) {
        super(context, database_name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE Accounts (accountNo TEXT PRIMARY KEY, bankName TEXT, accountHolderName TEXT, balance REAL);";
        String query2 = "CREATE TABLE Transactions (transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, accountNo TEXT, date TEXT, expenseType TEXT, amount REAL, FOREIGN KEY (accountNo) REFERENCES Accounts (accountNo) );";
        db.execSQL(query1);
        db.execSQL(query2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
