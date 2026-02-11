package br.com.uriel.dao;

import br.com.uriel.database.ConnectionFactory;
import br.com.uriel.model.Tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TasksDAO {

    public void save(Tasks task){
        String sql = "INSERT INTO tarefas (titulo, concluida, prioridade) VALUES(?,?,?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, task.getTitulo());
            stmt.setBoolean(2, task.isConcluida());
            stmt.setInt(3, task.getPrioridade());

            stmt.executeUpdate();
            System.out.println("Tarefa salva com sucesso!");
        } catch (SQLException e){
            System.err.println("Erro ao salvar tarefa: " + e.getMessage());
        }
    }

    public List<Tasks> listAll(){
        String sql = "SELECT * FROM tarefas ORDER BY id";
        List<Tasks> tasks = new ArrayList<>();

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                Tasks task = new Tasks();

                task.setId(rs.getInt("id"));
                task.setTitulo(rs.getString("titulo"));
                task.setConcluida(rs.getBoolean("concluida"));
                task.setPrioridade(rs.getInt("prioridade"));

                tasks.add(task);
            }
        } catch (SQLException e){
            System.out.println("Erro ao listar as tarefas: " + e.getMessage());
        }
        return tasks;
    }

    public void update(Tasks task){
        String sql = "UPDATE tarefas SET titulo = ?, concluida = ?, prioridade = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitulo());
            stmt.setBoolean(2, task.isConcluida());
            stmt.setInt(3, task.getPrioridade());
            stmt.setInt(4, task.getId());

            stmt.executeUpdate();
            System.out.println("Tarefa atualizada com Sucesso!");
        } catch(SQLException e){
            System.out.println("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    public void delete(int id){
        String sql = "DELETE FROM tabelas WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);

            stmt.executeUpdate();
            System.out.println("Tarefa excluída com sucesso!");
        } catch (SQLException e){
            System.out.println(("Erro ao deletar a tarefa: " + e.getMessage()));
        }
    }
}
