import java.util.*;
import java.sql.*;

public class ContaDao implements IContaDao {

    private PreparedStatement pstmCreate;
    private PreparedStatement pstmRead;
    private PreparedStatement pstmReadByNumero;
    private PreparedStatement pstmUpdate;
    private PreparedStatement pstmDelete;

    public ContaDao(Connection conexao) throws SQLException {
        pstmCreate = conexao.prepareStatement("INSERT INTO contas VALUES (?,?)");
        pstmRead = conexao.prepareStatement("SELECT * FROM contas");
        pstmReadByNumero = conexao.prepareStatement("SELECT * FROM contas WHERE nro_conta=?");
        pstmUpdate = conexao.prepareStatement("UPDATE contas SET saldo=? WHERE nro_conta=?");
        pstmDelete = conexao.prepareStatement("DELETE FROM contas WHERE nro_conta=?");
    }

    @Override
    public boolean criar(Conta c) {
        boolean resposta = false;
        try {
            pstmCreate.setLong(1, c.nroConta());
            pstmCreate.setDouble(2, c.saldo());
            int ret = pstmCreate.executeUpdate();
            resposta = (ret == 1);
        } catch(SQLException ex) {
            IO.println("Erro ao criar conta!");
        }
        return resposta;
    }

    @Override
    public List<Conta> lerTodas() {
        List<Conta> contas = new ArrayList<>();
        try {
            ResultSet rs = pstmRead.executeQuery();
            while (rs.next()) {
                long nro = rs.getLong("nro_conta");
                double saldo = rs.getDouble("saldo");
                Conta c = new Conta(nro, saldo);
                contas.add(c);
            }
        } catch (SQLException ex) {
            IO.println("Erro ao ler contas!");
        }
        return contas;
    }

    @Override
    public Conta buscarPeloNumero(long numero) {
        Conta c = null;
        try {
            pstmReadByNumero.setLong(1, numero);
            ResultSet rs = pstmReadByNumero.executeQuery();
            if (rs.next()) {
                long nro = rs.getLong("nro_conta");
                double saldo = rs.getDouble("saldo");
                c = new Conta(nro, saldo);
            }
        } catch (SQLException ex) {
            IO.println("Erro ao buscar uma conta!");
        }
        return c;
    }

    @Override
    public boolean atualizar(Conta c) {
        boolean resposta = false;
        try {
            pstmUpdate.setDouble(1, c.saldo());
            pstmUpdate.setLong(2, c.nroConta());
            int ret = pstmUpdate.executeUpdate();
            resposta = (ret == 1);
        } catch(SQLException ex) {
            IO.println("Erro ao atualizar conta!");
        }
        return resposta;
    }

    @Override
    public boolean apagar(Conta c) {
        boolean resposta = false;
        try {
            pstmDelete.setLong(1, c.nroConta());
            int ret = pstmDelete.executeUpdate();
            resposta = (ret == 1);
        } catch(SQLException ex) {
            IO.println("Erro ao apagar conta!");
        }
        return resposta;
    }
    
}
