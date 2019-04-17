package br.edu.utfpr.negocio;

import br.edu.utfpr.dao.ClienteDAO;
import java.util.List;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.excecao.NomeClienteJaExisteException;
import java.sql.SQLException;

public class ClienteNegocio {
    private final ClienteDAO dao = new ClienteDAO();

    public void incluir(ClienteDTO cliente) throws NomeClienteJaExisteException, SQLException {

        if (this.listar().stream().anyMatch(c -> c.getNome().equalsIgnoreCase(cliente.getNome())))
            throw new NomeClienteJaExisteException(cliente.getNome());
        
        // Chamar ClienteDAO para realizar persistência
        dao.incluir(cliente);
    }

    public List<ClienteDTO> listar() throws SQLException {
        return dao.listar();
        // Usar ClienteDAO para retornar valores no banco
    }
}