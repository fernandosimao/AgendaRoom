package br.com.alura.agenda.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/*
Resumo 14/04/2020:
Usamos essa classe para listar todos os migrations que originalmente estavam dentro de  Room.databaseBuilder no método getInstance() de
AgendaDatabase.java. Como os migrations (inclusão de coluna, alteração de tipo de coluna etc por exemplo) tendem a crescer com o tempo,
optamos por separá-las em uma nova classe por organização. Cada migration indica as alterações que estão sendo feitas no banco nesse atualização.
É preciso também informar em AgendaDatabase.java no marcador @Database a atualização da versão, assim na primeira atualização migration (1, 2)
o atributo version do marcador @Database deve ser atualizado para 2 e assim sucessivamente. Na migração abaixo (2 ,3) destacamos que o SQLITE
não suporta a exclusão de coluna ou alteração do seu tipo, como é possível em outros bancos. Nesse caso foi preciso criar uma nova tabela,
copiar os dados que interessavam da antiga para a nova, apagar a antiga e renomear a nova para o nome da antiga (tudo documentado abaixo).
Foram feitas aqui também algumas extrações para facilitação da leitura.
 */

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

