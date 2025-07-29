package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    //Classe para Montar a parte visual do Caça Palavra
    public void montarInterface(){

        dicas = new JLabel();  //Set da Label de dicas
        atualizarDicas(); //Classe para Atualizar as dicas que já foram achadas
        add(dicas,BorderLayout.NORTH); //Colocando as dicas na borda do lado Norte da Tela
        int TAM = gradeDePalavaras.getTamanho();
        String[] colunas = new String[TAM];
        for (int i = 0; i < TAM; i++) colunas[i] = String.valueOf(i + 1);

        String[][] dados = new String[TAM][TAM];
        renderers = new DefaultTableCellRenderer[TAM][TAM];

        char[][] grade = gradeDePalavaras.getGrade();
        for (int i = 0; i < TAM; i ++) {
            for(int j = 0; j < TAM; j++){
                dados[i][j] = String.valueOf(grade[i][j]);
                renderers[i][j] = new DefaultTableCellRenderer();
                renderers[i][j].setHorizontalAlignment(JLabel.CENTER);
            }
        }

        tabela = new JTable(dados, colunas) {
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (renderers[row][column].getBackground() != null) {
                    c.setBackground(renderers[row][column].getBackground());
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setRowHeight(40);
        tabela.setFont(new Font("Arial", Font.BOLD, 20));

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        dicas.setFont(new Font("Arial", Font.PLAIN, 16));
        add(dicas, BorderLayout.NORTH);

        tabela.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pontoInicial = tabela.getCellRect(tabela.rowAtPoint(e.getPoint()), tabela.columnAtPoint(e.getPoint()), true).getLocation();
            }

            public void mouseReleased(MouseEvent e) {
                pontoFinal = tabela.getCellRect(tabela.rowAtPoint(e.getPoint()), tabela.columnAtPoint(e.getPoint()), true).getLocation();
                processarSelecao();
            }
        });


    }
}
