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

    //Metodo para atualizar as dicas que foram acertadas
    private void atualizarDicas(){

        StringBuilder sb = new StringBuilder("<html>Palavras:"); // Cria um String Builder para chamar metodos HTML

        for(String palavra  : gradeDePalavaras.getPalavras()){ //Faz um for dentro do Get de Palavras
            if(gradeDePalavaras.getPalavrasRestantes().contains(palavras)){ //Verifica se A  palavra do For existe dentro do Get de Palavras do metodo
                sb.append(palavra).append(", "); //Se Existir ele adiciona
            }else { //Se não existir ele risca a palavra para mostrar que foi achada
                sb.append("<span style='text-decoration: line-through; font color: red;'>")
                        .append(palavra)
                        .append("</span>, ");
            }
        }

        if(sb.lastIndexOf(", ") != -1){
            sb.setLength(sb.length() - 2);
        }
        sb.append("</html>");

        if(dicas == null){
            dicas = new JLabel(sb.toString());
            dicas.setFont(new Font("Arial", Font.PLAIN, 16));
        }else{
            dicas.setText(sb.toString());
            dicas.revalidate();
            dicas.repaint();
        }
    }

    //Metodo para realizar as seleções das letras
    private void processarSelecao(){
        //criando as Variaveis de início e fim da tabela
        int linhaIni = tabela.rowAtPoint(pontoInicial);
        int colIni = tabela.columnAtPoint(pontoInicial);
        int linhaFim = tabela.rowAtPoint(pontoFinal);
        int colFim = tabela.columnAtPoint(pontoFinal);

        if(linhaIni == linhaFim){
            //Horizontal
            int inicio = Math.min(colIni, colFim);
            int fim = Math.max(colIni, colFim);
            StringBuilder sb = new StringBuilder();
            for(int i = inicio; i <= fim; i++){
                sb.append(gradeDePalavaras.getGrade()[linhaIni][i]);
            }
            verificarPalavra(sb.toString(), linhaIni, inicio, "HORIZONTAL", fim - inicio + 1 );

        }else if(colIni == colFim){
            //Vertical

            int inicio = Math.min(linhaIni, linhaFim);
            int fim = Math.max(linhaIni, linhaFim);
            StringBuilder sb = new StringBuilder();
            for (int i = inicio; i <= fim; i++) {
                sb.append(gradePalavras.getGrade()[i][colIni]);
            }
            verificarPalavra(sb.toString(), inicio, colIni, "VERTICAL", fim - inicio + 1);
        }
    }


    //Metodo para Verifificar se a palavra selecionada é a correta
    private void verificarPalavra(){
        if (gradePalavras.getPalavrasRestantes().contains(palavraSelecionada)) {
            for (int i = 0; i < tamanho; i++) {
                if (direcao.equals("HORIZONTAL")) {
                    renderers[linha][coluna + i].setBackground(Color.GREEN);
                } else {
                    renderers[linha + i][coluna].setBackground(Color.GREEN);
                }
            }
            gradePalavras.removerPalavra(palavraSelecionada);
            atualizarDicas();
            tabela.repaint();
            JOptionPane.showMessageDialog(this, "Palavra encontrada: " + palavraSelecionada);

            if (gradePalavras.todasEncontradas()) {
                JOptionPane.showMessageDialog(this, "Parabéns! Você encontrou todas as palavras!");
            }
        }
    }
}
