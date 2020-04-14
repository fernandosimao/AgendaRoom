package br.com.alura.agenda.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
Resumo 14/04/2020
Para implementação do Room, devemos ter um banco (AgendaDAtabase.java, uma entidade (Aluno.java) e um DAO (Interface AlunoDAO)). Nessa classe
definimos quem é a entidade a ser salva no banco através do marcador @Entity. Também precisamos indicar a chave primária @PrimaryKey. Usamos
o marcador @Ignore quando há varios contrutores e não queremos que o Room o utilize. O mesmo marcador serve para os atributos: atributos com o
@Ignore não são persistidos no banco de dados. É necessário implementar os getters e setters dos atributos para que o Room consiga ligar com
eles e adicioná-los ao banco.
 */

@Entity
public class Aluno implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String nome;
    //    private String sobrenome;
    private String telefone;
    private String email;
    //@ignore caso não queira persistir o atributo no SQLITE
    private Calendar momentoDeCadastro = Calendar.getInstance();

    @Ignore //quando há varios contrutores o Room só considerará um deles (o sem @Ignore). Os dois construtores abaixo podem ser apagados
    public Aluno(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public Aluno() {

    }


//    public String getSobrenome() {
//        return sobrenome;
//    }
//
//    public void setSobrenome(String sobrenome) {
//        this.sobrenome = sobrenome;
//    }

    // ^ foram necessários ser criados os geters e seters para momentoCadastro para que o Room possa colocar esses dados
    // no banco após a migration. No caso específico foi preciso criar a classe ConversorCalendar, já que o tipo Calendar
    // não é supoirtado no SQLITE
    public Calendar getMomentoDeCadastro() {
        return momentoDeCadastro;
    }

    public void setMomentoDeCadastro(Calendar momentoDeCadastro) {
        this.momentoDeCadastro = momentoDeCadastro;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String toString() {
        return nome + " - " + telefone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean temIdValido() {
        return id > 0;
    }

    public String getNomeCompleto() {
        return nome + " ";
    }

    public String dataFormatada(){
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        return formatador.format(momentoDeCadastro.getTime());

    }
}
