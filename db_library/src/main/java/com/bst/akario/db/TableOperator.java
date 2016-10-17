package com.bst.akario.db;

import net.sqlcipher.database.SQLiteDatabase;

public abstract class TableOperator {

	public abstract void doWork(SQLiteDatabase db);

}
