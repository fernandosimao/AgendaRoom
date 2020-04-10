package br.com.alura.agenda.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.alura.agenda.database.dao.RoomAlunoDAO;
import br.com.alura.agenda.model.Aluno;

@Database(entities = {Aluno.class}, version = 1, exportSchema = false) //exportSchema = false dessa maneira não é gerado o arquivo com o esquema de banco de dados
public abstract class AgendaDatabase extends RoomDatabase { //a classe que extende RoomDatabase deve ser abastrata para que o Room seja reponsavel para implementar o que precisa
    public abstract RoomAlunoDAO getRoomAlunoDAO();
}
