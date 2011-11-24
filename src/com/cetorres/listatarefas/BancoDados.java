package com.cetorres.listatarefas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public class BancoDados {
	
	private static final String DATABASE_NAME = "listatarefas.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_TAREFA = "tarefa";
	
	private Context context;
	private SQLiteDatabase db;
	
	public BancoDados(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
	}
	
	public long insereTarefa(String tarefa) {
		SQLiteStatement stmt = this.db.compileStatement("INSERT INTO " + TABLE_TAREFA + "(tarefa_descricao) values (?)");
		stmt.bindString(1, tarefa);
		return stmt.executeInsert();
	}

	public long apagaTarefa(Integer tarefa_id) {
		SQLiteStatement stmt = this.db.compileStatement("delete from " + TABLE_TAREFA + " where tarefa_id = ?");
		stmt.bindString(1, tarefa_id.toString());
		return stmt.executeInsert();
	}
	   
	public List<TarefaModelo> obtemTarefas() {
		List<TarefaModelo> list = new ArrayList<TarefaModelo>();
	    Cursor cursor = this.db.query(TABLE_TAREFA, new String[] {"tarefa_id, tarefa_descricao"}, 
	    		null, null, null, null, null);
	    if (cursor.moveToFirst()) {
	         do {
	        	 TarefaModelo t = new TarefaModelo();
	        	 t.TarefaId = cursor.getInt(0);
	        	 t.TarefaDescricao = cursor.getString(1);        	 
	        	 list.add(t);  
	         } while (cursor.moveToNext());
	    }
	    if (cursor != null && !cursor.isClosed()) {
	       cursor.close();
	    }
	    return list;
	}
	
	private static class OpenHelper extends SQLiteOpenHelper {
	
	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }
	
	      @Override
	      public void onCreate(SQLiteDatabase db) {
	         db.execSQL("CREATE TABLE " + TABLE_TAREFA + "(tarefa_id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa_descricao TEXT)");
	      }
	
	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREFA);
	    	  onCreate(db);
	      }
	}
}
