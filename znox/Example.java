package znox;

import java.util.ArrayList;
import java.util.List;

public class Example extends SQL {

    // Variável local para demonstração
    SQL sql;

    // Construção criar uma nova conexão
    public Example() {
        super("host", "port", "user", "pass", "db");
    }

    public void example() throws Exception {
        // Variável desprezivel
        Integer i = null;

        // Abrir uma conexão
        openConnection();

        if (i == 0) {
            // Lista para valores
            List<String> list = new ArrayList<String>();
            list.add("valor");
            list.add("valor2");
            list.add("valor3");

            // Lista para valores correspondentes
            List<String> list2 = new ArrayList<String>();
            list2.add("valor01");
            list2.add("valor02");
            list2.add("valor03");

            // Cria valores para uma tabela específica
            createValues("table", list, list2);

            // Irá criar a seguinte linha
            // insert into table (valor, valor2, valor3) values ('valor01', 'valor02', 'valor03');
        } else if (i == 1) {
            // Retorna o valor específicado na última variável
            getValue("table", new String[] { "jogador", "zNoX_" }, "mortes");

            // Irá criar a seguinte linha
            // select * from table where jogador='zNoX_';
            // return 'mortes';
        } else if (i == 2) {
            // Atualiza a tabela de acordo com a ordem da Linha
            updateValue("table", new String[] {"mortes", "1", "jogador", "zNoX_"});

            // Irá criar a seguinte linha
            // update set mortes='1' where jogador='zNoX_';
        }

        // Para clonar a classe SQL com todas as propriedades e variáveis já determinadas quando for extendida para uma classe
        sql = clone();

        // Método de utilização rápido para atualizar vários valores com a mesma linha
        sql
                .updateValue("table", new String[] { "mortes", "1", "jogador", "zNoX_" })
                .updateValue("table", new String[] { "matou", "3", "jogador", "zNoX_"});
    }

}
