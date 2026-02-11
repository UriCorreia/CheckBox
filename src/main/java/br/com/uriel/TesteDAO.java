package br.com.uriel;

import br.com.uriel.dao.TasksDAO;
import br.com.uriel.model.Tasks;

import java.util.List;

public class TesteDAO {
    public static void main(String[] args) {

        TasksDAO dao = new TasksDAO();

        List<Tasks> list = dao.listAll();
        System.out.println("---Lista de Tarefas---");

        for(Tasks t : list) {
            System.out.println(t);
        }
    }
}