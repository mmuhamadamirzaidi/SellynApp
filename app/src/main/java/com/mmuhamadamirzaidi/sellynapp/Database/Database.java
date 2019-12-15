package com.mmuhamadamirzaidi.sellynapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.mmuhamadamirzaidi.sellynapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    public static final String DB_NAME = "SellynAppDatabase.db";
    public static final int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public List<Order> getCart(){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId", "ProductName", "Quantity", "Price", "Discount", "ProductImage"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();

        if (c.moveToFirst()){
            do{
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("ProductImage"))
                ));
            }
            while (c.moveToNext());
        }

        return result;
    }

    //Cart

    public void addToCart(Order order){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId, ProductName, Quantity, Price, Discount, ProductImage) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getProductImage());

        db.execSQL(query);
    }

    public void clearCart(){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");

        db.execSQL(query);
    }

    //Wishlist

    public void addToWishlist(String productId){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Wishlist(ProductId) VALUES ('%s');", productId);

        db.execSQL(query);
    }

    public void clearWishlist(String productId){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Wishlist WHERE productId = '%s';", productId);

        db.execSQL(query);
    }

    public boolean currentWishlist(String productId){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Wishlist WHERE productId = '%s';", productId);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getCountCart(){
        int count = 0;

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do{
                count = cursor.getInt(0);
            }
            while(cursor.moveToNext());
        }
        return count;
    }
}
