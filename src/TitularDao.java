import java.util.*;
import java.sql.*;

public class TitularDao implements ITitularDao {

    private PreparedStatement pstmCreate;
    private PreparedStatement pstmRead;
    private PreparedStatement pstmReadByNumero;
    private PreparedStatement pstmUpdate;
    private PreparedStatement pstmDelete;

    public TitularDao(Connection conexao) throws SQLException {
        pstmCreate = conexao.prepareStatement("INSERT INTO titulares VALUES (?,?,?,?)");
        pstmRead = conexao.prepareStatement("SELECT * FROM titulares");
        pstmReadByNumero = conexao.prepareStatement("SELECT * FROM titulares WHERE nro_titular=?");
        pstmUpdate = conexao.prepareStatement("UPDATE titulares SET nome=?, rg=?, cpf=? WHERE nro_titular=?");
        pstmDelete = conexao.prepareStatement("DELETE FROM titulares WHERE nro_titular=?");
    }

    @Override
    public boolean criar(Titular t) {
        boolean resposta = false;
        try {
            pstmCreate.setLong(1, t.nroTitular());
            pstmCreate.setString(2, t.nome());
            pstmCreate.setString(3, t.rg());
            pstmCreate.setString(4, t.cpf());
            int ret = pstmCreate.executeUpdate();
            resposta = (ret == 1);
        } catch (SQLException ex) {
            System.out.println("Erro ao criar titular!");
        }
        return resposta;
    }

    @Override
    public List<Titular> lerTodas() {
        List<Titular> titulares = new ArrayList<>();
        try {
            ResultSet rs = pstmRead.executeQuery();
            while (rs.next()) {
                long nro = rs.getLong("nro_titular");
                String nome = rs.getString("nome");
                String rg = rs.getString("rg");
                String cpf = rs.getString("cpf");
                titulares.add(new Titular(nro, nome, rg, cpf));
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao ler titulares!");
        }
        return titulares;
    }

    @Override
    public Titular buscarPeloNumero(long numero) {
        Titular t = null;
        try {
            pstmReadByNumero.setLong(1, numero);
            ResultSet rs = pstmReadByNumero.executeQuery();
            if (rs.next()) {
                long nro = rs.getLong("nro_titular");
                String nome = rs.getString("nome");
                String rg = rs.getString("rg");
                String cpf = rs.getString("cpf");
                t = new Titular(nro, nome, rg, cpf);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao buscar um titular!");
        }
        return t;
    }

    @Override
    public boolean atualizar(Titular t) {
        boolean resposta = false;
        try {
            pstmUpdate.setString(1, t.nome());
            pstmUpdate.setString(2, t.rg());
            pstmUpdate.setString(3, t.cpf());
            pstmUpdate.setLong(4, t.nroTitular());   // o número é o ÚLTIMO ?
            int ret = pstmUpdate.executeUpdate();
            resposta = (ret == 1);
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar titular!");
        }
        return resposta;
    }

    @Override
    public boolean apagar(Titular t) {
        boolean resposta = false;
        try {
            pstmDelete.setLong(1, t.nroTitular());
            int ret = pstmDelete.executeUpdate();
            resposta = (ret == 1);
        } catch (SQLException ex) {
            System.out.println("Erro ao apagar titular!");
        }
        return resposta;
    }
}