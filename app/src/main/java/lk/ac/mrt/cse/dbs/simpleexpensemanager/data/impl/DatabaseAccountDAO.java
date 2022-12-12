package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DatabaseAccountDAO implements AccountDAO {

    private final DtabaseHelper db;

    public DatabaseAccountDAO(DtabaseHelper db) {
        this.db = db;
    }
    @Override
    public List<String> getAccountNumbersList(){

        List<String> AccountNumberList = new ArrayList<String>();
        SQLiteDatabase database = this.db.getReadableDatabase();

        String query = "SELECT accountNo FROM Accounts; ";

        Cursor result = database.rawQuery(query,null);
        if(result.moveToFirst()){
            do{
                AccountNumberList.add(result.getString(0));
            }while(result.moveToNext());
        }

        result.close();
        database.close();
        return AccountNumberList;

    }
    @Override
    public List<Account> getAccountsList(){
        List<Account> AccountList = new ArrayList<Account>();
        SQLiteDatabase database = this.db.getReadableDatabase();
        String query = "SELECT * FROM Accounts; ";

        Cursor result = database.rawQuery(query,null);
        if(result.moveToFirst()){
            do{
                String accountNo = result.getString(0);
                String bankName = result.getString(1);
                String accHolderName = result.getString(2);
                double balance = result.getDouble(3);
                AccountList.add( new Account(accountNo,bankName,accHolderName,balance));

            }while(result.moveToNext());
        }
        result.close();
        database.close();

        return AccountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account acc = null;

        SQLiteDatabase database = this.db.getReadableDatabase();

        String query = "SELECT * FROM Accounts  WHERE accountNo = '"+accountNo+"' ;";

        Cursor result = database.rawQuery(query,null);
        if(result != null){
            String bankName = result.getString(1);
            String accHolderName = result.getString(2);
            double balance = result.getDouble(3);
            acc= new Account(accountNo,bankName,accHolderName,balance);
            result.close();
        }
        else{
            throw new InvalidAccountException("Invalid Account Number");
        }

        database.close();

        return acc;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase database = this.db.getWritableDatabase();
        /*
        String accountNo = account.getAccountNo();
        String bankName = account.getBankName();
        String accHolderName = account.getAccountHolderName();
        double balance = account.getBalance();
        String query = "INSERT INTO Accounts VALUES ('"+accountNo+"','"+bankName+"','"+accHolderName+"','"+balance+"'); ";
        */
        ContentValues contentValues = new ContentValues();


        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("bankName", account.getBankName());
        contentValues.put("accountHolderName", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        database.insert("accounts", null, contentValues);
        // database.execSQL(query);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase database = this.db.getWritableDatabase();
        String query = "DELETE FROM Accounts  WHERE accountNo = '"+accountNo+"' ;";

        database.execSQL(query);

        database.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase database = this.db.getWritableDatabase();
        String query = "SELECT balance FROM Accounts  WHERE accountNo = '"+accountNo+"' ;";

        Cursor result = database.rawQuery(query,null);
        result.moveToFirst();
        if(result != null){

            double balance = result.getDouble(0);

            switch(expenseType){
                case EXPENSE:
                    balance= balance - amount;
                    break;
                case INCOME:
                    balance= balance + amount;
                    break;
            }
            result.close();

            query = "UPDATE Accounts SET balance = ? WHERE accountNo = ? ;";
            SQLiteStatement updateStatement = database.compileStatement(query);
            updateStatement.bindDouble(1,balance);
            updateStatement.bindString(2,accountNo);
            updateStatement.executeUpdateDelete();
        }
        else{
            database.close();
            throw new InvalidAccountException("Invalid Account Number");
        }
        database.close();

    }
}
