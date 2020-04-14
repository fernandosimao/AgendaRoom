package br.com.alura.agenda.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.Aluno;


/*
Resumo 14/04/2020:
O adapter facilita a implementação de uma lista e seus comportamentos. Primeiramente extendemos a classe BaseAdapter e depois precisamos
criar um objeto do tipo List e um contexto. Temos que implementar os 4 métodos: getCount() para sabermos o tamanho da lista,
getItem(int posicao) para sabermos qual item está selecionado, o getItemId(int posicao) caso precisemos saber qual a identificação de cada item
selecionado e finalmente o getView(int posicao, View view, ViewGroup viewGroup). Nesse último método, primeiramente criamos a view de cada item
da lista (criaView(ViewGroup viewGroup)). Essa criação (criaView) implica em inflar (tornar visivel na activity) o layout de criamos para cada item,
em nosso caso o item_aluno.xml (inflate(R.layout.item_aluno, viewGroup, false);). Depois de criar a view pegamos o aluno desejado através da
posição, com o método de lista get. De posse do aluno da lista fazemos a vinculação desse aluno buscado com a view criada anteriormente
 (vincula(viewCriada, alunoDevolvido);). A vinculação consiste em preencher as views da viewGroup criada (nome e telefone) identificadas através
 do id de cada um desses itens do layout com as informações que desejamos (em nosso caso o nome completo do aluno e seu telefone). Finalmente
 disponibilizamos dois métodos: um para atualização da lista atualiza(List<Aluno> alunos) e outro para remoção de um aluno da lista remove(Aluno aluno)
 Nesses dois métodos precisamos ao final precisamos inserir o método  notifyDataSetChanged() para que os dados sejam atualizados pelo adapter.
 */
public class ListaAlunosAdapter extends BaseAdapter {

    private final List<Aluno> alunos = new ArrayList<>();
    private final Context context;

    public ListaAlunosAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Aluno getItem(int posicao) {
        return alunos.get(posicao);
    }

    @Override
    public long getItemId(int posicao) {
        return alunos.get(posicao).getId();
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        View viewCriada = criaView(viewGroup);
        Aluno alunoDevolvido = alunos.get(posicao);
        vincula(viewCriada, alunoDevolvido);
        return viewCriada;
    }

    private void vincula(View view, Aluno aluno) {
        TextView nome = view.findViewById(R.id.item_aluno_nome);
        nome.setText(aluno.getNomeCompleto() + " " + aluno.dataFormatada());
        TextView telefone = view.findViewById(R.id.item_aluno_telefone);
        telefone.setText(aluno.getTelefone());
    }

    private View criaView(ViewGroup viewGroup) {
        return LayoutInflater
                .from(context)
                .inflate(R.layout.item_aluno, viewGroup, false);
    }

    public void atualiza(List<Aluno> alunos){
        this.alunos.clear();
        this.alunos.addAll(alunos);
        notifyDataSetChanged();
    }

    public void remove(Aluno aluno) {
        alunos.remove(aluno);
        notifyDataSetChanged();
    }
}
