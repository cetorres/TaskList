package com.cetorres.listatarefas;

import java.util.List;

import com.cetorres.listatarefas.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ListaTarefas extends Activity {
	
	EditText txtTarefa;
	Button btnAdicionar;
	ListView lstTarefas;
	
	private BancoDados bd;
	protected static final int MENU_SOBRE = 1;
	protected static final int MENU_APAGAR = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Associa controles visuais às suas variáveis no código-fonte
        txtTarefa = (EditText)findViewById(R.id.txtTarefa);
        btnAdicionar = (Button)findViewById(R.id.btnAdicionar);
        lstTarefas = (ListView)findViewById(R.id.lstTarefas);
       
        // Configura o método que irá tratar o clique no botão Adicionar
		btnAdicionar.setOnClickListener(cliqueBotao);
		
		// Cria o objeto de banco de dados
		bd = new BancoDados(this);
		
		// Inicia a lista de tarefas
		iniciaLista();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, MENU_SOBRE, 0, R.string.menuSobre);
    	return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case MENU_SOBRE:
            	mostraSobre();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void mostraSobre() {
    	// Mostra mensagem sobre a aplicação
    	new AlertDialog.Builder(this)
        .setMessage("Aplicação desenvolvida como projeto final do Módulo I do Mini Curso de Desenvolvimento de Aplicações para Dispositivos Móveis ministrado em setembro de 2010 na Faculdade Christus pelo professor Carlos Eugênio Torres (cetorres@cetorres.com).")
        .setTitle(R.string.app_name)
        .setPositiveButton("Ok", null)
        .show();
    }
    
    private void iniciaLista() {
    	// Carrega itens da lista puxando do banco de dados
    	carregaTarefas();
    	
    	// Adiciona menu (ao clicar e segurar) à lista
    	lstTarefas.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
	    	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		    	menu.setHeaderTitle("Menu");
		    	menu.add(0, MENU_APAGAR, 0, R.string.menuApagarTarefa);
		    }
    	});
    } 
    
    // Trata clique no menu
    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
    	AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
	    switch (aItem.getItemId()) {
		    case MENU_APAGAR:
		    	TarefaModelo tarefa = (TarefaModelo)lstTarefas.getAdapter().getItem(menuInfo.position);
			    Apagar(tarefa.TarefaId);
			    return true;
	    }
	    return false;
    } 
    
    private void carregaTarefas() {
    	// Obtém lista de tarefas do banco de dados
		List<TarefaModelo> listaTarefas = bd.obtemTarefas();
		
		// Cria o adaptador para a lista de tarefas
		ArrayAdapter<TarefaModelo> adaptador = new ArrayAdapter<TarefaModelo>(this, android.R.layout.simple_list_item_1, listaTarefas);
		lstTarefas.setAdapter(adaptador);
		adaptador.notifyDataSetChanged();
    }
    
    // Trata o clique do botão Adicionar
    private OnClickListener cliqueBotao = new OnClickListener() {
		public void onClick(View v) {
			Adicionar(txtTarefa.getText().toString());			
		}
	};
    
	// Adiciona nova tarefa ao banco de dados
    private void Adicionar(String item) {
    	bd.insereTarefa(item);
    	txtTarefa.setText("");
    	Toast.makeText(this, "Nova tarefa criada", Toast.LENGTH_SHORT).show();
    	carregaTarefas();
    }
    
    // Apaga tarefa do banco de dados
    private void Apagar(Integer id) {
    	bd.apagaTarefa(id);
    	Toast.makeText(this, "Tarefa apagada", Toast.LENGTH_SHORT).show();
    	carregaTarefas();
    }
}