package br.com.alura.agenda.database.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;

/*
Resumo: 14/04/2020
Para converter os tipos de atributos não suportados pelo SQL precisamos fazer uma conversão do que vai para o banco (paraLong(Calendar valor))
e do que volta dele (paraCalendar (Long valor)). A atributo inserido na nossa Entity Aluno.java, private Calendar momentoDeCadastro é de
um tipo não suportado navivamente pelo SQLITE. O tipo que "mais encaixa" a Calendário é long (ver documentação ). Além de escrever essa classe
de conversão, precisamos apontar no banco de dados (AgendaDatabase.java) o marcador @TypeConverters({ConversorCalendar.class})
 */

public class ConversorCalendar {

    @TypeConverter
    public Long paraLong(Calendar valor){
        if (valor !=null){
            return valor.getTimeInMillis();
        }
        return null;

    }

    @TypeConverter
    public Calendar paraCalendar (Long valor){
        Calendar momentoAtual = Calendar.getInstance();
        if (valor != null) {
            momentoAtual.setTimeInMillis(valor);
        }
        return momentoAtual;
    }

}
