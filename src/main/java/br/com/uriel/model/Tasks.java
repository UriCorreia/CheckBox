package br.com.uriel.model;

public class Tasks {
    private int id;
    private String titulo;
    private boolean concluida;
    private int prioridade;

    public Tasks(){}

    public Tasks(String titulo, int prioridade){
        this.titulo = titulo;
        this.prioridade = prioridade;
        this.concluida = false;
    }


    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getTitulo(){
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public boolean isConcluida(){
        return concluida;
    }
    public void setConcluida(boolean concluida){
        this.concluida = concluida;
    }

    public int getPrioridade(){
        return prioridade;
    }
    public void setPrioridade(int prioridade){
        this.prioridade = prioridade;
    }


    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", concluida=" + concluida +
                ", prioridade=" + prioridade +
                '}';
    }
}
