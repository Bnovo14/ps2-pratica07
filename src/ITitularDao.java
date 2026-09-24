import java.util.List;

public interface ITitularDao {
    boolean criar(Titular t);
    List<Titular> lerTodas();
    Titular buscarPeloNumero(long numero);
    boolean atualizar(Titular t);
    boolean apagar(Titular t);
}