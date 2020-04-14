package br.com.alura.agenda.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import br.com.alura.agenda.database.converter.ConversorCalendar;
import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

import static br.com.alura.agenda.database.AgendaMigrations.TODAS_MIGRATIONS;

/*
Resumo 14/04/2020:
Para implementação do Room, devemos ter um banco (AgendaDAtabase.java, uma entidade (Aluno.java) e um DAO (Interface AlunoDAO))
Essa classe é onde definmos o banco de dados SQLITE que será gerenciado pelo Room. Precisamos usar o marcador @Database pra indicar quais
serão as entidades (tabelas) e sempre atualizar o version após qualquer migration (atualização das tabelas). Nessa classe também indicamos
através do marcador @TypeConverters qual a classe que fará a conversão para da aplicação para o  banco e do banco para a aplicação
dos atributos que não são suportados nativamente pelo SQLITE (no nosso caso o atributo private Calendar momentoDeCadastro). Nessa classe
fazemos a implementação de dois métodos: o abstract AlunoDAO getRoomAlunoDAO que, sendo abstrato e do tipo do DAO, indicará que o Room deve
fazer a implementação da classe AlunoDAO (métodos insert, delete, update etc); o método getInstance, onde criamos uma instância do banco de
dados, assim cada classe que precisar entrar em contato com o banco, basta apenas chamar o método getInstance(), sem ter que implementar todas
essas linhas de código em diferente partes do APP, com o risco de errar. O Room tem alguns métodos: o databaseBuilder que constrói o banco,
o allowMainThreadQueries() que permite que as consultas rodem na mesma thread principal (não recomendado), o fallbackToDestructiveMigration()
que destrói e recria o banco inteiro (bom para mudanças nas tabelas quando a aplicação ainda não está em produção) e o addMigrations() que
faz as mudanças no banco (inclusão de coluna, alteração de tipo de coluna etc) sem apagar os dados que anteriormente foram incluídos (ideal
para o caso da aplicação já estar em produção)
 */

@Database(entities = {Aluno.class}, version = 4, exportSchema = false)
//é necessário atualizar a versão após modificação no banco (inclusão de coluna por ex)
//exportSchema = false dessa maneira não é gerado o arquivo com o esquema de banco de dados
@TypeConverters({ConversorCalendar.class})//para converter os tipos de atributos não suportados pelo SQL
public abstract class AgendaDatabase extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "agenda.db";


    public abstract AlunoDAO getRoomAlunoDAO();//Dessa maneira o Room vai pegar esse método abstrato, identificar que que ele é um
    // DAO já conhecido por causa da anotação (classe AlunoDAO) e vai fazer a implementação

    //para que a criação da instância do banco não tenha que ser criada em todas as classes, centralizamos aqui a criação dela
    public static AgendaDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, AgendaDatabase.class, NOME_BANCO_DE_DADOS)
                .allowMainThreadQueries()//permite rodar as consultas na trhead principal. Não recomendado pois pode ficar lenta a tela do usuário durante a consulta
                //.fallbackToDestructiveMigration() - foi inserida mais uma coluna: ou usa-se essa opção fallback para recriar o banco inteiro
                // (todos os dados serão perdidos - não fazer se o App já estiver em produção)ou se cria a nova coluna no esquema através de uma
                // nova query (migration) só usar essa opção fallback enquanto o banco não estiver publicado. Se estiver publicado, utilizar a migration
                .addMigrations(TODAS_MIGRATIONS)
                .build();
    }
}
