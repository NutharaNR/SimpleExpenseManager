package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseTransactionDAO implements TransactionDAO {

    private final DtabaseHelper db;
    public DatabaseTransactionDAO(DtabaseHelper db) {
        this.db= db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase database = this.db.getWritableDatabase();
        String expensetypeStr = new String(String.valueOf(expenseType));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        // String query = "INSERT INTO Transactions VALUES ('"+accountNo+"','"+strDate+"','"+expensetypeStr+"',"+amount+");";
        // database.execSQL(query);
        ContentValues contentValues = new ContentValues();


        contentValues.put("accountNo", accountNo);
        contentValues.put("date", strDate);
        contentValues.put("expenseType", expensetypeStr);
        contentValues.put("amount", amount);
        database.insert("Transactions", null, contentValues);
        database.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        List<Transaction> TransactionLogs = new ArrayList<>();
        SQLiteDatabase database = this.db.getReadableDatabase();
        String query = "SELECT * FROM Transactions ;";

        Cursor result = database.rawQuery(query,null);

        if(result.moveToFirst()){

            do{
                String  accNo = result.getString(1);
                String[] strDate  = result.getString(2).split("/");
                String strExpenseType = result.getString(3);
                double amount = result.getDouble(4);

                ExpenseType  expenseType = ExpenseType.valueOf(strExpenseType);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(strDate[0]),Integer.parseInt(strDate[1]),Integer.parseInt(strDate[2]));
                Date date = calendar.getTime();

                TransactionLogs.add(new Transaction(date,accNo,expenseType,amount));
            }while(result.moveToNext());
        }
        result.close();
        database.close();
        return TransactionLogs;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> paginatedTransactionLogs ;
        List<Transaction> allTransactionLogs = this.getAllTransactionLogs();
        int size = allTransactionLogs.size();

        if (size <= limit) {
            paginatedTransactionLogs = allTransactionLogs;
        }
        else {
            paginatedTransactionLogs = allTransactionLogs.subList(size - limit, size);
        }



        return paginatedTransactionLogs;
    }
}
