package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DtabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager{

    private final Context context;
    private final DtabaseHelper db;
    public PersistentExpenseManager(Context context) {
        this.context =context;
        this.db = new DtabaseHelper(this.context);
        setup();
    }

    @Override
    public void setup(){
        // adding the attributes of the expense manger object
        // hass two attributes
        // accountholder and transaction holder

        // accountholder
        AccountDAO persistantAccDAO = new DatabaseAccountDAO(this.db);
        this.setAccountsDAO(persistantAccDAO);
        //transaction holder
        TransactionDAO persistantTransDAO = new DatabaseTransactionDAO(this.db);
        this.setTransactionsDAO(persistantTransDAO);

        // adding dummy data

        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        this.getAccountsDAO().addAccount(dummyAcct1);
        this.getAccountsDAO().addAccount(dummyAcct2);

    }
}
