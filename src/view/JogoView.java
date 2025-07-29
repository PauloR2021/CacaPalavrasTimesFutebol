package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class JogoView extends JFrame {

    //Criando as Váriaveis Iniciais do APP
    private final GradeDePalavaras gradeDePalavaras;
    private JTable tabela;
    private DefaultTableCellRenderer[][] renderers;
    private Point pontoInicial, pontoFinal;
    private JLabel dicas;


    //Classe Principal
    public JogoView(){
        //Set dos atributos da Tela
        setTitle("Caça-Palavras");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Set das palavras que vão aparecer no cabeçalho do APP e no Caça Palavras
        String[] palavras = {"PALMERIAS","BOTAFOGO","FLAMENGO","SANTOS","CRUZEIRO","GREMIO"};
        this.gradeDePalavaras = new GradeDePalavras(palavras);
        montarInterface();
        setVisible(true);

    }
}
