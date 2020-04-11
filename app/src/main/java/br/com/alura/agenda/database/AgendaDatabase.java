package br.com.alura.agenda.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

@Database(entities = {Aluno.class}, version = 3, exportSchema = false)
//é necessário atualizar a versão após modificação no banco (inclusão de coluna por ex)
//exportSchema = false dessa maneira não é gerado o arquivo com o esquema de banco de dados
public abstract class AgendaDatabase extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "agenda.db";

    public abstract AlunoDAO getRoomAlunoDAO();

    //para que a criação da instância do banco não tenha que ser criada em todas as classes, centralizamos aqui a criação dela
    public static AgendaDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, AgendaDatabase.class, NOME_BANCO_DE_DADOS)
                .allowMainThreadQueries()//permite rodar as consultas na trhead principal. Não recomendado pois pode ficar lenta a tela do usuário durante a consulta
                //.fallbackToDestructiveMigration() - foi inserida mais uma coluna: ou usa-se essa opção fallbaxk para recriar o banco inteiro (todos os dados serão perdidos)ou se cria a nova coluna no esquema através de uma
                // nova query (migration) só usar essa opção fallback enquanto o banco não estiver publicado. Se estiver publicado, utilizar a migration
                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("ALTER TABLE aluno ADD COLUMN sobrenome TEXT");

                    }
                }, new Migration(2, 3) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        //QUANDO FOR NECESSÁRIO ALTERAR UM TIPO DE CLUNA OU REMOVELA, O SQLITE TEM ALGUMAS LIMITAÇÕES - POR EXEMPLO NÃO TEM O DROP COLUMN ( database.execSQL("ALTER TABLE aluno DROP COLUMN sobrenome");) SENDO NECESSÁRIO A TÉCNICA ABAIXO

                        //Criar nova tabela com as informações desejadas

                        //Copiar dados da tabela antiga para a nova

                        //Remoção da tabela antiga

                        //Renomear a tabela nova com o nome da tabela antiga


                    }
                })
                .build();
    }
}
