package br.com.alura.agenda.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

class AgendaMigrations {
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) { //incluindo a coluna sobrenome
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Aluno ADD COLUMN sobrenome TEXT");

        }
    };
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) { //apagando a coluna sobrenome
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //QUANDO FOR NECESSÁRIO ALTERAR UM TIPO DE COLUNA OU REMOVE-LA, O SQLITE TEM ALGUMAS LIMITAÇÕES - POR EXEMPLO NÃO TEM
            // O DROP COLUMN ( database.execSQL("ALTER TABLE aluno DROP COLUMN sobrenome");) SENDO NECESSÁRIA A TÉCNICA ABAIXO

            //Criar nova tabela com as informações desejadas
            database.execSQL("CREATE TABLE IF NOT EXISTS `Aluno_novo`" +
                    " (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`nome` TEXT, " +
                    " `telefone` TEXT, " +
                    "`email` TEXT)");

            //Copiar dados da tabela antiga para a nova
            database.execSQL("INSERT INTO Aluno_novo (id, nome, telefone, email)" +
                    "SELECT id, nome, telefone, email FROM Aluno");

            //Remoção da tabela antiga
            database.execSQL("DROP TABLE Aluno");

            //Renomear a tabela nova com o nome da tabela antiga
            database.execSQL("ALTER TABLE Aluno_novo RENAME TO Aluno");


        }
    };
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //o nome da coluna momentoDeCadastro e o tipo INTEGER foram obtidos do AgendaDatabase_Impl.java, é importante que sejam iguais
            database.execSQL("ALTER TABLE Aluno ADD COLUMN momentoDeCadastro INTEGER");
        }
    };
    static final Migration[] TODAS_MIGRATIONS = {MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4};
}

