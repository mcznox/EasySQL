package znox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Easy SQL</h1>
 * <h3>Fácil e prático de usar!</h3>
 * @author zNoX_
 * @version 1.1.3
 */
public class SQL implements Cloneable {

    // Variáveis para conexão
    protected String host, port, user, pass, db;

    // Variável de conexão
    protected Connection connection;

    // Variável de estado
    protected SQLEnum state;

    // Construção...
    public SQL(String host, String port, String user, String pass, String db) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.db = db;
        state = SQLEnum.Disconnect;
    }

    // Construção...
    public SQL(Connection connection) {
        this.connection = connection;
        state = SQLEnum.Disconnect;
    }

    // Construção...
    public SQL() { state = SQLEnum.Disconnect; }

    // Retorna a host
    public String getHost() { return host; }

    // Retorna a porta
    public String getPort() { return port; }

    // Retorna o usuário
    public String getUser() { return user; }

    // Retorn a senha
    public String getPass() { return pass; }

    // Retorna o nome da db (Database)
    public String getData() { return db; }

    // Retorna a conexão
    public Connection getConnection() { return connection; }

    // Retorna o estado
    public SQLEnum getState() { return state; }

    // Aplica um novo estado
    public SQLEnum setState(SQLEnum state) { return this.state = state; }

    // Abre uma nova conexão
    public SQL openConnection() {
        // Checar a conexão para não abrir mais de uma vez a mesma
        if (!connectionStatus()) {
            try {
                Class.forName("com.mysql.jdbc.Driver"); // Pega a classe dentro do metodo
                connection = DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getData(), getUser(), getPass()); // Assosia a conexão com o metodo do DriverManager
                setState(SQLEnum.Connect); // Muda o estado
            } catch (SQLException e) {
                // Erros...
                setState(SQLEnum.Error);
                getState().setDescription(e.getMessage());
                throw new Error("Erro ao abrir conexão em 'SQL': " + e.getMessage());
            } catch (ClassNotFoundException e) {
                // Erros...
                setState(SQLEnum.Error);
                getState().setDescription(e.getMessage());
                throw new Error("Classe para abrir conexão não localizada.");
            }
        }
        return this;
    }

    // Status da conexão (Somente para teste e afins)
    public boolean connectionStatus() {
        if (getState() == SQLEnum.Error) {
            return false;
        }
        if (connection != null) {
            return true;
        }
        return false;
    }

    // Cria valores dentro da tabela
    public SQL createValues(String table, List<String> stringList, List<String> stringList2) throws Exception {
        // Checa se existe algum erro na conexão, caso sim, retorna um novo erro
        if (getState() == SQLEnum.Error) {
            throw new Error("Erro ao criar update de valor em 'SQL': " + getState().getDescription());
        }

        // Cria a primeira parte do corpo do insert
        StringBuilder builder = new StringBuilder();
        for (String s : stringList) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(s);
        }

        // Cria a segunda parte do corpo do insert
        StringBuilder builder2 = new StringBuilder();
        for (String s : stringList2) {
            if (builder2.length() > 0) {
                builder2.append("', '");
            }
            builder2.append(s);
        }

        // Cria a linha do insert
        String query = "insert into " + table + " (" + builder.toString() + ") values ('" + builder2.toString() + "');";

        // Cria um novo Statement para a execução da linha
        Statement statement = getConnection().createStatement();

        // Executa a linha
        statement.executeQuery(query);

        return this;
    }

    // Retorna um velor dentro da tabela
    public Object getValue(String table, String[] values, String value) throws Exception {
        // Checa se existe algum erro na conexão, caso sim, retorna um novo erro
        if (getState() == SQLEnum.Error) {
            throw new Error("Erro ao resgatar valor em 'SQL': " + getState().getDescription());
        }

        // Cria uma list para os valores de busca dentro da tabela
        List<String> list = new ArrayList<String>();
        for (String string : values) {
            list.add(string);
        }

        // Cria a linha do select
        String query = "select * from " + table + " where " + list.get(0) + "='" + list.get(1) + "';";

        // Cria um novo Statement para a execução da linha
        Statement statement = getConnection().createStatement();

        // Cria um resultado dentro da linha de execução
        ResultSet set = statement.executeQuery(query);

        // Checa quando algum tipo de valor for retornável
        while (set.next()) {
            // Retorna um object (Já que podem ser vários tipos de variáveis dentro da tabela)
            return set.getObject(value);
        }

        return null;
    }

    // Atualiza um valor dentro da tabela
    public SQL updateValue(String table, String[] values) throws Exception {
        // Checa se existe algum erro na conexão, caso sim, retorna um novo erro
        if (getState() == SQLEnum.Error) {
            throw new Error("Erro ao criar update de valor em 'SQL': " + getState().getDescription());
        }

        // Cria uma list para os valores de busca dentro da tabela
        List<String> list = new ArrayList<String>();
        for (String string : values) {
            list.add(string);
        }

        // Cria a linha do update
        String update = "update " + table + " set " + list.get(0) + "='" + list.get(1) + "' where " + list.get(2) + "='"
                + list.get(3) + "';";

        // Cria um novo Statement para a execução da linha
        Statement statement = getConnection().createStatement();

        // Executa o update
        statement.executeUpdate(update);

        return this;
    }

    // Faz um clone da classe com todas as propriedades e valores dentro da mesma
    @Override
    public SQL clone() {
        try {
            // Retorna o clone da classe
            return (SQL) super.clone();
        } catch (Exception e) {
            // Erros...
            throw new Error("Erro ao gerar clone de 'SQL': " + e.getMessage());
        }

    }

    // Tipos de estados
    public enum SQLEnum {
        Error, Disconnect, Connect;

        // Descrição do estado
        String description;

        // Construção...
        SQLEnum(String description) {
            this.description = description;
        }

        // Construção...
        SQLEnum() {}

        // Retorna a descrição
        public String getDescription() { return description; }

        // Aplica uma nova descrição
        public String setDescription(String description) {
            return this.description = description;
        }
    }

}
