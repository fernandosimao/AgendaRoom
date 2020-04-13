package br.com.alura.agenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.alura.agenda.R;
import br.com.alura.agenda.database.AgendaDatabase;
import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

import static br.com.alura.agenda.ui.activity.ConstantesActivities.CHAVE_ALUNO;

/*
Resumo 13/04/2020: O formulário é aberto ao se clicar no botão + ou ao se clicar em um item da agenda ambos na ListaAlunos Activity.
Inicialmente setamos o layout (setContentView), criamos uma instância do banco de dados (AgendaDatabase.getInstance) e do DAO
 (database.getRoomAlunoDAO();). Após, fazemos a vinculação dos campos do FormularioActivity com as variáveis EditTexts
  correspondentes (inicializacaoDosCampos()). Finalmente carregamos o Aluno (carregaAluno()): se houver um extra, siginifica
  que um item foi clicado para edição e um aluno foi recebido, então setamos o título da APP_BAR para edição (setTitle) e
  preenchemos os nomes (preencheCampos();) com os atributos do aluno recebido. Se não houver um extra, então setamos o título
  da APP_BAR para inserção (outro setTitle) e então um aluno é criado vazio (aluno = new Aluno();). A partir desse momento
  aguardamos que o usuário clique no botão salvar (onCreateOptionsMenu). Após a seleção (onOptionsItemSelected) finalizamos
  o formulário (finalizaFormulario();). Inicialmente preenchemos os alunos com os dados digitados ou carregados dos
  EditTexts e setamos um objeto aluno com esses atributos( preencheAluno();). Ainda em finalizaFormulario(), se o aluno
  tem um id válido (> 0) fazemos o update do aluno no banco  (dao.edita(aluno)), caso não seja válido (id = 0) fazemos
  a inserção do aluno no banco (dao.salva(aluno);). Na criação de um aluno ele recebe um id = 0, então na primeira vez
  que ele é preenchido significa que precisamos fazer uma inserção no banco. Como o id é incrementado automaticamente no DAO,
  alunos existentes sempre terão id > 0, o que significa que ao finalizar o formulários eles serão atualizados (update)
*/


public class FormularioAlunoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR_NOVO_ALUNO = "Novo aluno";
    private static final String TITULO_APPBAR_EDITA_ALUNO = "Edita aluno";
    private EditText campoNome;
//    private EditText campoSobrenome;
    private EditText campoTelefone;
    private EditText campoEmail;
    private AlunoDAO dao;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_aluno);
        AgendaDatabase database = AgendaDatabase.getInstance(this);
        dao = database.getRoomAlunoDAO();
        inicializacaoDosCampos();
        carregaAluno();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.activity_formulario_aluno_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_formulario_aluno_menu_salvar){
            finalizaFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregaAluno() {
        Intent dados = getIntent();
        if (dados.hasExtra(CHAVE_ALUNO)) {
            setTitle(TITULO_APPBAR_EDITA_ALUNO);
            aluno = (Aluno) dados.getSerializableExtra(CHAVE_ALUNO);
            preencheCampos();
        } else {
            setTitle(TITULO_APPBAR_NOVO_ALUNO);
            aluno = new Aluno();
        }
    }

    private void preencheCampos() {
        campoNome.setText(aluno.getNome());
//        campoSobrenome.setText(aluno.getSobrenome());
        campoTelefone.setText(aluno.getTelefone());
        campoEmail.setText(aluno.getEmail());
    }

    private void finalizaFormulario() {
        preencheAluno();
        if (aluno.temIdValido()) {
            dao.edita(aluno);
        } else {
            dao.salva(aluno);
        }
        finish();
    }

    private void inicializacaoDosCampos() {
        campoNome = findViewById(R.id.activity_formulario_aluno_nome);
//        campoSobrenome = findViewById(R.id.activity_formulario_aluno_sobrenome);
        campoTelefone = findViewById(R.id.activity_formulario_aluno_telefone);
        campoEmail = findViewById(R.id.activity_formulario_aluno_email);
    }

    private void preencheAluno() {
        String nome = campoNome.getText().toString();
//        String sobrenome = campoSobrenome.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String email = campoEmail.getText().toString();

        aluno.setNome(nome);
//        aluno.setSobrenome(sobrenome);
        aluno.setTelefone(telefone);
        aluno.setEmail(email);
    }
}
