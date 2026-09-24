import java.util.List;

public interface ITitularDao {
    boolean criar(Titular c);
    List<Titular> lerTodas();
    Conta buscarPeloNumero(long numero);
    boolean atualizar(Titular c);
    boolean apagar(Titular c);
}