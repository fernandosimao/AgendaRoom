package br.com.alura.agenda.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.alura.agenda.model.Aluno;

/*
Resumo 14/04/2020:
Para implementação do Room, devemos ter um banco (AgendaDAtabase.java, uma entidade (Aluno.java) e um DAO (Interface AlunoDAO)). Nessa classe
indicamos através dos marcadores @Insert, @Delete, @Update etc os comportamentos que desejamos que os métodos façam no banco. Quando precisamos
de comportamentos personalizados usamos o marcador @Query e incluimos uma query SQL personalizada, foi o caso da função List<Aluno> todos()
 */

@Dao
public interface AlunoDAO {
    @Insert
    void salva(Aluno aluno);

    @Query("SELECT * FROM Aluno")
    List<Aluno> todos();

    @Delete
    void remove(Aluno aluno);

    @Update
    void edita(Aluno aluno);
}
