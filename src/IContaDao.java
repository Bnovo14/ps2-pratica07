import java.util.List;
public interface IContaDao {
    boolean criar(Conta c);
    List<Conta> lerTodas();
    Conta buscarPeloNumero(long numero);
    boolean atualizar(Conta c);
    boolean apagar(Conta c);
}
