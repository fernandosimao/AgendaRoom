package br.com.alura.agenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.ui.ListaAlunosView;

import static br.com.alura.agenda.ui.activity.ConstantesActivities.CHAVE_ALUNO;


/*
Resumo 14/04/2020
Essa é a tela inicial do aplicativo. Nela exibimos os alunos já cadastrados no banco e damos a opção de ser cadastrado novo aluno.
Inicialemtne setamos o layout (setContentView(R.layout.activity_lista_alunos);) e o título da APP Bar ( setTitle(TITULO_APPBAR);).
Após criamos um objeto do tipo ListaAlunosView, que tem acesso ao DAO e ao Adapter. Inicialmente fazemos a configuração do botão
Fab (action button - configuraFabNovoAluno();) Esse método faz a vinculação de uma variável com o FAB e depois seta nele a ação de
clique (.setOnClickListener). Ao clicá-lo chamamos o método abreFormularioModoInsereAluno, que apenas chama a FormularioActivity
sem levar nenhum objeto para lá.
 Após a configuração do botão, fazemos a configuração da lista (configuraLista();). Inicialmente criamos uma variável ListView
 listaDeAlunos e fazemos a vinculação dela com o listview do layout.  Essa ListView lista de alunos então é passada como argumento
  do método configura adapder do objeto listaAlunosView. Esse método configuraAdapter consegue pegar um objeto do tipo ListView
  e chamar um de seus métodos, o setAdapter, que precisa receber um adapter como argumento. O adapter é criado através da classe
  ListaAlunosAdapter (ver documentação). Após configurar o adapter temos uma lista com vários itens (alunos) e gostaríamos de implementar
  uma ação para cada aluno clicado.  Caso haja usuários na lista, podemos clicar nele para fazer a edição ou exclusão.Para isso,
   ainda na configuração da lista (configuraLista()) chamamos o método configuraListenerDeCliquePorItem(listaDeAlunos) passando o
   ListView listaDeAlunos. Esse método pega o mesmo ListView listaDeAlunos e configura nele o setOnItemClickListener.
   Identificamos o aluno clicado através do método .getItemAtPosition. Finalmente, o método chamado após o clique é o
   abreFormularioModoEditaAluno, que envia o aluno clicado para o FormulárioActivity enviando um extra: um objeto
   do tipo aluno (vaiParaFormularioActivity.putExtra(CHAVE_ALUNO, aluno);). Finalmente para terminar a configuração da lista, colocamos
   o método registerForContextMenu e passamos novamente a ListView listaDeAlunos. Esse método vai nos permitir implementar a ação de clicar
   e segurar em um item da lista e depois executar uma ação (no nosso caso remoção). Primeiramente criamos um menu, activit_list_alunos_menu,
    que possua dentro dele um item (cujo id é activity_lista_alunos_menu_remover). Agora precisamos de dois métodos: o onCreateContextMenu
    que vai inflar (exibir na activity) o menu criado anteriormente e o onContextItemSelected que vai verificar se o item clicado foi o que
    criamos para remoção, no caso, (itemId = item.getItemId()). Caso confirmado, chamamos o método confirmaRemoção do objeo listaAlunosView,
    que por sua vez exibirá um AlertDialog perguntando se o usuário quer mesmo excluir o item. Finalmente para a lista de alunos ser atualizada
    na tela, no onResume() chamamos o método do objeto listaAlunosView chamado atualizaAlunos(). Esse método chama outro método, do adapter,
    o atualiza(dao.todos()); que vai limpar toda a lista (que está momentaneamente desatualizada) e pegar novamente todos os objetos dela.
 */
public class ListaAlunosActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Lista de alunos";
    private ListaAlunosView listaAlunosView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
        setTitle(TITULO_APPBAR);
        listaAlunosView = new ListaAlunosView(this);
        configuraFabNovoAluno();
        configuraLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater()
                .inflate(R.menu.activity_lista_alunos_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.activity_lista_alunos_menu_remover) {
            listaAlunosView.confirmaRemocao(item);
        }

        return super.onContextItemSelected(item);
    }

    private void configuraFabNovoAluno() {
        FloatingActionButton botaoNovoAluno = findViewById(R.id.activity_lista_alunos_fab_novo_aluno);
        botaoNovoAluno.setOnClickListener(view -> abreFormularioModoInsereAluno());
    }

    private void abreFormularioModoInsereAluno() {
        startActivity(new Intent(this, FormularioAlunoActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaAlunosView.atualizaAlunos();
    }

    private void configuraLista() {
        ListView listaDeAlunos = findViewById(R.id.activity_lista_alunos_listview);
        listaAlunosView.configuraAdapter(listaDeAlunos);
        configuraListenerDeCliquePorItem(listaDeAlunos);
        registerForContextMenu(listaDeAlunos);
    }

    private void configuraListenerDeCliquePorItem(ListView listaDeAlunos) {
        listaDeAlunos.setOnItemClickListener((adapterView, view, posicao, id) -> {
            Aluno alunoEscolhido = (Aluno) adapterView.getItemAtPosition(posicao);
            abreFormularioModoEditaAluno(alunoEscolhido);
        });
    }

    private void abreFormularioModoEditaAluno(Aluno aluno) {
        Intent vaiParaFormularioActivity = new Intent(ListaAlunosActivity.this, FormularioAlunoActivity.class);
        vaiParaFormularioActivity.putExtra(CHAVE_ALUNO, aluno);
        startActivity(vaiParaFormularioActivity);
    }

}
