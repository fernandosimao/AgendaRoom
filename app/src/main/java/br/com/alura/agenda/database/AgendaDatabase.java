package br.com.alura.agenda.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import br.com.alura.agenda.database.converter.ConversorCalendar;
import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

import static br.com.alura.agenda.database.AgendaMigrations.TODAS_MIGRATIONS;

@Database(entities = {Aluno.class}, version = 4, exportSchema = false)
//é necessário atualizar a versão após modificação no banco (inclusão de coluna por ex)
//exportSchema = false dessa maneira não é gerado o arquivo com o esquema de banco de dados
@TypeConverters({ConversorCalendar.class})//para converter os tipos de atributos não suportados pelo SQL
public abstract class AgendaDatabase extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "agenda.db";


    public abstract AlunoDAO getRoomAlunoDAO();

    //para que a criação da instância do banco não tenha que ser criada em todas as classes, centralizamos aqui a criação dela
    public static AgendaDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, AgendaDatabase.class, NOME_BANCO_DE_DADOS)
                .allowMainThreadQueries()//permite rodar as consultas na trhead principal. Não recomendado pois pode ficar lenta a tela do usuário durante a consulta
                //.fallbackToDestructiveMigration() - foi inserida mais uma coluna: ou usa-se essa opção fallbaxk para recriar o banco inteiro (todos os dados serão perdidos)ou se cria a nova coluna no esquema através de uma
                // nova query (migration) só usar essa opção fallback enquanto o banco não estiver publicado. Se estiver publicado, utilizar a migration
                .addMigrations(TODAS_MIGRATIONS)
                .build();
    }
}
