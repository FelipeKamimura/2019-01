package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.java.Log;

@Log
public class ClienteDAO {
    
    private Connection conn;
    // Responsável por criar a tabela Cliente no banco.
    public ClienteDAO() {

        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")) {

            log.info("Criando tabela cliente ...");
            conn.createStatement().executeUpdate(
            "CREATE TABLE cliente (" +
						"id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT id_cliente_pk PRIMARY KEY," +
						"nome varchar(255)," +
						"telefone varchar(30)," + 
						"idade int," + 
                        "limiteCredito double," +
                        "id_pais int)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void incluir (ClienteDTO dto) throws SQLException{
        
        try{
            String sql = "INSERT INTO cliente (nome, telefone, idade, limiteCredito. id_pais) VALUES ( ?, ?, ?, ?, ?)"; 
            PreparedStatement statement = this.conn.prepareStatement(sql);
           //id é gerado automaticamente
            statement.setString(1, dto.getNome());
            statement.setString(2, dto.getTelefone());
            statement.setInt(3, dto.getIdade());
            statement.setDouble(4, dto.getLimiteCredito());
            statement.setInt(5, dto.getPais().getId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0){
                if (rowsInserted > 0){
                    System.out.println("Cliente inserido com sucesso!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            this.conn.close();
        }
        
        
        
        
    }
    public void alterar (ClienteDTO dto) throws SQLException{
        try {
            String sql = "UPDATE cliente SET nome = ?, telefone = ?, idade = ?, limiteCredito = ?, id_pais = ? WHERE id = ?";
                
            PreparedStatement statement = this.conn.prepareStatement(sql);
            statement.setString(1, dto.getNome());
            statement.setString(2, dto.getTelefone());
            statement.setInt(4, dto.getIdade());
            statement.setDouble(5, dto.getLimiteCredito());
            statement.setInt(6, dto.getPais().getId());
                
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0){
                    System.out.println("Atualização feita com sucesso");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
        this.conn.close();
        }
    }
    public void excluir (ClienteDTO dto) throws SQLException{
         try {

           String sql = "DELETE FROM cliente WHERE id=?";
           PreparedStatement statement = this.conn.prepareStatement(sql);
           statement.setInt(1, dto.getId());
           
           int rowsDeleted = statement.executeUpdate();
           if (rowsDeleted > 0){
               System.out.println("O cliente foi deletado!");
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
         finally{
             this.conn.close();
         }
        
    }
    public List<ClienteDTO> listar() throws SQLException{
        try {

           List<ClienteDTO> list = new ArrayList<ClienteDTO>();
           String sql = "SELECT id, nome, telefone, idade, limiteCredito, id_pais FROM cliente ORDER BY id";
           PreparedStatement statement = this.conn.prepareStatement(sql);
           ResultSet rs = statement.executeQuery();
           try{
               while(rs.next()){
                   ClienteDTO cliente;
                   cliente = ClienteDTO.builder().id(rs.getInt(1)).nome(rs.getString(2)).
                           idade(rs.getInt(3)).limiteCredito(rs.getDouble(4)).
                           pais(PaisDTO.builder().id(rs.getInt(6)).build())
                          .build();
                   list.add(cliente);
               }
               return list;
           }
               catch (SQLException ex){
                System.out.println("Erro ao retornar: " + ex.getMessage());
               
               }
               finally{
                       conn.close();
               }
           
        } catch (Exception e) {
            e.printStackTrace();    
        }
        finally{
            this.conn.close();
            return null;
        }     
    }
}